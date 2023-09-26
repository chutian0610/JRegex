package info.victorchu.jregex.automata.dfa;

import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.nfa.NFAGraph;
import info.victorchu.jregex.automata.nfa.NFAGraphMatcher;
import info.victorchu.jregex.automata.state.GenericStateManager;
import info.victorchu.jregex.util.RegexTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author victorchu
 */
class DFAGraphMatcherTest
{
    private static final Logger log = LoggerFactory.getLogger(DFAGraphMatcherTest.class);

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
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("ab"));
    }

    @Test
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
    void matchesCharClass01()
    {
        RegexExp regexExpression = RegexParser.parse("[a-cA-]{1,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        DFAGraphMatcher dfaGraphMatcher = new DFAGraphMatcher(minDfa);
        Assertions.assertTrue(dfaGraphMatcher.matches("A-b"));
    }
}