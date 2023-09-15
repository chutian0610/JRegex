package info.victorchu.jregex.util;

import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author victorchu
 */
@EqualsAndHashCode
@Getter
public class Transition
{

    public static Transition of(Edge edge, State state)
    {
        return new Transition(edge, state);
    }

    private Transition(Edge edge, State state)
    {
        this.edge = edge;
        this.state = state;
    }

    private final Edge edge;
    private final State state;

    @Override
    public String toString()
    {
        return "-- " + edge.toString() + " -->" + state.toString();
    }
}
