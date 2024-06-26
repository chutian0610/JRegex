package io.github.chutian0610.jregex.automata.nfa;

import com.google.common.collect.Lists;
import io.github.chutian0610.jregex.util.RegexTestContext;
import io.github.chutian0610.jregex.ast.RegexExp;
import io.github.chutian0610.jregex.ast.RegexParser;
import io.github.chutian0610.jregex.automata.dfa.DFAGraph;
import io.github.chutian0610.jregex.automata.state.GenericStateManager;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */
@DisplayName("NFA to DFA测试")
@Slf4j
class NFAGraphToDFATest
{

    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    @DisplayName("测试-Concat")
    void toDFA01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))"),
                RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-Or")
    void toDFA02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0097;\"|s_1((1))"),
                RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-Repeat *")
    void toDFA03()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))", "s_1(1)-->|\"#0097;\"|s_1(1)"),
                RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-复合表达式")
    void toDFA04()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2(2)", "s_2(2)-->|\"#0098;\"|s_3((3))", "s_3((3))-->|\"#0098;\"|s_4(4)", "s_3((3))-->|\"#0097;\"|s_1(1)", "s_2(2)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0097;\"|s_1(1)", "s_0(0)-->|\"#0097;\"|s_1(1)"),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-字符类")
    void toDFA05()
    {
        RegexExp regexExpression = RegexParser.parse("[a-zA-Z]b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0097; - #0122;\"|s_3(3)", "s_3(3)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0065; - #0090;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))")
                , RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-元字符")
    void toDFA06()
    {
        RegexExp regexExpression = RegexParser.parse("\\d+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(dfa.printStateMapping());
        List<String> chart = dfa.toMermaidJsChartLines();
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0048; - #0057;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))", "s_1(1)-->|\"#0048; - #0057;\"|s_1(1)")
                , RegexTestContext.containsInAnyOrder(chart));
    }
}