package info.victorchu.jregex.automata.edge;

import info.victorchu.jregex.automata.Edge;
import lombok.Getter;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author victorchu
 */
@Getter
public class CharacterRangeEdge implements Edge
{
    protected Character from;
    @Getter
    protected Character to;

    public CharacterRangeEdge(Character from,Character to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString()
    {
        return String.format("['%s'-'%s']",
                StringEscapeUtils.escapeJava(from.toString()),
                StringEscapeUtils.escapeJava(to.toString())
        );
    }

    @Override
    public boolean canTrigger(Edge edge)
    {
        if (edge instanceof CharacterEdge) {
            CharacterEdge characterEdge = (CharacterEdge) edge;
            return characterEdge.getCharacter() >= from && characterEdge.getCharacter() <= to;
        }
        if (edge instanceof CharacterRangeEdge) {
            CharacterRangeEdge rangeEdge = (CharacterRangeEdge) edge;
            return rangeEdge.getFrom() >= from && rangeEdge.getTo() <= to;
        }
        return false;
    }
}
