package info.victorchu.jregex.automata.edge;

import info.victorchu.jregex.automata.Edge;
import lombok.EqualsAndHashCode;

/**
 * @author victorchu
 */
@EqualsAndHashCode
public class CharacterEdge
        implements Edge
{
    protected Character character;

    public CharacterEdge(Character character)
    {
        this.character = character;
    }

    @Override
    public String toString()
    {
        return String.format("'%s'", character);
    }
}
