package info.victorchu.jregex.automata;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author victorchu
 * 
 */
public class DFA
{
    private final Context context;
    private final DFAState start;

    public DFA(DFAState start, Context context)
    {
        this.start = start;
        this.context = context;
    }

    // ================================ simplify DFA =========================================

    /**
     * DFA 化简
     */
    public DFA simplify()
    {
        // 将 所有 DFA 状态分为 终结状态集合和非终结状态集合
        Set<Set<DFAState>> sets = context.getDFAStates()
                .stream()
                .collect(Collectors.groupingBy(DFAState::isAccept))
                .values().stream()
                .map(HashSet::new)
                .collect(Collectors.toSet());
        context.initMinimizationIndexMap(sets);

        simplify(sets);

        Set<DFAState> stateSet = context.searchDFAStateSet(this.start);
        DFAState start = createMinimizationDFAState(stateSet);
        return new DFA(start, context);
    }

    private DFAState createMinimizationDFAState(Set<DFAState> stateSet)
    {
        // 防止循环递归
        if (context.getMinimizationDFAState(stateSet) != null) {
            return context.getMinimizationDFAState(stateSet);
        }
        DFAState minState = context.createMinimizationDFAState(stateSet);
        getTransitions(stateSet).stream().forEach(transition -> {
            stateSet.forEach(item -> {
                item.getToStateOfTransition(transition).ifPresent(to -> {
                    Set<DFAState> toStateSet = context.searchDFAStateSet(to);
                    if (toStateSet != stateSet) {
                        DFAState minToState = createMinimizationDFAState(toStateSet);
                        minState.addTransition(transition, minToState);
                    }
                    else {
                        minState.addTransition(transition, minState);
                    }
                });
            });
        });
        return minState;
    }

    /**
     * 化简DFA集合列表
     *
     * @param sets DFA集合列表
     * @return
     */
    private void simplify(Set<Set<DFAState>> sets)
    {

        sets = sets.stream().flatMap(x -> {
            if (!canSplit(x)) {
                // 不可以分割
                context.registerSingleMinimizationSet(x);
                return Stream.empty();
            }
            else {
                return Stream.of(x);
            }
        }).collect(Collectors.toSet());
        int before = sets.size();
        sets = sets.stream().flatMap(x -> {
            Collection<Set<DFAState>> result = trySplit(x);
            if (!result.isEmpty()) {
                // 分割成功
                return result.stream();
            }
            else {
                return Stream.of(x);
            }
        }).collect(Collectors.toSet());
        int after = sets.size();
        if (before != after) {
            simplify(sets);
        }
    }

    private Collection<Set<DFAState>> trySplit(Set<DFAState> set)
    {
        for (Edge item : getTransitions(set)) {
            Map<Set<DFAState>, Set<DFAState>> mapping = new HashMap<>();
            // 没有对应转换的状态 -> 空集
            Set<DFAState> empty = new HashSet<>();
            for (DFAState state : set) {
                Optional<DFAState> op = state.getToStateOfTransition(item);
                if (op.isPresent()) {
                    op.ifPresent(to -> {
                        Set<DFAState> mappedSet = context.searchDFAStateSet(to);
                        Set<DFAState> sourceSet = mapping.getOrDefault(mappedSet, new HashSet<>());
                        sourceSet.add(state);
                        mapping.put(mappedSet, sourceSet);
                    });
                }
                else {
                    empty.add(state);
                }
            }
            if ((mapping.keySet().size() + (empty.isEmpty() ? 0 : 1)) > 1) {
                Set<Set<DFAState>> stateSet = new HashSet<>();
                stateSet.addAll(mapping.values());
                if (!empty.isEmpty()) {
                    stateSet.add(empty);
                }
                // 应用transition出现不同的结果子集
                context.refreshMinimizationIndexMap(stateSet);
                return stateSet;
            }
        }
        return Collections.emptySet();
    }

    private static Set<Edge> getTransitions(Set<DFAState> set)
    {
        return set.stream().flatMap(x -> x.getAllTransition().stream()).collect(Collectors.toSet());
    }

    /**
     * 判断DFAState是否可分
     *
     * @param dfaStates
     * @return
     */
    private static boolean canSplit(Set<DFAState> dfaStates)
    {
        return dfaStates != null && !dfaStates.isEmpty() && dfaStates.size() > 1;
    }

    // ================================ print DFA =============================================

    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @return
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Set<Integer> markSet = new HashSet<>();
        printState(start, sb, markSet);
        return sb.toString();
    }

    private static void printState(DFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            markSet.add(cursor.getId());
            List<Edge> edges = cursor.getSortedAllTransition();
            for (Edge edge : edges) {
                Optional<DFAState> state = cursor.getToStateOfTransition(edge);
                if (state.isPresent()) {
                    sb.append(cursor).append("-->|").append(edge).append("|").append(state.get()).append("\n");
                    printState(state.get(), sb, markSet);
                }
            }
        }
    }

    public String printMapping()
    {
        if (start.isMinimizationDFA()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n<<<<<<<<<<<< Min DFA -> DFA >>>>>>>>>>>>>\n");
            Set<Integer> markSet = new HashSet<>();
            printDFAMapping(start, sb, markSet);
            return sb.toString();
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("\n<<<<<<<<<<<< NFA -> DFA >>>>>>>>>>>>>\n");
            Set<Integer> markSet = new HashSet<>();
            printNFAMapping(start, sb, markSet);
            return sb.toString();
        }
    }

    private static void printDFAMapping(DFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            String nfaStr = String.format("(%s)", cursor.getMappedDFAStates().stream().map(x -> "ds_" + x.getId()).collect(Collectors.joining(",")));
            sb.append("ms_").append(cursor.getId()).append("<==>").append(nfaStr).append("\n");
            markSet.add(cursor.getId());
            List<Edge> edges = cursor.getSortedAllTransition();
            for (Edge edge : edges) {
                Optional<DFAState> state = cursor.getToStateOfTransition(edge);
                state.ifPresent(dfaState -> printDFAMapping(dfaState, sb, markSet));
            }
        }
    }

    private static void printNFAMapping(DFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            String nfaStr = String.format("(%s)", cursor.getMappedNFAStates().stream().map(x -> "ns_" + x.getId()).collect(Collectors.joining(",")));
            sb.append("ds_").append(cursor.getId()).append("<==>").append(nfaStr).append("\n");
            markSet.add(cursor.getId());
            List<Edge> edges = cursor.getSortedAllTransition();
            for (Edge edge : edges) {
                Optional<DFAState> state = cursor.getToStateOfTransition(edge);
                state.ifPresent(dfaState -> printNFAMapping(dfaState, sb, markSet));
            }
        }
    }
}
