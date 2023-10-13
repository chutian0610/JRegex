package info.victorchu.jregex.automata.dfa;

import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.nfa.NFAGraph;
import info.victorchu.jregex.automata.state.GenericStateManager;
import info.victorchu.jregex.util.RegexTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author victorchu
 */
@DisplayName("DFA匹配测试")
@Slf4j
class DFAGraphMatcherTest
{
    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    @DisplayName("测试-concat")
    void matches01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("ab"));
    }

    @Test
    @DisplayName("测试-concat")
    void matches02()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertFalse(dfaGraphMatcher.matches("abb"));
    }

    @Test
    @DisplayName("测试-or")
    void matches03()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertFalse(dfaGraphMatcher.matches("ab"));
    }

    @Test
    @DisplayName("测试-or")
    void matches04()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("a"));
    }

    @Test
    @DisplayName("测试-repeat *")
    void matchesRepeat01()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertFalse(dfaGraphMatcher.matches("aaaaaaaaaaaaaa"));
    }

    @Test
    @DisplayName("测试-repeat *")
    void matchesRepeat02()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaab"));
    }

    @Test
    @DisplayName("测试-repeat +")
    void matchesRepeat03()
    {
        RegexExp regexExpression = RegexParser.parse("a+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertFalse(dfaGraphMatcher.matches("b"));
    }

    @Test
    @DisplayName("测试-repeat +")
    void matchesRepeat04()
    {
        RegexExp regexExpression = RegexParser.parse("a+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("ab"));
    }

    @Test
    @DisplayName("测试-repeat {n,m}")
    void matchesRepeat05()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,4}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("aaab"));
    }

    @Test
    @DisplayName("测试-repeat {n,m}")
    void matchesRepeat06()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,4}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertFalse(dfaGraphMatcher.matches("aaaaab"));
    }

    @Test
    @DisplayName("测试-复合表达式")
    void matches07()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaabb"));
    }

    @Test
    @DisplayName("测试-复合表达式")
    void matches08()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("ababababababababb"));
    }

    @Test
    @DisplayName("测试-复合表达式")
    void matches09()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertFalse(dfaGraphMatcher.matches("ababababababaabbb"));
    }
    @Test
    @DisplayName("测试-字符类")
    void matchesCharClass01()
    {
        RegexExp regexExpression = RegexParser.parse("[a-cA-]{1,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("A-b"));
    }

    @Test
    @DisplayName("测试-元字符")
    void matchesMetaChar01()
    {
        RegexExp regexExpression = RegexParser.parse("\\d+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("111b"));
    }

    @Test
    @DisplayName("测试-元字符")
    void matchesMetaChar02()
    {
        RegexExp regexExpression = RegexParser.parse(".+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("aaascbb"));
    }
}