package info.victorchu.jregex.automata;

import com.google.common.collect.Lists;
import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.state.GenericStateManager;
import info.victorchu.jregex.util.RegexExpTreeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static info.victorchu.jregex.util.TestUtil.chart2ExpectString;
import static info.victorchu.jregex.util.TestUtil.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */
@Slf4j
class NFAGraphBuilderTest {

    private static final RegexContext regexContext = new RegexContext(new GenericStateManager());

    @BeforeEach
    void init(){
        regexContext.reset();
    }
    @Test
    void buildNFA01()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("ab");
        String tree = RegexExpTreeFormatter.print(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression,regexContext);
        List<String> chart = nfaGraph.toMermaidJsChart();
        log.debug("\n================== NFA ================\n{}======================================", String.join("\n",chart)+"\n");
//        log.debug(chart2ExpectString(chart));
        assertThat(
                Lists.newArrayList("flowchart LR","s_0(0)-->|'a'|s_1(1)","s_1(1)-->|ϵ|s_2(2)","s_2(2)-->|'b'|s_3((3))")
                ,containsInAnyOrder(chart));
    }

    @Test
    void buildNFA02()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        String tree = RegexExpTreeFormatter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression,regexContext);
        List<String> chart = nfaGraph.toMermaidJsChart();
        log.debug("\n================== NFA ================\n{}======================================", String.join("\n",chart)+"\n");
//        log.debug(chart2ExpectString(chart));
        assertThat(
                Lists.newArrayList("flowchart LR","s_0(0)-->|ϵ|s_1(1)","s_1(1)-->|'a'|s_2(2)","s_2(2)-->|ϵ|s_5((5))","s_0(0)-->|ϵ|s_3(3)","s_3(3)-->|'b'|s_4(4)","s_4(4)-->|ϵ|s_5((5))")
                ,containsInAnyOrder(chart));
    }

    @Test
    void buildNFA03()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        String tree = RegexExpTreeFormatter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression,regexContext);
        List<String> chart = nfaGraph.toMermaidJsChart();
        log.debug("\n================== NFA ================\n{}======================================", String.join("\n",chart)+"\n");
//        log.debug(chart2ExpectString(chart));
        assertThat(
                Lists.newArrayList("flowchart LR","s_0(0)-->|ϵ|s_1(1)","s_1(1)-->|'a'|s_2(2)","s_2(2)-->|ϵ|s_1(1)","s_2(2)-->|ϵ|s_3(3)","s_3(3)-->|ϵ|s_4(4)","s_4(4)-->|'b'|s_5((5))","s_0(0)-->|ϵ|s_3(3)")
                ,containsInAnyOrder(chart));
    }

    @Test
    void buildNFA04()
            throws IOException
    {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        String tree = RegexExpTreeFormatter.print(regexExpression);
        log.info("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression,regexContext);
        List<String> chart = nfaGraph.toMermaidJsChart();
        log.debug("\n================== NFA ================\n{}======================================", String.join("\n",chart)+"\n");
//        log.debug(chart2ExpectString(chart));
        assertThat(
                Lists.newArrayList("flowchart LR","s_0(0)-->|ϵ|s_7(7)","s_7(7)-->|ϵ|s_8(8)","s_8(8)-->|'a'|s_9(9)","s_9(9)-->|ϵ|s_10(10)","s_10(10)-->|'b'|s_11(11)","s_11(11)-->|ϵ|s_12(12)","s_12(12)-->|'b'|s_13((13))","s_0(0)-->|ϵ|s_1(1)","s_1(1)-->|ϵ|s_4(4)","s_4(4)-->|'b'|s_5(5)","s_5(5)-->|ϵ|s_6(6)","s_6(6)-->|ϵ|s_7(7)","s_6(6)-->|ϵ|s_1(1)","s_1(1)-->|ϵ|s_2(2)","s_2(2)-->|'a'|s_3(3)","s_3(3)-->|ϵ|s_6(6)")
                ,containsInAnyOrder(chart));
    }
}