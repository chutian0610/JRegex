package io.github.chutian0610.jregex.ast;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Regex expression 的抽象父类
 *
 * @author victorchutian
 */
@Getter
@SuperBuilder
public abstract class RegexExp
{

    protected NodeType nodeType;

    public RegexExp(NodeType nodeType)
    {
        this.nodeType = nodeType;
    }

    /**
     * 访问者模式, 子类实现具体的 accept 方法
     *
     * @param visitor 访问者
     * @return 返回 T
     */
    public abstract <T, C> T accept(RegexExpVisitor<T, C> visitor, C context);
}

