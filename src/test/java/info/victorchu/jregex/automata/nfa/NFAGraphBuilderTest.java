package info.victorchu.jregex.automata.nfa;

import com.google.common.collect.Lists;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexParser;
import info.victorchu.jregex.automata.state.GenericStateManager;
import info.victorchu.jregex.misc.RegexExpTreeFormatter;
import info.victorchu.jregex.misc.RegexTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static info.victorchu.jregex.misc.RegexTestContext.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author victorchu
 */
@Slf4j
class NFAGraphBuilderTest {

    private static final RegexTestContext regexContext = new RegexTestContext(new GenericStateManager());

    @BeforeEach
    void init() {
        regexContext.reset();
    }

    @Test
    void buildNFA01() throws IOException {
        RegexExp regexExpression = RegexParser.parse("ab");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|\"#0097;\"|s_1(1)", "s_1(1)-->|ϵ|s_2(2)", "s_2(2)-->|\"#0098;\"|s_3((3))"),
                containsInAnyOrder(chart));
    }

    @Test
    void buildNFA02()
    {
        RegexExp regexExpression = RegexParser.parse("a|b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|ϵ|s_5((5))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_5((5))"),
                containsInAnyOrder(chart));
    }

    @Test
    void buildNFA03()
    {
        RegexExp regexExpression = RegexParser.parse("a*b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5((5))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_2(2)-->|ϵ|s_1(1)"),
                containsInAnyOrder(chart));
    }

    @Test
    void buildNFA04() throws IOException {
        RegexExp regexExpression = RegexParser.parse("(a|b)*abb");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_7(7)", "s_7(7)-->|ϵ|s_8(8)", "s_8(8)-->|\"#0097;\"|s_9(9)", "s_9(9)-->|ϵ|s_10(10)", "s_10(10)-->|\"#0098;\"|s_11(11)", "s_11(11)-->|ϵ|s_12(12)", "s_12(12)-->|\"#0098;\"|s_13((13))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|ϵ|s_2(2)", "s_2(2)-->|\"#0097;\"|s_3(3)", "s_3(3)-->|ϵ|s_6(6)", "s_6(6)-->|ϵ|s_7(7)", "s_6(6)-->|ϵ|s_1(1)", "s_1(1)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5(5)", "s_5(5)-->|ϵ|s_6(6)"),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA05()
    {
        RegexExp regexExpression = RegexParser.parse("a+b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5((5))", "s_2(2)-->|ϵ|s_1(1)"),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA06()
    {
        RegexExp regexExpression = RegexParser.parse("a?b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_3(3)", "s_3(3)-->|ϵ|s_4(4)", "s_4(4)-->|\"#0098;\"|s_5((5))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)"),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA07()
    {
        RegexExp regexExpression = RegexParser.parse("a{1,2}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))", "s_2(2)-->|ϵ|s_5(5)"),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA08()
    {
        RegexExp regexExpression = RegexParser.parse("a{2}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList("flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))"),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA09()
    {
        RegexExp regexExpression = RegexParser.parse("a{2,}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_3(3)", "s_4(4)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))"
                ),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA10()
    {
        RegexExp regexExpression = RegexParser.parse("a{0,2}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_5(5)", "s_5(5)-->|ϵ|s_6(6)", "s_6(6)-->|\"#0098;\"|s_7((7))", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_3(3)", "s_3(3)-->|\"#0097;\"|s_4(4)", "s_4(4)-->|ϵ|s_5(5)", "s_2(2)-->|ϵ|s_5(5)"
                        ),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA11()
    {
        RegexExp regexExpression = RegexParser.parse("[a-cA-]{1,3}b");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|ϵ|s_1(1)", "s_1(1)-->|\"#0045;\"|s_6(6)", "s_6(6)-->|ϵ|s_7(7)", "s_7(7)-->|ϵ|s_22(22)", "s_22(22)-->|ϵ|s_23(23)", "s_23(23)-->|\"#0098;\"|s_24((24))", "s_7(7)-->|ϵ|s_8(8)", "s_8(8)-->|\"#0097;\"|s_9(9)", "s_9(9)-->|ϵ|s_14(14)", "s_14(14)-->|ϵ|s_22(22)", "s_14(14)-->|ϵ|s_15(15)", "s_15(15)-->|\"#0045;\"|s_20(20)", "s_20(20)-->|ϵ|s_21(21)", "s_21(21)-->|ϵ|s_22(22)", "s_15(15)-->|\"#0099;\"|s_19(19)", "s_19(19)-->|ϵ|s_21(21)", "s_15(15)-->|\"#0098;\"|s_18(18)", "s_18(18)-->|ϵ|s_21(21)", "s_15(15)-->|\"#0097;\"|s_16(16)", "s_16(16)-->|ϵ|s_21(21)", "s_15(15)-->|\"#0065;\"|s_17(17)", "s_17(17)-->|ϵ|s_21(21)", "s_8(8)-->|\"#0065;\"|s_10(10)", "s_10(10)-->|ϵ|s_14(14)", "s_8(8)-->|\"#0045;\"|s_13(13)", "s_13(13)-->|ϵ|s_14(14)", "s_8(8)-->|\"#0099;\"|s_12(12)", "s_12(12)-->|ϵ|s_14(14)", "s_8(8)-->|\"#0098;\"|s_11(11)", "s_11(11)-->|ϵ|s_14(14)", "s_1(1)-->|\"#0099;\"|s_5(5)", "s_5(5)-->|ϵ|s_7(7)", "s_1(1)-->|\"#0098;\"|s_4(4)", "s_4(4)-->|ϵ|s_7(7)", "s_1(1)-->|\"#0097;\"|s_2(2)", "s_2(2)-->|ϵ|s_7(7)", "s_1(1)-->|\"#0065;\"|s_3(3)", "s_3(3)-->|ϵ|s_7(7)"
                ),
                containsInAnyOrder(chart));
    }
    @Test
    void buildNFA12()
    {
        RegexExp regexExpression = RegexParser.parse("[^a-zA-Z0-9_-]");
        String tree = RegexExpTreeFormatter.format(regexExpression);
        log.debug("\n================== tree ================\n{}=====================================", tree);
        NFAGraph nfaGraph = NFAGraphBuilder.INSTANCE.apply(regexExpression, regexContext.getStateManager());
        List<String> chart = nfaGraph.toMermaidJsChartLines();
        log.debug("\n================== NFA ================\n{}======================================", nfaGraph.toMermaidJsChart());
        assertThat(
                Lists.newArrayList(
                        "flowchart LR", "s_0(0)-->|\"#0011;\"|s_12(12)", "s_12(12)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0043;\"|s_44(44)", "s_44(44)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0123;\"|s_60(60)", "s_60(60)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0026;\"|s_27(27)", "s_27(27)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0009;\"|s_10(10)", "s_10(10)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0041;\"|s_42(42)", "s_42(42)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0024;\"|s_25(25)", "s_25(25)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0007;\"|s_8(8)", "s_8(8)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0039;\"|s_40(40)", "s_40(40)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0093;\"|s_57(57)", "s_57(57)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0022;\"|s_23(23)", "s_23(23)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0005;\"|s_6(6)", "s_6(6)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0037;\"|s_38(38)", "s_38(38)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0091;\"|s_55(55)", "s_55(55)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0020;\"|s_21(21)", "s_21(21)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0003;\"|s_4(4)", "s_4(4)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0035;\"|s_36(36)", "s_36(36)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0018;\"|s_19(19)", "s_19(19)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0063;\"|s_53(53)", "s_53(53)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0001;\"|s_2(2)", "s_2(2)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0033;\"|s_34(34)", "s_34(34)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0016;\"|s_17(17)", "s_17(17)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0061;\"|s_51(51)", "s_51(51)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0031;\"|s_32(32)", "s_32(32)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0046;\"|s_46(46)", "s_46(46)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0014;\"|s_15(15)", "s_15(15)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0126;\"|s_63(63)", "s_63(63)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0059;\"|s_49(49)", "s_49(49)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0029;\"|s_30(30)", "s_30(30)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0012;\"|s_13(13)", "s_13(13)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0044;\"|s_45(45)", "s_45(45)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0124;\"|s_61(61)", "s_61(61)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0027;\"|s_28(28)", "s_28(28)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0010;\"|s_11(11)", "s_11(11)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0042;\"|s_43(43)", "s_43(43)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0096;\"|s_59(59)", "s_59(59)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0025;\"|s_26(26)", "s_26(26)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0008;\"|s_9(9)", "s_9(9)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0040;\"|s_41(41)", "s_41(41)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0094;\"|s_58(58)", "s_58(58)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0023;\"|s_24(24)", "s_24(24)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0006;\"|s_7(7)", "s_7(7)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0038;\"|s_39(39)", "s_39(39)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0092;\"|s_56(56)", "s_56(56)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0021;\"|s_22(22)", "s_22(22)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0004;\"|s_5(5)", "s_5(5)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0036;\"|s_37(37)", "s_37(37)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0019;\"|s_20(20)", "s_20(20)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0064;\"|s_54(54)", "s_54(54)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0002;\"|s_3(3)", "s_3(3)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0034;\"|s_35(35)", "s_35(35)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0017;\"|s_18(18)", "s_18(18)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0062;\"|s_52(52)", "s_52(52)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0000;\"|s_1(1)", "s_1(1)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0032;\"|s_33(33)", "s_33(33)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0047;\"|s_47(47)", "s_47(47)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0015;\"|s_16(16)", "s_16(16)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0127;\"|s_64(64)", "s_64(64)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0060;\"|s_50(50)", "s_50(50)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0030;\"|s_31(31)", "s_31(31)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0013;\"|s_14(14)", "s_14(14)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0125;\"|s_62(62)", "s_62(62)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0058;\"|s_48(48)", "s_48(48)-->|ϵ|s_65((65))", "s_0(0)-->|\"#0028;\"|s_29(29)", "s_29(29)-->|ϵ|s_65((65))"
                )
                        ,containsInAnyOrder(chart));
    }
}