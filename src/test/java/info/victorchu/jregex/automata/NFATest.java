package info.victorchu.jregex.automata;

import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.util.RegexExpTreePrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author victorchu
 * @date 2023/9/5 16:26
 */
class NFATest
{
    private static final Logger log = LoggerFactory.getLogger(NFATest.class);
    @Test
    void parse01()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        String tree = RegexExpTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.toString();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals(
                "ns_0(0)-->|'a'|ns_1(1)\n" +
                        "ns_1(1)-->|ϵ|ns_2(2)\n" +
                        "ns_2(2)-->|'b'|ns_3((3))\n", nfaStr);
    }

    @Test
    void parse02()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        String tree = RegexExpTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.toString();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals(
                "ns_0(0)-->|ϵ|ns_1(1)\n" +
                        "ns_1(1)-->|'a'|ns_2(2)\n" +
                        "ns_2(2)-->|ϵ|ns_5((5))\n" +
                        "ns_0(0)-->|ϵ|ns_3(3)\n" +
                        "ns_3(3)-->|'b'|ns_4(4)\n" +
                        "ns_4(4)-->|ϵ|ns_5((5))\n", nfaStr);
    }

    @Test
    void parse03()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        String tree = RegexExpTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.toString();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals(
                "ns_0(0)-->|ϵ|ns_1(1)\n" +
                        "ns_1(1)-->|'a'|ns_2(2)\n" +
                        "ns_2(2)-->|ϵ|ns_1(1)\n" +
                        "ns_2(2)-->|ϵ|ns_3(3)\n" +
                        "ns_3(3)-->|ϵ|ns_4(4)\n" +
                        "ns_4(4)-->|'b'|ns_5((5))\n" +
                        "ns_0(0)-->|ϵ|ns_3(3)\n", nfaStr);
    }

    @Test
    void parse04()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        String tree = RegexExpTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.toString();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals(
                "ns_0(0)-->|ϵ|ns_1(1)\n" +
                        "ns_1(1)-->|ϵ|ns_2(2)\n" +
                        "ns_2(2)-->|'a'|ns_3(3)\n" +
                        "ns_3(3)-->|ϵ|ns_6(6)\n" +
                        "ns_6(6)-->|ϵ|ns_1(1)\n" +
                        "ns_6(6)-->|ϵ|ns_7(7)\n" +
                        "ns_7(7)-->|ϵ|ns_8(8)\n" +
                        "ns_8(8)-->|'a'|ns_9(9)\n" +
                        "ns_9(9)-->|ϵ|ns_10(10)\n" +
                        "ns_10(10)-->|'b'|ns_11(11)\n" +
                        "ns_11(11)-->|ϵ|ns_12(12)\n" +
                        "ns_12(12)-->|'b'|ns_13((13))\n" +
                        "ns_1(1)-->|ϵ|ns_4(4)\n" +
                        "ns_4(4)-->|'b'|ns_5(5)\n" +
                        "ns_5(5)-->|ϵ|ns_6(6)\n" +
                        "ns_0(0)-->|ϵ|ns_7(7)\n", nfaStr);
    }

    @Test
    void toDFA01()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        Assertions.assertEquals("ds_0(0)-->|'a'|ds_1(1)\n" +
                "ds_1(1)-->|'b'|ds_2((2))\n", dfa.toString());
    }

    @Test
    void toDFA02()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        Assertions.assertEquals("ds_0(0)-->|'b'|ds_2((2))\n" +
                "ds_0(0)-->|'a'|ds_1((1))\n", dfa.toString());
    }

    @Test
    void toDFA03()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        Assertions.assertEquals("ds_0(0)-->|'b'|ds_2((2))\n" +
                "ds_0(0)-->|'a'|ds_1(1)\n" +
                "ds_1(1)-->|'b'|ds_2((2))\n" +
                "ds_1(1)-->|'a'|ds_1(1)\n", dfa.toString());
        Assertions.assertEquals("\n<<<<<<<<<<<< NFA -> DFA >>>>>>>>>>>>>\n" +
                "ds_0<==>(ns_1,ns_3,ns_4,ns_0)\n" +
                "ds_2<==>(ns_5)\n" +
                "ds_1<==>(ns_1,ns_2,ns_3,ns_4)\n", dfa.printMapping());
    }

    @Test
    void toDFA04()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        Assertions.assertEquals("ds_0(0)-->|'b'|ds_4(4)\n" +
                "ds_4(4)-->|'b'|ds_4(4)\n" +
                "ds_4(4)-->|'a'|ds_1(1)\n" +
                "ds_1(1)-->|'b'|ds_2(2)\n" +
                "ds_2(2)-->|'b'|ds_3((3))\n" +
                "ds_3((3))-->|'b'|ds_4(4)\n" +
                "ds_3((3))-->|'a'|ds_1(1)\n" +
                "ds_2(2)-->|'a'|ds_1(1)\n" +
                "ds_1(1)-->|'a'|ds_1(1)\n" +
                "ds_0(0)-->|'a'|ds_1(1)\n", dfa.toString());
    }
}