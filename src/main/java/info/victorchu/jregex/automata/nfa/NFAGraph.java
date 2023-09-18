package info.victorchu.jregex.automata.nfa;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.automata.NFAState;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.edge.EpsilonEdge;
import info.victorchu.jregex.util.AutoMateMermaidJSFormatter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author victorchu
 */

@Getter
@RequiredArgsConstructor(staticName = "of")
public class NFAGraph
{
    @NonNull
    private State start;
    @NonNull
    private StateManager stateManager;

    public List<String> toMermaidJsChartLines()
    {
        return AutoMateMermaidJSFormatter.INSTANCE.convertNFA2FlowChartLines(this);
    }

    public String toMermaidJsChart()
    {
        return AutoMateMermaidJSFormatter.INSTANCE.convertNFA2FlowChart(this);
    }

    /**
     * 找到状态的 ϵ 闭包
     *
     * @param state 初始状态Id
     * @return ϵ 闭包
     */
    private Set<Integer> computeEpsilonClosure(Integer state)
    {
        return dfsComputeEpsilonClosure(state, new HashSet<>());
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
        Optional<State> current = stateManager.getNFAState(state);
        if (!current.isPresent()) {
            throw new IllegalArgumentException("无效的NFA StateId:" + state);
        }
        Set<Integer> result = new HashSet<>();
        if (!marked.contains(state)) {
            // 记录当前节点
            result.add(state);
        }
        current.get().getTransitionsOfEdge(EpsilonEdge.INSTANCE).stream()
                // 记录未标记节点
                .filter(s -> !marked.contains(s.getState().getStateId())).map(s -> {
                    result.add(s.getState().getStateId());
                    marked.add(s.getState().getStateId());
                    // 记录下一级节点的 ϵ 闭包
                    return dfsComputeEpsilonClosure(s.getState().getStateId(), marked);
                }).forEach(result::addAll);
        return result;
    }
}
