package io.github.chutian0610.jregex.automata.nfa;

import io.github.chutian0610.jregex.ast.RegexExp;
import io.github.chutian0610.jregex.ast.RegexParser;
import io.github.chutian0610.jregex.automata.state.GenericStateManager;
import io.github.chutian0610.jregex.util.RegexTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author victorchu
 */
@Slf4j
@DisplayName("NFA匹配测试")
class NFAGraphMatcherTest
{
    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    void matchesConcat01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("ab"));
    }

    @Test
    void matchesConcat02()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("abb"));
    }

    @Test
    void matchesUnion01()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("ab"));
    }

    @Test
    void matchesUnion02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("a"));
    }

    @Test
    void matchesRepeat01()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("aaaaaaaaaaaaaa"));
    }

    @Test
    void matchesRepeat02()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaab"));
    }

    @Test
    void matchesRepeat03()
    {
        RegexExp regexExpression = RegexParser.parse("a+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaaab"));
    }

    @Test
    void matchesRepeat04()
    {
        RegexExp regexExpression = RegexParser.parse("a+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("b"));
    }

    @Test
    void matchesRepeat05()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("b"));
    }

    @Test
    void matchesRepeat06()
    {
        RegexExp regexExpression = RegexParser.parse("a?b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("b"));
    }

    @Test
    void matchesRepeat07()
    {
        RegexExp regexExpression = RegexParser.parse("a?b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("ab"));
    }

    @Test
    void matchesRepeat08()
    {
        RegexExp regexExpression = RegexParser.parse("a?b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("aab"));
    }

    @Test
    void matchesRepeat09()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("ab"));
    }

    @Test
    void matchesRepeat10()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aab"));
    }

    @Test
    void matchesRepeat11()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("aaaab"));
    }

    @Test
    void matchesRepeat12()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,5}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaab"));
    }

    @Test
    void matchesRepeat13()
    {
        RegexExp regexExpression = RegexParser.parse("a{3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaab"));
    }

    @Test
    void matchesRepeat14()
    {
        RegexExp regexExpression = RegexParser.parse("a{3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("aaaab"));
    }

    @Test
    void matchesRepeat15()
    {
        RegexExp regexExpression = RegexParser.parse("a{3,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaab"));
    }

    @Test
    void matchesRepeat16()
    {
        RegexExp regexExpression = RegexParser.parse("a{3,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("aaaab"));
    }

    @Test
    void matchesRepeat17()
    {
        RegexExp regexExpression = RegexParser.parse("a{3,}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaaaaab"));
    }

    @Test
    void matchesRepeat18()
    {
        RegexExp regexExpression = RegexParser.parse("a{1,5}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaab"));
    }
    @Test
    void matchesComplex01()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaabb"));
    }

    @Test
    void matchesComplex02()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("ababababababababb"));
    }

    @Test
    void matchesComplex03()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("ababababababaabbb"));
    }

    @Test
    void matchesCharClass01()
    {
        RegexExp regexExpression = RegexParser.parse("[a-cA-]{1,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("A-b"));
    }
    @Test
    void matchesCharClass02()
    {
        RegexExp regexExpression = RegexParser.parse("[^a-cA-]{1,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("dddb"));
    }

    @Test
    void matchesMetaChar01()
    {
        RegexExp regexExpression = RegexParser.parse("\\d+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("111b"));
    }

    @Test
    void matchesMetaChar02()
    {
        RegexExp regexExpression = RegexParser.parse(".+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("111ab"));
    }
}