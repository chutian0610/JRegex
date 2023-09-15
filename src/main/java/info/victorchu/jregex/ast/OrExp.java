package info.victorchu.jregex.ast;

/**
 * or expression.
 *
 * @author victorchutian
 */
public class OrExp extends RegexExp {

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

    public OrExp() {
        super(NodeType.REGEX_OR);
    }

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context) {
        return visitor.visitOr(this, context);
    }

    /**
     * builder for  RegexOrNode
     */
    public static final class RegexOrNodeBuilder {
        private RegexExp left;
        private RegexExp right;

        private RegexOrNodeBuilder() {
        }

        public static RegexOrNodeBuilder aRegexOrNode() {
            return new RegexOrNodeBuilder();
        }

        public RegexOrNodeBuilder withLeft(RegexExp left) {
            this.left = left;
            return this;
        }

        public RegexOrNodeBuilder withRight(RegexExp right) {
            this.right = right;
            return this;
        }

        public OrExp build() {
            OrExp regexOrNode = new OrExp();
            regexOrNode.setLeft(left);
            regexOrNode.setRight(right);
            return regexOrNode;
        }
    }
}