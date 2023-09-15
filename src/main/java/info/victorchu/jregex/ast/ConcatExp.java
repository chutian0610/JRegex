package info.victorchu.jregex.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * concat expression.
 *
 * @author victorchutian
 */
@Getter
@Setter
@Builder
public class ConcatExp extends RegexExp {

    public ConcatExp(RegexExp left, RegexExp right) {
        super(NodeType.REGEX_CONCAT);
        this.left = left;
        this.right = right;
    }

    private RegexExp left;
    private RegexExp right;

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context) {
        return visitor.visitConcat(this, context);
    }
}

