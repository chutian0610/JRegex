package info.victorchu.jregex.automata;

import com.google.common.collect.Lists;
import info.victorchu.jregex.util.RegexTestContext;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.dfa.DFAGraph;
import info.victorchu.jregex.automata.nfa.NFAGraph;
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
public class DFAGraphTest
{
    private static final Logger log = LoggerFactory.getLogger(DFAGraphTest.class);
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
        log.debug(regexContext.printDFA2DFAMapping(minDfa));
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'a'|s_1(1)",
                        "s_1(1)-->|'b'|s_2((2))"),
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
        log.debug(regexContext.printDFA2DFAMapping(minDfa));
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'b'|s_1((1))",
                        "s_0(0)-->|'a'|s_1((1))"),
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
        log.debug(regexContext.printDFA2DFAMapping(minDfa));
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'b'|s_2((2))",
                        "s_0(0)-->|'a'|s_0(0)"),
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
        log.debug(regexContext.printDFA2DFAMapping(minDfa));
        List<String> chart = minDfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'b'|s_0(0)",
                        "s_0(0)-->|'a'|s_1(1)",
                        "s_1(1)-->|'b'|s_2(2)",
                        "s_2(2)-->|'b'|s_3((3))",
                        "s_3((3))-->|'b'|s_0(0)",
                        "s_3((3))-->|'a'|s_1(1)",
                        "s_2(2)-->|'a'|s_1(1)",
                        "s_1(1)-->|'a'|s_1(1)"),
                containsInAnyOrder(chart));
    }
}
