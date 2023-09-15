package info.victorchu.jregex.automata;

import info.victorchu.jregex.automata.edge.CharacterEdge;
import info.victorchu.jregex.automata.edge.EpsilonEdge;

/**
 * state ==|edge|==> state
 *
 * @author victorchu
 */
public interface Edge
{

    static Edge epsilon()
    {
        return EpsilonEdge.INSTANCE;
    }

    static Edge character(Character c)
    {
        return new CharacterEdge(c);
    }
}
