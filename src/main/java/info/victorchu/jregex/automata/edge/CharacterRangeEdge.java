package info.victorchu.jregex.automata.edge;

import info.victorchu.jregex.automata.Edge;
import lombok.Getter;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author victorchu
 */
public class CharacterRangeEdge implements Edge
{
    @Getter
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
        return String.format("'%s'~'%s'",
                StringEscapeUtils.escapeJava(from.toString()),
                StringEscapeUtils.escapeJava(to.toString())
        );
    }
}
