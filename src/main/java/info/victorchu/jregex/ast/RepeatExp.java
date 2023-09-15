package info.victorchu.jregex.ast;

/**
 * 重复 expression.
 *
 * @author victorchutian
 */
public class RepeatExp extends RegexExp {
    public RepeatExp() {
        super(NodeType.REGEXP_REPEAT);
    }

    private RegexExp inner;

    public RegexExp getInner() {
        return inner;
    }

    public void setInner(RegexExp inner) {
        this.inner = inner;
    }

    @Override
    public <T, C> T accept(RegexExpVisitor<T, C> visitor, C context) {
        return visitor.visitRepeat(this, context);
    }

    /**
     * builder for  RegexRepeatNode
     */
    public static final class RegexRepeatNodeBuilder {
        private RegexExp innerNode;

        private RegexRepeatNodeBuilder() {
        }

        public static RegexRepeatNodeBuilder aRegexRepeatNode() {
            return new RegexRepeatNodeBuilder();
        }

        public RegexRepeatNodeBuilder withInnerNode(RegexExp innerNode) {
            this.innerNode = innerNode;
            return this;
        }

        public RepeatExp build() {
            RepeatExp regexRepeatNode = new RepeatExp();
            regexRepeatNode.setInner(innerNode);
            return regexRepeatNode;
        }
    }
}
