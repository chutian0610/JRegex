package io.github.chutian0610.jregex.automata.edge;

import io.github.chutian0610.jregex.automata.Edge;
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

    @Override
    public boolean canTrigger(Edge edge)
    {
        if (edge instanceof CharacterEdge) {
            return character.equals(((CharacterEdge) edge).getCharacter());
        }
        return false;
    }
}
