package info.victorchu.jregex.ast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 字符 expression.
 *
 * @author victorchutian
 */
@Builder
@Getter
@Setter
public class CharExp extends RegexExp {
    public CharExp(Character character) {
        super(NodeType.REGEXP_CHAR);
        this.character = character;
    }

    private Character character;

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context) {
        return visitor.visitChar(this, context);
    }
}

