package info.victorchu.jregex.automata;

import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author victorchu
 * @date 2023/9/14 19:33
 */
public class DFATest
{
    private static final Logger log = LoggerFactory.getLogger(NFATest.class);

    @Test
    void minimizationDFA01()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFA nfa = NFA.buildNFA(regexExpression);
        DFA dfa = nfa.toDFA();
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
        Assertions.assertEquals("ms_0(0)-->|'a'|ms_1(1)\n" +
                "ms_1(1)-->|'b'|ms_2((2))\n", minDfa.toString());
    }

    @Test
    void minimizationDFA02()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFA nfa = NFA.buildNFA(regexExpression);
        DFA dfa = nfa.toDFA();
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
        Assertions.assertEquals("ms_0(0)-->|'a'|ms_1((1))\n" +
                "ms_0(0)-->|'b'|ms_1((1))\n", minDfa.toString());
    }

    @Test
    void minimizationDFA03()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFA nfa = NFA.buildNFA(regexExpression);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
        Assertions.assertEquals("ms_0(0)-->|'b'|ms_2((2))\n" +
                "ms_0(0)-->|'a'|ms_0(0)\n", minDfa.toString());
    }

    @Test
    void minimizationDFA04()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFA nfa = NFA.buildNFA(regexExpression);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
    }
}
