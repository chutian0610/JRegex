package info.victorchu.jregex.ast;

/**
 * 简单正则表达式parser.
 *
 * <pre>
 * bnf 语法，递归下降
 *
 * {@literal <regex>} ::= {@literal <unionexp>} '|' {@literal <regex>}
 *              | {@literal <unionexp>}
 * {@literal <unionexp>} ::= {@literal <concatexp>} {@literal <unionexp>}
 *              | {@literal <concatexp>}
 * {@literal <concatexp>} ::= {@literal <repeatexp>} '*'
 *              | {@literal <repeatexp>}
 * {@literal <repeatexp>} := {@literal <charexp>}
 *               | '(' {@literal <regexexp>} ')'
 * {@literal <charexp>} ::= {@literal <Unicode character>} | '\' {@literal <Unicode character>}
 * </pre>
 * <p>
 * 将正则语法解析为AST Node.
 *
 * @author victorchutian
 */
public class RegexParser {
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
    public static RegexExp parse(String regexStr) {
        return new RegexParser(regexStr).parseRegex();
    }

    /**
     * 构造器
     *
     * @param regexStr 正则表达式字符串
     */
    private RegexParser(String regexStr) {
        this.regexStr = regexStr;
    }

    /**
     * 判断当前字符是否匹配输入字符
     *
     * @param c 输入字符
     * @return
     */
    private boolean matchChar(char c) {
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
    private boolean notEnd() {
        return position < regexStr.length();
    }

    /**
     * 当前指向字符是否在输入字符串中可以找到。
     *
     * @param s 输入字符串
     * @return
     */
    private boolean peek(String s) {
        return notEnd() && s.indexOf(regexStr.charAt(position)) != -1;
    }

    /**
     * 获取下一个字符
     *
     * @return
     * @throws IllegalArgumentException
     */
    private char next() throws IllegalArgumentException {
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
    private RegexExp parseRegex() {
        RegexExp regexExp = parseUnionExp();
        if (matchChar('|')) {
            return OrExp.RegexOrNodeBuilder.aRegexOrNode().withLeft(regexExp).withRight(parseUnionExp()).build();
        }
        return regexExp;
    }

    private RegexExp parseUnionExp() {
        RegexExp regexExp = parseConcatExp();
        if (notEnd() && !peek("|)")) {
            return ConcatExp.RegexConcatNodeBuilder.aRegexConcatNode().withLeft(regexExp).withRight(parseUnionExp()).build();
        }
        return regexExp;
    }

    private RegexExp parseConcatExp() {
        RegexExp regexExp = parseRepeatExp();
        if (matchChar('*')) {
            return RepeatExp.RegexRepeatNodeBuilder.aRegexRepeatNode().withInnerNode(regexExp).build();
        }
        return regexExp;
    }

    private RegexExp parseRepeatExp() {
        if (matchChar('(')) {
            RegexExp regex = parseRegex();
            if (matchChar(')')) {
                return regex;
            } else {
                throw new IllegalArgumentException("expected ')' at position " + position);
            }
        } else {
            return parseCharExp();
        }
    }

    private RegexExp parseCharExp() {
        matchChar('\\');
        return CharExp.RegexCharNodeBuilder.aRegexCharNode().withCharacter(next()).build();
    }
}

