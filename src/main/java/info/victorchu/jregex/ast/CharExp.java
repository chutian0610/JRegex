package info.victorchu.jregex.ast;

/**
 * 字符 expression.
 *
 * @author victorchutian
 */
public class CharExp extends RegexExp {
    public CharExp() {
        super(NodeType.REGEXP_CHAR);
    }

    private Character character;

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context) {
        return visitor.visitChar(this, context);
    }

    /**
     * builder for  RegexCharNode
     */
    public static final class RegexCharNodeBuilder {
        private Character character;

        private RegexCharNodeBuilder() {
        }

        public static RegexCharNodeBuilder aRegexCharNode() {
            return new RegexCharNodeBuilder();
        }

        public RegexCharNodeBuilder withCharacter(Character character) {
            this.character = character;
            return this;
        }

        public CharExp build() {
            CharExp regexCharNode = new CharExp();
            regexCharNode.setCharacter(character);
            return regexCharNode;
        }
    }
}

