package io.github.chutian0610.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 字符 expression.
 *
 * @author victorchutian
 */
@Builder
@Getter
@Setter
public class CharExp
        extends RegexCharExp
{
    public CharExp(Character character)
    {
        super(NodeType.REGEX_CHAR);
        this.character = character;
    }

    private Character character;

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitChar(this, context);
    }

    @Override
    public String toString()
    {
        return String.format("[Char:%s]", getCharacter());
    }
}

