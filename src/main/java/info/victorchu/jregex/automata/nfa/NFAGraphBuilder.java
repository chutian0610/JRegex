package info.victorchu.jregex.automata.nfa;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.ast.CharExp;
import info.victorchu.jregex.ast.ConcatExp;
import info.victorchu.jregex.ast.OrExp;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexExpVisitor;
import info.victorchu.jregex.ast.RepeatExp;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.SubGraph;

import java.util.function.BiFunction;

/**
 * @author victorchu
 */
public class NFAGraphBuilder
        implements RegexExpVisitor<SubGraph, RegexContext>, BiFunction<RegexExp, RegexContext, NFAGraph>
{
    public static final NFAGraphBuilder INSTANCE = new NFAGraphBuilder();

    @Override
    public SubGraph visitChar(CharExp node, RegexContext context)
    {
        State start = context.createNFAState();
        State charState = context.createNFAState();
        start.addTransition(Edge.character(node.getCharacter()), charState);
        return SubGraph.of(Edge.epsilon(), start, charState);
    }

    @Override
    public SubGraph visitConcat(ConcatExp node, RegexContext context)
    {
        SubGraph subNFALeft = process(node.getLeft(), context);
        SubGraph subNFARight = process(node.getRight(), context);
        subNFALeft.getEnd().addTransition(subNFARight.getInEdge(), subNFARight.getStart());
        return SubGraph.of(Edge.epsilon(), subNFALeft.getStart(), subNFARight.getEnd());
    }

    @Override
    public SubGraph visitOr(OrExp node, RegexContext context)
    {
        State begin = context.createNFAState();
        SubGraph subNFALeft = process(node.getLeft(), context);
        begin.addTransition(subNFALeft.getInEdge(), subNFALeft.getStart());
        SubGraph subNFARight = process(node.getRight(), context);
        begin.addTransition(subNFARight.getInEdge(), subNFARight.getStart());
        State end = context.createNFAState();
        subNFALeft.getEnd().addTransition(Edge.epsilon(), end);
        subNFARight.getEnd().addTransition(Edge.epsilon(), end);
        return SubGraph.of(Edge.epsilon(), begin, end);
    }

    @Override
    public SubGraph visitRepeat(RepeatExp node, RegexContext context)
    {
        State begin = context.createNFAState();
        SubGraph subNFA = process(node.getInner(), context);
        State end = context.createNFAState();

        // 一次
        begin.addTransition(subNFA.getInEdge(), subNFA.getStart());
        subNFA.getEnd().addTransition(Edge.epsilon(), end);
        // 多次
        subNFA.getEnd().addTransition(Edge.epsilon(), subNFA.getStart());

        // 0次
        begin.addTransition(Edge.epsilon(), end);
        return SubGraph.of(Edge.epsilon(), begin, end);
    }

    @Override
    public NFAGraph apply(RegexExp regexExp, RegexContext context)
    {
        SubGraph subGraph = process(regexExp, context);
        subGraph.getEnd().setAccept(true);
        return NFAGraph.of(subGraph.getStart(), context.getStateManager());
    }
}
