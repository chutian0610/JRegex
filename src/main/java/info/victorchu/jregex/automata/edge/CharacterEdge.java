package info.victorchu.jregex.automata.edge;

import info.victorchu.jregex.automata.Edge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author victorchu
 */
@EqualsAndHashCode
public class CharacterEdge
        implements Edge
{
    @Getter
    protected Character character;

    public CharacterEdge(Character character)
    {
        this.character = character;
    }

    @Override
    public String toString()
    {
        return String.format("'%s'", StringEscapeUtils.escapeJava(character.toString()));
    }
}
