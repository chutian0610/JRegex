package info.victorchu.jregex.ast;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * 简单正则表达式parser.
 *
 * <pre>
 * bnf 语法，递归下降
 *
 * {@literal <regex>} := {@literal <unionexp>}
 * {@literal <unionexp>} := {@literal <concatexp>} '|' {@literal <unionexp>}
 *              | {@literal <concatexp>}
 * {@literal <concatexp>} := {@literal <repeatexp>} {@literal <concatexp>}
 *              | {@literal <repeatexp>}
 * {@literal <concatexp>} := {@literal <repeatexp>} ('*' | '+' |'?')
 *              | {@literal <repeatexp>} '{' {@literal <number>} '}'
 *              | {@literal <repeatexp>} '{' {@literal <number>} ',' '}'
 *              | {@literal <repeatexp>} '{' {@literal <number>} ',' {@literal <number>} '}'
 *              | {@literal <charclassesexp>}
 * {@literal <charclassesexp>} := '[' {@literal <charclasses>} ']'
 *              | '[' '^' {@literal <charclasses>} ']'
 *              | {@literal <atomexp}
 *  {@literal <charclasses>} := {@literal <charclass>} {@literal <charclasses>}
 *              | {@literal <charclass>}
 *  {@literal <charclass>} := {@literal <charexp>} '-' {@literal <charexp>}
 *              | {@literal <charexp>}
 *  {@literal <atomexp>} := {@literal <charexp>}
 *              | '(' {@literal <unionexp>} ')'
 * {@literal <charexp>} ::= {@literal <Meta character>}
 *              | {@literal <Unicode character>}
 *              | '\' {@literal <Unicode character>}
 * </pre>
 * <p>
 * 将正则语法解析为AST Node.
 *
 * @author victorchutian
 */
public class RegexParser
{
    /**
     * 正则表达式字符串
     */
    private final String regexStr;
    private int position;

    /**
     * 解析正则表达式字符
     *
     * @param regexStr 正则表达式字符串
     * @return
     */
    public static RegexExp parse(String regexStr)
    {
        return new RegexParser(regexStr).parseUnionExp();
    }

    /**
     * 构造器
     *
     * @param regexStr 正则表达式字符串
     */
    private RegexParser(String regexStr)
    {
        this.regexStr = regexStr;
    }

    /**
     * 判断当前字符是否匹配输入字符
     *
     * @param c 输入字符
     * @return
     */
    private boolean matchChar(char c)
    {
        if (position >= regexStr.length()) {
            return false;
        }
        if (regexStr.charAt(position) == c) {
            position++;
            return true;
        }
        return false;
    }

    /**
     * 判断是否到达字符串尾部
     *
     * @return 是否到达字符串尾部
     */
    private boolean notEnd()
    {
        return position < regexStr.length();
    }

    /**
     * 当前指向字符是否在输入字符串中可以找到。
     *
     * @param s 输入字符串
     * @return
     */
    private boolean peek(String s)
    {
        return notEnd() && s.indexOf(regexStr.charAt(position)) != -1;
    }

    /**
     * 获取下一个字符
     *
     * @return
     * @throws IllegalArgumentException
     */
    private char next()
            throws IllegalArgumentException
    {
        if (!notEnd()) {
            throw new IllegalArgumentException("unexpected end-of-string");
        }
        return regexStr.charAt(position++);
    }

    /* 语法解析-递归下降 */

    /**
     * 语法解析入口
     *
     * @return
     */
    private RegexExp parseUnionExp()

    {
        RegexExp regexExp = parseConcatExp();
        if (matchChar('|')) {
            return OrExp.builder().left(regexExp).right(parseUnionExp()).build();
        }
        return regexExp;
    }

    private RegexExp parseConcatExp()
    {
        RegexExp regexExp = parseRepeatExp();
        if (notEnd() && !peek("|)")) {
            return ConcatExp.builder().left(regexExp).right(parseConcatExp()).build();
        }
        return regexExp;
    }

