package info.victorchu.jregex.automata;

import com.google.common.collect.Lists;
import info.victorchu.jregex.RegexContext;
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

import static info.victorchu.jregex.util.TestUtil.chart2ExpectString;
import static info.victorchu.jregex.util.TestUtil.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */
class NFAGraphTest
{
    private static final Logger log = LoggerFactory.getLogger(NFAGraphTest.class);

    private static final RegexContext regexContext = new RegexContext(new GenericStateManager());

    @BeforeEach
    void init()
    {
        regexContext.reset();
    }

    @Test
    void toDFA01()
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext);
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(regexContext.printDFA2NFAMapping(dfa));
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'a'|s_1(1)",
                        "s_1(1)-->|'b'|s_2((2))"),
                containsInAnyOrder(chart));
    }

    @Test
    void toDFA02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext);
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(regexContext.printDFA2NFAMapping(dfa));
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'b'|s_2((2))",
                        "s_0(0)-->|'a'|s_1((1))"),
                containsInAnyOrder(chart));
    }

    @Test
    void toDFA03()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext);
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(regexContext.printDFA2NFAMapping(dfa));
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'b'|s_2((2))",
                        "s_0(0)-->|'a'|s_1(1)",
                        "s_1(1)-->|'b'|s_2((2))",
                        "s_1(1)-->|'a'|s_1(1)"),
                containsInAnyOrder(chart));
    }

    @Test
    void toDFA04()
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        NFAGraph nfa = NFAGraph.build(regexExpression, regexContext);
        log.debug("\n================== NFA ================\n{}======================================", nfa.toMermaidJsChart());
        DFAGraph dfa = nfa.toDFA();
        log.debug("\n================== DFA ================\n{}======================================", dfa.toMermaidJsChart());
        log.debug(regexContext.printDFA2NFAMapping(dfa));
        List<String> chart = dfa.toMermaidJsChartLines();
        assertThat(
                Lists.newArrayList("flowchart LR",
                        "s_0(0)-->|'b'|s_4(4)",
                        "s_4(4)-->|'b'|s_4(4)",
                        "s_4(4)-->|'a'|s_1(1)",
                        "s_1(1)-->|'b'|s_2(2)",
                        "s_2(2)-->|'b'|s_3((3))",
                        "s_3((3))-->|'b'|s_4(4)",
                        "s_3((3))-->|'a'|s_1(1)",
                        "s_2(2)-->|'a'|s_1(1)",
                        "s_1(1)-->|'a'|s_1(1)",
                        "s_0(0)-->|'a'|s_1(1)"),
                containsInAnyOrder(chart));
    }
}