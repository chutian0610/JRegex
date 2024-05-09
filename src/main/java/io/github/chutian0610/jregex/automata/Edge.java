package io.github.chutian0610.jregex.automata;

import io.github.chutian0610.jregex.automata.edge.CharacterEdge;
import io.github.chutian0610.jregex.automata.edge.CharacterRangeEdge;
import io.github.chutian0610.jregex.automata.edge.EpsilonEdge;
import io.github.chutian0610.jregex.misc.CharRange;

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

    static Edge range(Character from, Character to)
    {
        if (from.equals(to)) {
            return new CharacterEdge(from);
        }
        return new CharacterRangeEdge(from, to);
    }

    static Edge fromCharRange(CharRange x)
    {
        if (x.isSingle()) {
            return Edge.character(x.getFrom());
        }
        else {
            return Edge.range(x.getFrom(), x.getTo());
        }
    }

    boolean canTrigger(Edge edge);
}
