package info.victorchu.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 重复 expression.
 *
 * @author victorchutian
 */
@Builder
@Getter
@Setter
public class RepeatExp
        extends RegexExp
{
    public RepeatExp(RegexExp inner)
    {
        super(NodeType.REGEXP_REPEAT);
        this.inner = inner;
    }

    private RegexExp inner;

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitRepeat(this, context);
    }
}
