package info.victorchu.jregex.automata.dfa;

import com.google.common.collect.Lists;
import info.victorchu.jregex.util.RegexTestContext;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.nfa.NFAGraph;
import info.victorchu.jregex.automata.state.GenericStateManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static info.victorchu.jregex.util.RegexTestContext.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */

@DisplayName("DFA最小化测试")
@Slf4j
public class DFAGraphSimplifyTest
{
    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    void minimizationDFA01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        log.debug("\n==================  mini DFA ================\n{}======================================", minDfa.toMermaidJsChart());
        log.debug(minDfa.printStateMapping());
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))"
                ),
                containsInAnyOrder(chart));
    }

    @Test
    void minimizationDFA02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        log.debug("\n==================  mini DFA ================\n{}======================================", minDfa.toMermaidJsChart());
        log.debug(minDfa.printStateMapping());
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_1((1))", "s_0(0)-->|\"#0097;\"|s_1((1))"
                ),
                containsInAnyOrder(chart));
    }

    @Test
    void minimizationDFA03()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        log.debug("\n==================  mini DFA ================\n{}======================================", minDfa.toMermaidJsChart());
        log.debug(minDfa.printStateMapping());
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0097;\"|s_0(0)"
                ),
                containsInAnyOrder(chart));
    }

    @Test
    void minimizationDFA04()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        log.debug("\n==================  mini DFA ================\n{}======================================", minDfa.toMermaidJsChart());
        log.debug(minDfa.printStateMapping());
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0098;\"|s_0(0)", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2(2)", "s_2(2)-->|\"#0098;\"|s_3((3))", "s_3((3))-->|\"#0098;\"|s_0(0)", "s_3((3))-->|\"#0097;\"|s_1(1)", "s_2(2)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|\"#0097;\"|s_1(1)"
                ),
                containsInAnyOrder(chart));
    }

    @Test
    void minimizationDFA05()
    {
        RegexExp regexExpression = RegexParser.parse("[a-zA-Z]b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        log.debug("\n================== mini DFA ================\n{}======================================", minDfa.toMermaidJsChart());
        log.debug(minDfa.printStateMapping());
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0097; - #0122;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))", "s_0(0)-->|\"#0065; - #0090;\"|s_1(1)")
                , containsInAnyOrder(chart));
    }

    @Test
    void minimizationDFA06()
    {
        RegexExp regexExpression = RegexParser.parse("\\d+b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext.getStateManager());
        DFAGraph dfa = nfa.toDFA();
        DFAGraph minDfa = dfa.simplify();
        log.debug("\n================== mini DFA ================\n{}======================================", minDfa.toMermaidJsChart());
        log.debug(minDfa.printStateMapping());
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0048; - #0057;\"|s_1(1)", "s_1(1)-->|\"#0098;\"|s_2((2))", "s_1(1)-->|\"#0048; - #0057;\"|s_1(1)")
                , containsInAnyOrder(chart));
    }
}
