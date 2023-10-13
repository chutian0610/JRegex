package info.victorchu.jregex.automata;

import info.victorchu.jregex.automata.edge.CharacterEdge;
import info.victorchu.jregex.automata.edge.CharacterRangeEdge;
import info.victorchu.jregex.automata.edge.EpsilonEdge;
import info.victorchu.jregex.misc.CharRange;
import info.victorchu.jregex.misc.CharRanges;

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
