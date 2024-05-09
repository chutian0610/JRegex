package io.github.chutian0610.jregex.ast;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 重复 expression.
 *
 * @author victorchutian
 */
@Getter
@Setter
@SuperBuilder
public class RepeatExp
        extends RegexExp
{
    public RepeatExp(NodeType nodeType,RegexExp inner)
    {
        super(nodeType);
        this.inner = inner;
    }

    private RegexExp inner;

    private Integer min;
    private Integer max;

    public String repeatStr(){

        switch (nodeType) {
            case REGEX_REPEAT_MANY:
                return "*";
            case REGEX_REPEAT_PLUS:
                return "+";
            case REGEX_REPEAT_OPTION:
                return "?";
            case REGEX_REPEAT_RANGE:
            default:
                if (max == null) {
                    return String.format("{%d,}", min);
                }
                return String.format("{%d,%d}", min, max);
        }
    }

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitRepeat(this, context);
    }

    @Override
    public String toString()
    {
        return String.format("[Repeat:%s]", repeatStr());
    }
}
