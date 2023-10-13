package info.victorchu.jregex.ast;

/**
 * RegexNode 的抽象访问者
 *
 * @author victorchutian
 */
public interface RegexExpVisitor<T, C>
{

    // --------------- 基于 RegexNode 具体类型分发 ------------------------

    T visitChar(CharExp node, C context);
    T visitCharRange(CharRangeExp node, C context);
    T visitCharClass(CharClassExp node, C context);

    T visitConcat(ConcatExp node, C context);

    T visitOr(OrExp node, C context);

    T visitRepeat(RepeatExp node, C context);

    T visitMetaChar(MetaCharExp node, C context);

    // 泛型入口
    default T process(RegexExp node, C context)
    {
        return node.accept(this, context);
    }
}
