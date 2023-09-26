package info.victorchu.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author victorchu
 */
@Builder
@Getter
@Setter
public class CharRangeExp  extends RegexCharExp
{
    public CharRangeExp(Character from,Character to)
    {
        super(NodeType.REGEX_CHAR_RANGE);
        this.from = from;
        this.to = to;
    }
    private Character from;
    private Character to;
    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitCharRange(this, context);
    }
}
