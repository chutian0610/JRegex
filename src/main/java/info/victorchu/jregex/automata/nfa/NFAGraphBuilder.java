package info.victorchu.jregex.automata.nfa;

import info.victorchu.jregex.ast.CharExp;
import info.victorchu.jregex.ast.ConcatExp;
import info.victorchu.jregex.ast.OrExp;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexExpVisitor;
import info.victorchu.jregex.ast.RepeatExp;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.SubGraph;

import java.util.function.BiFunction;

/**
 * @author victorchu
 */
public class NFAGraphBuilder
        implements RegexExpVisitor<SubGraph, StateManager>, BiFunction<RegexExp, StateManager, NFAGraph>
{
    public static final NFAGraphBuilder INSTANCE = new NFAGraphBuilder();

    @Override
    public SubGraph visitChar(CharExp node, StateManager context)
    {
        State start = context.createNFAState();
        State charState = context.createNFAState();
        start.addTransition(Edge.character(node.getCharacter()), charState);
        return SubGraph.of(Edge.epsilon(), start, charState);
    }

    @Override
    public SubGraph visitConcat(ConcatExp node, StateManager context)
    {
        SubGraph subNFALeft = process(node.getLeft(), context);
        SubGraph subNFARight = process(node.getRight(), context);
        subNFALeft.getEnd().addTransition(subNFARight.getInEdge(), subNFARight.getStart());
        return SubGraph.of(Edge.epsilon(), subNFALeft.getStart(), subNFARight.getEnd());
    }

    @Override
    public SubGraph visitOr(OrExp node, StateManager context)
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
    public SubGraph visitRepeat(RepeatExp node, StateManager context)
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
    public NFAGraph apply(RegexExp regexExp, StateManager stateManager)
    {
        SubGraph subGraph = process(regexExp, stateManager);
        subGraph.getEnd().setAccept(true);
        return NFAGraph.of(subGraph.getStart(), stateManager);
    }
}
