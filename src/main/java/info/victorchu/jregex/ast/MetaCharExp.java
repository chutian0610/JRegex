package info.victorchu.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static info.victorchu.jregex.ast.NodeType.REGEX_META_CHAR;

/**
 * @author victorchu
 */
@Builder
@Getter
@Setter
public class MetaCharExp
        extends RegexCharExp
{
    public MetaCharExp(String metaName)
    {
        super(REGEX_META_CHAR);
        this.metaName = metaName;
    }

    private String metaName;

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context)
    {
        return visitor.visitMetaChar(this, context);
    }

    @Override
    public String toString()
    {
        return String.format("[MetaChar:%s]", getMetaName());
    }
}
