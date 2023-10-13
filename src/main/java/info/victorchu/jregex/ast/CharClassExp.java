package info.victorchu.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static info.victorchu.jregex.ast.NodeType.REGEX_CHAR_CLASS;

/**
 * @author victorchu
 */
@Builder
@Getter
@Setter
public class CharClassExp extends RegexExp
{
    public CharClassExp(List<RegexCharExp> regexCharExpList,Boolean negative)
    {
        super(REGEX_CHAR_CLASS);
        this.regexCharExpList = regexCharExpList;
        this.negative = negative;
    }

    private List<RegexCharExp> regexCharExpList;
    private Boolean negative;
    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitCharClass(this,context);
    }

    @Override
    public String toString()
    {
        return String.format("[CharClass: negative=%s]", getNegative());
    }
}
