package info.victorchu.jregex.util;

import com.google.common.base.Strings;
import info.victorchu.jregex.automata.nfa.NFAGraph;
import info.victorchu.jregex.automata.State;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author victorchu
 */
public class AutoMateMermaidJSFormatter
{

    public static final AutoMateMermaidJSFormatter INSTANCE = new AutoMateMermaidJSFormatter();

    // ================================print NFA =============================================

    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @return
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     */
    public String convertNFA2FlowChart(NFAGraph nfaGraph)
    {
        List<String> list = new ArrayList<>();
        list.add("flowchart LR");
        Set<Integer> markSet = new HashSet<>();
        handleState(nfaGraph.getStart(), list, markSet);
        return String.join("\n", list) + "\n";
    }

    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @return
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     */
    public List<String> convertNFA2FlowChartLines(NFAGraph nfaGraph)
    {
        List<String> list = new ArrayList<>();
        list.add("flowchart LR");
        Set<Integer> markSet = new HashSet<>();
        handleState(nfaGraph.getStart(), list, markSet);
        return list;
    }

    protected void handleState(State cursor, List<String> sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getStateId())) {
            markSet.add(cursor.getStateId());
            Set<Transition> transitions = cursor.getTransitions();
            for (Transition transition : transitions) {
                State state = transition.getState();
                sb.add(convertState2Line(cursor, transition));
                handleState(state, sb, markSet);
            }
        }
    }

    protected String convertState2Line(@NonNull State state, @NonNull Transition transition)
    {
        return convertState2Node(state) + "-->|" + transition.getEdge() + "|" + convertState2Node(transition.getState()) + "";
    }

    protected String convertState2Node(@NonNull State state)
    {
        return state.isAccept() ?
                String.format("s_%d((%d))", state.getStateId(), state.getStateId()) :
                String.format("s_%d(%d)", state.getStateId(), state.getStateId());
    }
}
