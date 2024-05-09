package io.github.chutian0610.jregex.automata.edge;

import io.github.chutian0610.jregex.automata.Edge;

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
