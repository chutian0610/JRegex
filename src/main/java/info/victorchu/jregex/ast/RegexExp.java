package info.victorchu.jregex.ast;

/**
 * Regex expression 的抽象父类
 *
 * @author victorchutian
 */
public abstract class RegexExp {

    protected NodeType nodeType;

    public RegexExp(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * 访问者模式, 子类实现具体的 accept 方法
     *
     * @param visitor 访问者
     * @return 返回 T
     */
    public abstract <T, C> T accept(RegexExpVisitor<T, C> visitor, C context);

    /**
     * 节点类型
     */
    public enum NodeType {
        /**
         * s|t 或
         */
        REGEX_OR,
        /**
         * st 连接
         */
        REGEX_CONCAT,
        /**
         * s* 重复
         */
        REGEXP_REPEAT,
        /**
         * 字符
         */
        REGEXP_CHAR
    }
}

