package info.victorchu.jregex.automata.nfa;

import com.google.common.collect.Sets;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.Graph;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.dfa.DFAGraph;
import info.victorchu.jregex.automata.edge.EpsilonEdge;
import info.victorchu.jregex.automata.state.GenericStateManager;
import info.victorchu.jregex.automata.Transition;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */

@Getter
@RequiredArgsConstructor(staticName = "of")
public class NFAGraph
        implements Graph
{
    @NonNull
    private State start;
    @NonNull
    private StateManager stateManager;

    public static NFAGraph build(RegexExp regexExp)
    {
        StateManager stateManager = new GenericStateManager();
        return NFAGraphBuilder.INSTANCE.apply(regexExp, stateManager);
    }

    public static NFAGraph build(RegexExp regexExp, StateManager stateManager)
    {
        return NFAGraphBuilder.INSTANCE.apply(regexExp, stateManager);
    }

    /**
     * 子集构造法从NFA生成DFA.
     *
     * @return DFAGraph
     */
    public DFAGraph toDFA()
    {
        // 计算 ϵ−closure(0)
        Set<Integer> startSet = computeEpsilonClosure(this.start.getStateId());
        // 基于NFA 状态集构建 DFA
        State state = createDFAState(startSet);
        // 构建DFA Graph
        return DFAGraph.of(state, stateManager, false);
    }

    public boolean isNFASetAccept(Set<Integer> nfaStates){
        return stateManager.isNFASetAccept(nfaStates);
    }

    /**
     * 递归创建DFA 状态
     *
     * @param nfaSet
     * @return
     */
    private State createDFAState(Set<Integer> nfaSet)
    {
        // 防止循环递归
        Optional<State> dfaOp = stateManager.getDFAState(nfaSet);
        if (dfaOp.isPresent()) {
            return dfaOp.get();
        }
        // 构建NFA集合 对应的DFA节点
        State dfa = stateManager.createOrGetDFAState(nfaSet);
        Map<Edge, Set<Integer>> map = findDFAMoveTable(nfaSet);
        if (!map.isEmpty()) {
            // 设置DFA跳转状态
            map.entrySet().stream()
                    .filter((x) -> dfa.getTransitionsOfInputEdge(x.getKey()).isEmpty())
                    .forEach(x -> {
                        // 给DFA设置转换
                        State state = createDFAState(x.getValue());
                        dfa.addTransition(x.getKey(), state);
                    });
        }
        return dfa;
    }

    /**
     * 查找NFA Set 和 edge 状态move 集合
     *
     * @param nfaSet NFA状态
     * @return
     */
    private Map<Edge, Set<Integer>> findDFAMoveTable(Set<Integer> nfaSet)
    {
        Map<Edge, Set<Integer>> res = new HashMap<>();
        for (Edge edge : getAllEdgesOfStateSet(nfaSet)) {
            res.put(edge, findDFAMoveSet(nfaSet, edge));
        }
        return res;
    }

    /**
     * 获取NFA状态集合的所有有效输入
     *
     * @param nfaSet NFA 状态
     * @return
     */
    private Set<Edge> getAllEdgesOfStateSet(Set<Integer> nfaSet)
    {
        // 获取对当前状态集有效的字符集(排除 epsilon)
        return stateManager.getNfAEdges(nfaSet)
                .stream()
                .filter(edge -> edge != EpsilonEdge.INSTANCE)
                .collect(Collectors.toSet());
    }

    /**
     * 查找NFA集合应用某个输入的转换结果集
     *
     * @param nfaSet NFA状态集合
     * @param edge 输入
     * @return
     */
    public Set<Integer> findDFAMoveSet(Set<Integer> nfaSet, Edge edge)
    {
        Set<Integer> res = new HashSet<>();
        for (Integer s : nfaSet) {
            Set<Transition> next = stateManager.tryGetNFAState(s).getTransitionsOfInputEdge(edge);
            if (!next.isEmpty()) {
                Set<Integer> marked = Sets.newHashSet();
                next.forEach(x -> {
                    res.addAll(computeEpsilonClosure(x.getState().getStateId(),marked));
                });
            }
        }
        return res;
    }

    /**
     * 找到 NFA 状态的 ϵ 闭包.
     * 从 NFA 状态 s 出发，只通过 ϵ 边能到达的状态集合
     *
     * @param state 初始状态Id
     * @return ϵ 闭包
     */
    public Set<Integer> computeEpsilonClosure(Integer state)
    {
        return dfsComputeEpsilonClosure(state, new HashSet<>());
    }

    private Set<Integer> computeEpsilonClosure(Integer state,Set<Integer> marked)
    {
        return dfsComputeEpsilonClosure(state, Sets.newHashSet(marked));
    }

    /**
     * 找到状态的 ϵ 闭包
     *
     * @param state 初始状态
     * @param marked 已标记的状态
     * @return ϵ 闭包
     */
    private Set<Integer> dfsComputeEpsilonClosure(Integer state, Set<Integer> marked)
    {
        State current = stateManager.tryGetNFAState(state);
        Set<Integer> result = new HashSet<>();
        if (!marked.contains(state)) {
            // 记录当前节点
            result.add(state);
        }
        current.getTransitionsOfInputEdge(EpsilonEdge.INSTANCE).stream()
                // 记录未标记节点
                .filter(s -> !marked.contains(s.getState().getStateId())).map(s -> {
                    result.add(s.getState().getStateId());
                    marked.add(s.getState().getStateId());
                    // 记录next节点的 ϵ 闭包
                    return dfsComputeEpsilonClosure(s.getState().getStateId(), marked);
                }).forEach(result::addAll);
        return result;
    }
}
