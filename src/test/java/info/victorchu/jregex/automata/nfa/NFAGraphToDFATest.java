package info.victorchu.jregex.automata.nfa;

import com.google.common.collect.Lists;
import info.victorchu.jregex.util.RegexTestContext;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.dfa.DFAGraph;
import info.victorchu.jregex.automata.state.GenericStateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static info.victorchu.jregex.util.RegexTestContext.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */
class NFAGraphToDFATest
{
    private static final Logger log = LoggerFactory.getLogger(NFAGraphToDFATest.class);

    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    void toDFA01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))"),
                containsInAnyOrder(chart));
    }

    @Test
    void toDFA02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0097;\"|s_1((1))"),
                containsInAnyOrder(chart));
    }

    @Test
    void toDFA03()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))", "s_1(1)-->|\"#0097;\"|s_1(1)"),
                containsInAnyOrder(chart));
    }

    @Test
    void toDFA04()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2(2)", "s_2(2)-->|\"#0098;\"|s_3((3))", "s_3((3))-->|\"#0098;\"|s_4(4)", "s_3((3))-->|\"#0097;\"|s_1(1)", "s_2(2)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0097;\"|s_1(1)", "s_0(0)-->|\"#0097;\"|s_1(1)"),
                containsInAnyOrder(chart));
    }
    @Test
    void toDFA05()
    {
        RegexExp regexExpression = RegexParser.parse("[a-cA-]{1,3}b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_15(15)", "s_15(15)-->|\"#0097;\"|s_9(9)", "s_9(9)-->|\"#0099;\"|s_8(8)", "s_8(8)-->|\"#0098;\"|s_4((4))", "s_9(9)-->|\"#0098;\"|s_7((7))", "s_7((7))-->|\"#0098;\"|s_4((4))", "s_9(9)-->|\"#0097;\"|s_5(5)", "s_5(5)-->|\"#0098;\"|s_4((4))", "s_9(9)-->|\"#0065;\"|s_6(6)", "s_6(6)-->|\"#0098;\"|s_4((4))", "s_9(9)-->|\"#0045;\"|s_3(3)", "s_3(3)-->|\"#0098;\"|s_4((4))", "s_15(15)-->|\"#0065;\"|s_10(10)", "s_10(10)-->|\"#0099;\"|s_8(8)", "s_10(10)-->|\"#0098;\"|s_7((7))", "s_10(10)-->|\"#0097;\"|s_5(5)", "s_10(10)-->|\"#0065;\"|s_6(6)", "s_10(10)-->|\"#0045;\"|s_3(3)", "s_15(15)-->|\"#0099;\"|s_12(12)", "s_12(12)-->|\"#0099;\"|s_8(8)", "s_12(12)-->|\"#0098;\"|s_7((7))", "s_12(12)-->|\"#0097;\"|s_5(5)", "s_12(12)-->|\"#0065;\"|s_6(6)", "s_12(12)-->|\"#0045;\"|s_3(3)", "s_15(15)-->|\"#0045;\"|s_2(2)", "s_2(2)-->|\"#0099;\"|s_8(8)", "s_2(2)-->|\"#0098;\"|s_7((7))", "s_2(2)-->|\"#0097;\"|s_5(5)", "s_2(2)-->|\"#0065;\"|s_6(6)", "s_2(2)-->|\"#0045;\"|s_3(3)", "s_15(15)-->|\"#0098;\"|s_11((11))", "s_11((11))-->|\"#0099;\"|s_8(8)", "s_11((11))-->|\"#0098;\"|s_7((7))", "s_11((11))-->|\"#0097;\"|s_5(5)", "s_11((11))-->|\"#0065;\"|s_6(6)", "s_11((11))-->|\"#0045;\"|s_3(3)", "s_0(0)-->|\"#0097;\"|s_13(13)", "s_13(13)-->|\"#0097;\"|s_9(9)", "s_13(13)-->|\"#0065;\"|s_10(10)", "s_13(13)-->|\"#0099;\"|s_12(12)", "s_13(13)-->|\"#0045;\"|s_2(2)", "s_13(13)-->|\"#0098;\"|s_11((11))", "s_0(0)-->|\"#0065;\"|s_14(14)", "s_14(14)-->|\"#0097;\"|s_9(9)", "s_14(14)-->|\"#0065;\"|s_10(10)", "s_14(14)-->|\"#0099;\"|s_12(12)", "s_14(14)-->|\"#0045;\"|s_2(2)", "s_14(14)-->|\"#0098;\"|s_11((11))", "s_0(0)-->|\"#0045;\"|s_1(1)", "s_1(1)-->|\"#0097;\"|s_9(9)", "s_1(1)-->|\"#0065;\"|s_10(10)", "s_1(1)-->|\"#0099;\"|s_12(12)", "s_1(1)-->|\"#0045;\"|s_2(2)", "s_1(1)-->|\"#0098;\"|s_11((11))", "s_0(0)-->|\"#0099;\"|s_16(16)", "s_16(16)-->|\"#0097;\"|s_9(9)", "s_16(16)-->|\"#0065;\"|s_10(10)", "s_16(16)-->|\"#0099;\"|s_12(12)", "s_16(16)-->|\"#0045;\"|s_2(2)", "s_16(16)-->|\"#0098;\"|s_11((11))")
                , containsInAnyOrder(chart));
    }
}