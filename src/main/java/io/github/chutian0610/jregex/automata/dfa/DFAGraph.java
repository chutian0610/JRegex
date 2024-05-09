package io.github.chutian0610.jregex.automata.dfa;

import com.google.common.collect.Sets;
import io.github.chutian0610.jregex.automata.Edge;
import io.github.chutian0610.jregex.automata.Graph;
import io.github.chutian0610.jregex.automata.State;
import io.github.chutian0610.jregex.automata.StateManager;
import io.github.chutian0610.jregex.misc.CharRanges;
import io.github.chutian0610.jregex.misc.Pair;
import io.github.chutian0610.jregex.automata.Transition;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author victorchu
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class DFAGraph
        implements Graph
{
    @NonNull private State start;
    @NonNull private StateManager stateManager;
    @NonNull private Boolean minimized;

    /**
     * DFA 化简 (Hopcroft)算法
     *
     * @return
     */
    public DFAGraph simplify()
    {
        // DFA 状态和DFA状态所在集合
        Map<Integer, Set<Integer>> dfaStateSetIndexMap = new HashMap<>();
        // 将所有DFA 状态分为 终结状态集合和非终结状态集合
        Set<Set<Integer>> sets = initSplitedSet(start.getStateId(), dfaStateSetIndexMap);
        // 分割DFA状态集进行化简
        trySplitSet(sets, dfaStateSetIndexMap);
        Set<Integer> stateSet = searchDFAIndexMap(this.start.getStateId(), dfaStateSetIndexMap);
        State start = createMinimizationDFAState(stateSet, dfaStateSetIndexMap);
        return new DFAGraph(start, stateManager, true);
    }

    /**
     * 获取状态集合的所有有效输入
     *
     * @param dfaSet DFA 状态
     * @return
     */
    private Set<Edge> getAllEdgesOfStateSet(Set<Integer> dfaSet)
    {
        return CharRanges.fromEdges(stateManager.getDfAEdges(dfaSet))
                .expand()
                .stream()
                .map(Edge::fromCharRange)
                .collect(Collectors.toSet());
    }

    private State createMinimizationDFAState(Set<Integer> stateSet, Map<Integer, Set<Integer>> dfaStateSetIndexMap)
    {
        // 防止循环递归
        if (stateManager.getMinimizationDFAState(stateSet).isPresent()) {
            return stateManager.getMinimizationDFAState(stateSet).get();
        }
        State minState = stateManager.createOrGetMinimizationDFAState(stateSet);
        getAllEdgesOfStateSet(stateSet).forEach(edge -> {
            stateSet.forEach(item -> {
                stateManager.tryGetDFAState(item).getTransitionsOfInputEdge(edge).forEach(transition -> {
                    Set<Integer> toStateSet = searchDFAIndexMap(transition.getTargetId(), dfaStateSetIndexMap);
                    if (toStateSet != stateSet) {
                        State minToState = createMinimizationDFAState(toStateSet, dfaStateSetIndexMap);
                        minState.addTransition(transition.getEdge(), minToState);
                    }
                    else {
                        minState.addTransition(transition.getEdge(), minState);
                    }
                });
            });
        });
        return minState;
    }

    private void trySplitSet(Set<Set<Integer>> sets, Map<Integer, Set<Integer>> dfaStateSetIndexMap)
    {
        sets = sets.stream().flatMap(x -> {
            if (!canSplit(x)) {
                // 不可以分割的集合
                return Stream.empty();
            }
            else {
                // 可以分割的集合需要继续尝试分割
                return Stream.of(x);
            }
        }).collect(Collectors.toSet());
        // 记录下尝试分割前的集合数
        int before = sets.size();

        sets = sets.stream().flatMap(x -> {
            Collection<Set<Integer>> result = trySplit(x, dfaStateSetIndexMap);
            if (!result.isEmpty()) {
                // 分割成功
                return result.stream();
            }
            else {
                // 分割不成功
                return Stream.of(x);
            }
        }).collect(Collectors.toSet());
        // 记录下尝试分割后的集合数
        int after = sets.size();
        if (before != after) {
            // 如果成功分割，继续尝试分割
            trySplitSet(sets, dfaStateSetIndexMap);
        }
    }

    public Set<Integer> searchDFAIndexMap(Integer state, Map<Integer, Set<Integer>> dfaStateSetIndexMap)
    {
        return dfaStateSetIndexMap.get(state);
    }

    public void refreshDFAIndexMap(Collection<Set<Integer>> dfaStateSets, Map<Integer, Set<Integer>> dfaStateSetIndexMap)
    {
        dfaStateSets.forEach(x -> x.forEach(y -> {
            dfaStateSetIndexMap.put(y, x);
        }));
    }

    private Collection<Set<Integer>> trySplit(Set<Integer> set, Map<Integer, Set<Integer>> dfaStateSetIndexMap)
    {
        for (Edge item : getAllEdgesOfStateSet(set)) {
            // 去向集合 <-> 状态分组
            Map<Set<Integer>, Set<Integer>> mapping = new HashMap<>();
            // 空集状态分组(死状态)
            // 如果当前状态没有对应edge的转换状态，记录到emptyTransition中
            Set<Integer> emptyTransition = new HashSet<>();
            for (Integer state : set) {
                State dfa = stateManager.tryGetDFAState(state);
                Set<Transition> transitions = dfa.getTransitionsOfInputEdge(item);
                if (!transitions.isEmpty()) {
                    transitions.forEach(transition -> {
                        // 在索引中搜索target所在的DFA集合
                        Set<Integer> mappedSet = searchDFAIndexMap(transition.getTargetId(), dfaStateSetIndexMap);
                        Set<Integer> subSetGroup = mapping.getOrDefault(mappedSet, new HashSet<>());
                        // 在分组中记录下当前状态
                        subSetGroup.add(state);
                        mapping.put(mappedSet, subSetGroup);
                    });
                }
                else {
                    emptyTransition.add(state);
                }
            }

            if ((mapping.keySet().size() + (emptyTransition.isEmpty() ? 0 : 1)) > 1) {
                // 当前集合存在多个子分组
                Set<Set<Integer>> stateSet = new HashSet<>(mapping.values());
                if (!emptyTransition.isEmpty()) {
                    stateSet.add(emptyTransition);
                }
                // 出现分组拆分，需要刷新索引
                refreshDFAIndexMap(stateSet, dfaStateSetIndexMap);
                return stateSet;
            }
        }
        return Collections.emptySet();
    }

    /**
     * 判断DFAState是否可分
     *
     * @param dfaStates
     * @return
     */
    private static boolean canSplit(Set<Integer> dfaStates)
    {
        return dfaStates != null && !dfaStates.isEmpty() && dfaStates.size() > 1;
    }

    /**
     * 分割DFA状态为终结状态集合和非终结状态集合
     *
     * @param state
     * @return
     */
    private Set<Set<Integer>> initSplitedSet(Integer state, Map<Integer, Set<Integer>> dfaStateSetIndexMap)
    {
        Pair<Set<Integer>, Set<Integer>> pair = dfsSplitSet(state, new HashSet<>());
        Set<Set<Integer>> result = Sets.newHashSet(pair.getLeft(), pair.getRight());
        refreshDFAIndexMap(result, dfaStateSetIndexMap);
        return result;
    }

    private Pair<Set<Integer>, Set<Integer>> dfsSplitSet(Integer state, Set<Integer> marked)
    {
        if (marked.contains(state)) {
            return Pair.of(Collections.emptySet(), Collections.emptySet());
        }
        marked.add(state);
        Set<Integer> accept = Sets.newHashSet();
        Set<Integer> unAccept = Sets.newHashSet();
        State dfa = stateManager.tryGetDFAState(state);
        if (dfa.isAccept()) {
            accept.add(state);
        }
        else {
            unAccept.add(state);
        }
        dfa.getTransitions().stream().forEach(transition -> {
            Pair<Set<Integer>, Set<Integer>> sub = dfsSplitSet(transition.getState().getStateId(), marked);
            accept.addAll(sub.getLeft());
            unAccept.addAll(sub.getRight());
        });
        return Pair.of(accept, unAccept);
    }

    public String printStateMapping()
    {
        StringBuilder sb = new StringBuilder();
        Set<Integer> markSet = new HashSet<>();
        if (this.getMinimized()) {
            sb.append("\n<<<<<<<<<<<< Min DFA -> DFA >>>>>>>>>>>>>\n");
            printDFA2DFAMapping(this.getStart(), sb, markSet);
        }
        else {
            sb.append("\n<<<<<<<<<<<< NFA -> DFA >>>>>>>>>>>>>\n");
            printDFA2NFAMapping(this.getStart(), sb, markSet);
        }
        return sb.toString();
    }

    private void printDFA2NFAMapping(State cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getStateId())) {
            String nfaStr = String.format("(%s)", stateManager.getDFAMappedNFAState(cursor).stream().map(x -> "s_" + x.getStateId()).collect(Collectors.joining(",")));
            sb.append("s_").append(cursor.getStateId()).append("<==>").append(nfaStr).append("\n");
            markSet.add(cursor.getStateId());
            Set<Transition> transitions = cursor.getTransitions();
            for (Transition transition : transitions) {
                printDFA2NFAMapping(transition.getState(), sb, markSet);
            }
        }
    }

    private void printDFA2DFAMapping(State cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getStateId())) {
            String nfaStr = String.format("(%s)", stateManager.getMinDFAMappedDFAState(cursor).stream().map(x -> "s_" + x.getStateId()).collect(Collectors.joining(",")));
            sb.append("s_").append(cursor.getStateId()).append("<==>").append(nfaStr).append("\n");
            markSet.add(cursor.getStateId());
            Set<Transition> transitions = cursor.getTransitions();
            for (Transition transition : transitions) {
                printDFA2DFAMapping(transition.getState(), sb, markSet);
            }
        }
    }
}