    private RegexExp parseRepeatExp()
    {
        RegexExp regexExp = parseCharClassesExp();
        while (peek("?*+{")) {
            if (matchChar('?'))
                return RepeatExp.builder().nodeType(NodeType.REGEX_REPEAT_OPTION).min(0).max(1).inner(regexExp).build();
            else if (matchChar('*'))
                return RepeatExp.builder().nodeType(NodeType.REGEX_REPEAT_MANY).min(0).inner(regexExp).build();
            else if (matchChar('+'))
                return RepeatExp.builder().nodeType(NodeType.REGEX_REPEAT_PLUS).min(1).inner(regexExp).build();
            else if (matchChar('{')) {
                int start = position;
                while (peek("0123456789")) {
                    next();
                }
                if (start == position)
                    throw new IllegalArgumentException("integer expected at position " + position);
                int n = Integer.parseInt(regexStr.substring(start, position));
                int m = -1;
                if (matchChar(',')) {
                    start = position;
                    while (peek("0123456789")) {
                        next();
                    }
                    if (start != position)
                        m = Integer.parseInt(regexStr.substring(start, position));
                } else {
                    m = n;
                }
                if (!matchChar('}'))
                    throw new IllegalArgumentException("expected '}' at position " + position);
                if (m == -1)
                    return RepeatExp.builder().nodeType(NodeType.REGEX_REPEAT_RANGE).min(n).inner(regexExp).build();
                else
                    return RepeatExp.builder().nodeType(NodeType.REGEX_REPEAT_RANGE).min(n).max(m).inner(regexExp).build();
            }
        }
        return regexExp;
    }
    private RegexExp parseCharClassesExp(){
        if (matchChar('[')) {
            boolean negate = matchChar('^');
            CharClassExp regex = parseCharClasses();
            regex.setNegative(negate);
            if (!matchChar(']')) {
                throw new IllegalArgumentException("expected ']' at position " + position);
            }
            return regex;
        } else {
            return parseAtomExp();
        }
    }
    private CharClassExp parseCharClasses(){
        List<RegexCharExp> regexExps = parseCharClass();
        while (notEnd() && !peek("]")){
            regexExps.addAll(parseCharClass());
        }
        return CharClassExp.builder().regexCharExpList(regexExps).build();
    }
    private List<RegexCharExp> parseCharClass(){
        RegexCharExp c = parseCharExp();
        if (matchChar('-')) {
            Validate.isTrue(c instanceof CharExp);
            if (peek("]")){
                return Lists.newArrayList(c,CharExp.builder().character('-').build());
            }else {
                int pos = position;
                RegexCharExp charExp = parseCharExp();
                if (!(charExp instanceof CharExp)) {
                    throw new IllegalArgumentException("expected simple char at position " + pos);
                }
                return Lists.newArrayList(CharRangeExp.builder().from(((CharExp) c).getCharacter()).to(((CharExp) charExp).getCharacter()).build());

            }
        }else {
            return Lists.newArrayList(c);
        }
    }

    private RegexExp parseAtomExp()
    {
        if (matchChar('(')) {
            RegexExp regex = parseUnionExp();
            if (matchChar(')')) {
                return regex;
            }
            else {
                throw new IllegalArgumentException("expected ')' at position " + position);
            }
        }
        else {
            return parseCharExp();
        }
    }

    private RegexCharExp parseCharExp()
    {
        if (matchChar('\\')) {
            if (matchChar('d')) {
                return MetaCharExp.builder().metaName("\\d").build();
            }
            if (matchChar('D')) {
                return MetaCharExp.builder().metaName("\\D").build();
            }
            if (matchChar('w')) {
                return MetaCharExp.builder().metaName("\\w").build();
            }
            if (matchChar('W')) {
                return MetaCharExp.builder().metaName("\\W").build();
            }
            if (matchChar('s')) {
                return MetaCharExp.builder().metaName("\\s").build();
            }
            if (matchChar('S')) {
                return MetaCharExp.builder().metaName("\\S").build();
            }
            // 转义字符
            return CharExp.builder().character(next()).build();
        }
        else {
            if (matchChar('.')) {
                return MetaCharExp.builder().metaName(".").build();
            }
            // 非转义
            return CharExp.builder().character(next()).build();
        }

    }
}

