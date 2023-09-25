package info.victorchu.jregex.automata.nfa;

import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.state.GenericStateManager;
import info.victorchu.jregex.util.RegexTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author victorchu
 */
class NFAGraphMatcherTest
{
    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    void matches01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("ab"));
    }

    @Test
    void matches02()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("abb"));
    }

    @Test
    void matches03()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("ab"));
    }

    @Test
    void matches04()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("a"));
    }

    @Test
    void matches05()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("aaaaaaaaaaaaaa"));
    }

    @Test
    void matches06()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaab"));
    }

    @Test
    void matches07()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaabb"));
    }

    @Test
    void matches08()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertTrue(nfaGraphMatcher.matches("ababababababababb"));
    }

    @Test
    void matches09()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        NFAGraphMatcher nfaGraphMatcher = new NFAGraphMatcher(nfa);
        Assertions.assertFalse(nfaGraphMatcher.matches("ababababababaabbb"));
    }
}