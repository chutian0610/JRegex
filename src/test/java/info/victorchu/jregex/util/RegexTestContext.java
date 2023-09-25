package info.victorchu.jregex.util;

import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.Transition;
import info.victorchu.jregex.automata.dfa.DFAGraph;
import lombok.Getter;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */
public class RegexTestContext
{
    @Getter
    private final StateManager stateManager;

    public RegexTestContext(StateManager stateManager)
    {
        this.stateManager = stateManager;
        reset();
    }

    public synchronized void reset()
    {
        stateManager.reset();
    }

    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Collection<T> items) {
        return IsIterableContainingInAnyOrder.<T>containsInAnyOrder((T[]) items.toArray());
    }
    public static String chart2ExpectString(List<String> chart){
        return "\"" + String.join("\",\n\"", chart) + "\"";
    }
    public String printDFA2DFAMapping(DFAGraph dfaGraph)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<<<<<<<<<<<< Min DFA -> DFA >>>>>>>>>>>>>\n");
        Set<Integer> markSet = new HashSet<>();
        printDFA2DFAMapping(dfaGraph.getStart(), sb, markSet);
        return sb.toString();
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

    public String printDFA2NFAMapping(DFAGraph dfaGraph)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<<<<<<<<<<<< NFA -> DFA >>>>>>>>>>>>>\n");
        Set<Integer> markSet = new HashSet<>();
        printDFA2NFAMapping(dfaGraph.getStart(), sb, markSet);
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
}
