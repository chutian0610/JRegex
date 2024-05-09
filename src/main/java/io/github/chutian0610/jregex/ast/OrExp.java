package io.github.chutian0610.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * or expression.
 *
 * @author victorchutian
 */

@Builder
@Getter
@Setter
public class OrExp
        extends RegexExp
{
    public OrExp(RegexExp left, RegexExp right)
    {
        super(NodeType.REGEX_OR);
        this.left = left;
        this.right = right;
    }

    private RegexExp left;
    private RegexExp right;

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitOr(this, context);
    }

    @Override
    public String toString()
    {
        return "[Or]";
    }
}