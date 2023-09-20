package info.victorchu.jregex.automata;

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

    public Integer getTargetId()
    {
        return state.getStateId();
    }

    @Override
    public String toString()
    {
        return "-- " + edge.toString() + " -->" + state.toString();
    }
}
