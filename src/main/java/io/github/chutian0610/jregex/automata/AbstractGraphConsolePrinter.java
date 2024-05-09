package io.github.chutian0610.jregex.automata;

import com.google.common.collect.Lists;
import lombok.NonNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */
public abstract class AbstractGraphConsolePrinter
        implements GraphConsolePrinter
{
    @Override
    public List<String> printLines(Graph graph)
    {
        List<String> list = Lists.newArrayList();
        traverse(graph.getStart(), list);
        return list;
    }

    protected void traverse(State cursor, List<String> sb)
    {
        Set<Integer> markSet = new HashSet<>();
        traverse(cursor, sb, markSet);
    }

    protected void traverse(State cursor, List<String> sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getStateId())) {
            markSet.add(cursor.getStateId());
            List<Transition> transitions = cursor.getTransitions()
                    .stream()
                    .sorted(Comparator.comparing(Transition::toString))
                    .collect(Collectors.toList());
            for (Transition transition : transitions) {
                State state = transition.getState();
                sb.add(handleTransition(cursor, transition));
                traverse(state, sb, markSet);
            }
        }
    }

    protected String handleTransition(@NonNull State state, @NonNull Transition transition)
    {
        return state2Str(state) + "-->|" + edge2Str(transition.getEdge()) + "|" + state2Str(transition.getState());
    }

    protected String edge2Str(@NonNull Edge edge)
    {
        return edge.toString();
    }

    protected String state2Str(@NonNull State state)
    {
        return state.toString();
    }
}
