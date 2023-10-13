package info.victorchu.jregex.automata.edge;

import info.victorchu.jregex.automata.Edge;

/**
 * @author victorchu
 */
public enum EpsilonEdge
        implements Edge
{
    INSTANCE;

    @Override
    public String toString()
    {
        return "ϵ";
    }

    @Override
    public boolean canTrigger(Edge edge)
    {
        return edge == INSTANCE;
    }
}
