package io.github.chutian0610.jregex.ast;

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
    REGEX_REPEAT_MANY,
    /**
     * s+
     */
    REGEX_REPEAT_PLUS,
    /**
     * s?
     */
    REGEX_REPEAT_OPTION,
    /**
     * s{n,m}
     * s{n,}
     */
    REGEX_REPEAT_RANGE,

    REGEX_CHAR_CLASS,

    /**
     * 字符
     */
    REGEX_CHAR,

    /**
     * 字符范围
     */
    REGEX_CHAR_RANGE,

    /**
     * 元字符
     */
    REGEX_META_CHAR

}
