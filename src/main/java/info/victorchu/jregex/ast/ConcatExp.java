package info.victorchu.jregex.ast;

/**
 * concat expression.
 *
 * @author victorchutian
 */
public class ConcatExp extends RegexExp {

    private RegexExp left;
    private RegexExp right;

    public RegexExp getLeft() {
        return left;
    }

    public void setLeft(RegexExp left) {
        this.left = left;
    }

    public RegexExp getRight() {
        return right;
    }

    public void setRight(RegexExp right) {
        this.right = right;
    }

    public ConcatExp() {
        super(NodeType.REGEX_CONCAT);
    }

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context) {
        return visitor.visitConcat(this, context);
    }

    /**
     * builder for  RegexConcatNode
     */
    public static final class RegexConcatNodeBuilder {
        private RegexExp left;
        private RegexExp right;

        private RegexConcatNodeBuilder() {
        }

        public static RegexConcatNodeBuilder aRegexConcatNode() {
            return new RegexConcatNodeBuilder();
        }

        public RegexConcatNodeBuilder withLeft(RegexExp left) {
            this.left = left;
            return this;
        }

        public RegexConcatNodeBuilder withRight(RegexExp right) {
            this.right = right;
            return this;
        }

        public ConcatExp build() {
            ConcatExp regexConcatNode = new ConcatExp();
            regexConcatNode.setLeft(left);
            regexConcatNode.setRight(right);
            return regexConcatNode;
        }
    }
}

