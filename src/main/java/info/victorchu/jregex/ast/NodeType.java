package info.victorchu.jregex.ast;

/**
 * 节点类型
 */
public enum NodeType
{
    /**
     * s|t 或
     */
    REGEX_OR,
    /**
     * st 连接
     */
    REGEX_CONCAT,
    /**
     * s*
     */
    REGEXP_REPEAT_MANY,
    /**
     * s+
     */
    REGEXP_REPEAT_PLUS,
    /**
     * s?
     */
    REGEXP_REPEAT_OPTION,
    /**
     * s{n,m}
     * s{n,}
     */
    REGEXP_REPEAT_RANGE,

    /**
     * 字符
     */
    REGEXP_CHAR
}
