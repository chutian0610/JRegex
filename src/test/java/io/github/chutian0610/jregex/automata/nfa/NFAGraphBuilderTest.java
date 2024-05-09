package io.github.chutian0610.jregex.automata.nfa;

import com.google.common.collect.Lists;
import io.github.chutian0610.jregex.ast.RegexExp;
import io.github.chutian0610.jregex.ast.RegexParser;
import io.github.chutian0610.jregex.automata.state.GenericStateManager;
import io.github.chutian0610.jregex.misc.RegexExpTreeFormatter;
import io.github.chutian0610.jregex.util.RegexTestContext;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */
@Slf4j
@DisplayName("NFA构建测试")
class NFAGraphBuilderTest {

    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init() {
        regexContext.reset();
    }

    @Test
    @DisplayName("测试-Concat")
    void buildNFA01() throws IOException {
        RegexExp regexExpression = RegexParser.parse("ab");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|ϵ|s_2(2)", "s_2(2)-->|\"#0098;\"|s_3((3))"),
                RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-Or")
    void buildNFA02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|ϵ|s_5((5))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_5((5))"),
                RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-Repeat *")
    void buildNFA03()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5((5))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_2(2)-->|ϵ|s_1(1)"),
                RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-复合表达式")
    void buildNFA04() throws IOException {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_7(7)", "s_7(7)-->|ϵ|s_8(8)", "s_8(8)-->|\"#0097;\"|s_9(9)", "s_9(9)-->|ϵ|s_10(10)", "s_10(10)-->|\"#0098;\"|s_11(11)", "s_11(11)-->|ϵ|s_12(12)", "s_12(12)-->|\"#0098;\"|s_13((13))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|ϵ|s_2(2)", "s_2(2)-->|\"#0097;\"|s_3(3)", "s_3(3)-->|ϵ|s_6(6)", "s_6(6)-->|ϵ|s_7(7)", "s_6(6)-->|ϵ|s_1(1)", "s_1(1)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5(5)", "s_5(5)-->|ϵ|s_6(6)"),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-repeat +")
    void buildNFA05()
    {
        RegexExp regexExpression = RegexParser.parse("a+b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5((5))", "s_2(2)-->|ϵ|s_1(1)"),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-repeat ?")
    void buildNFA06()
    {
        RegexExp regexExpression = RegexParser.parse("a?b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5((5))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)"),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-repeat {n,m}")
    void buildNFA07()
    {
        RegexExp regexExpression = RegexParser.parse("a{1,2}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))", "s_2(2)-->|ϵ|s_5(5)"),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-repeat {n}")
    void buildNFA08()
    {
        RegexExp regexExpression = RegexParser.parse("a{2}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))"),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-repeat {n,}")
    void buildNFA09()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_3(3)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))"
                ),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-repeat {0,m}")
    void buildNFA10()
    {
        RegexExp regexExpression = RegexParser.parse("a{0,2}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_2(2)-->|ϵ|s_5(5)"
                        ),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-字符类")
    void buildNFA11()
    {
        RegexExp regexExpression = RegexParser.parse("[a-cA-]{1,3}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097; - #0099;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_16(16)", "s_16(16)-->|ϵ|s_17(17)", "s_17(17)-->|\"#0098;\"|s_18((18))", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0065;\"|s_8(8)", "s_8(8)-->|ϵ|s_10(10)", "s_10(10)-->|ϵ|s_16(16)", "s_10(10)-->|ϵ|s_11(11)", "s_11(11)-->|\"#0065;\"|s_13(13)", "s_13(13)-->|ϵ|s_15(15)", "s_15(15)-->|ϵ|s_16(16)", "s_11(11)-->|\"#0097; - #0099;\"|s_14(14)", "s_14(14)-->|ϵ|s_15(15)", "s_11(11)-->|\"#0045;\"|s_12(12)", "s_12(12)-->|ϵ|s_15(15)", "s_6(6)-->|\"#0045;\"|s_7(7)", "s_7(7)-->|ϵ|s_10(10)", "s_6(6)-->|\"#0097; - #0099;\"|s_9(9)", "s_9(9)-->|ϵ|s_10(10)", "s_1(1)-->|\"#0065;\"|s_3(3)", "s_3(3)-->|ϵ|s_5(5)", "s_1(1)-->|\"#0045;\"|s_2(2)", "s_2(2)-->|ϵ|s_5(5)"
                ),
                RegexTestContext.containsInAnyOrder(chart));
    }
    @Test
    @DisplayName("测试-字符类 ^")
    void buildNFA12()
    {
        RegexExp regexExpression = RegexParser.parse("[^a-zA-Z]");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|\"#0091; - #0096;\"|s_2(2)", "s_2(2)-->|ϵ|s_4((4))", "s_0(0)-->|\"#0000; - #0064;\"|s_1(1)", "s_1(1)-->|ϵ|s_4((4))", "s_0(0)-->|\"#0123; - #65535;\"|s_3(3)", "s_3(3)-->|ϵ|s_4((4))"
                )
                        , RegexTestContext.containsInAnyOrder(chart));
    }

    @Test
    @DisplayName("测试-元字符")
    void buildNFA13()
    {
        RegexExp regexExpression = RegexParser.parse("\\d+b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        MatcherAssert.assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0048; - #0057;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_1(1)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|\"#0098;\"|s_6((6))"
                ),
                RegexTestContext.containsInAnyOrder(chart));
    }
}