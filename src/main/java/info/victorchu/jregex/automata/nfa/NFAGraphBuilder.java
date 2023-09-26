package info.victorchu.jregex.automata.nfa;

import info.victorchu.jregex.ast.CharExp;
import info.victorchu.jregex.ast.ConcatExp;
import info.victorchu.jregex.ast.NodeType;
import info.victorchu.jregex.ast.OrExp;
import info.victorchu.jregex.ast.RegexExp;
import info.victorchu.jregex.ast.RegexExpVisitor;
import info.victorchu.jregex.ast.RepeatExp;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.SubGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Thompson algorithm
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
        if (0 ==node.getMin() && node.getMax() !=null && 0== node.getMax()) {
            // 特殊情况
            State end = context.createNFAState();
            // 0次
            begin.addTransition(Edge.epsilon(), end);
            return SubGraph.of(Edge.epsilon(), begin, end);
        }

        if(node.getMax() ==null){
            int min = node.getMin();
            SubGraph middle= null;
            int length = (min>0?min:1);
            for (int i = 0; i < length; i++) {
                SubGraph subNFA = process(node.getInner(), context);
                if(middle ==null){
                    middle = subNFA;
                }else {
                    middle.getEnd().addTransition(subNFA.getInEdge(), subNFA.getStart());
                    middle = SubGraph.of(middle.getInEdge(), middle.getStart(), subNFA.getEnd());
                }
                if(i == length-1){
                    subNFA.getEnd().addTransition(Edge.epsilon(), subNFA.getStart());
                }
            }
            State end = context.createNFAState();
            if (node.getMin() == 0) {
                // 0次
                begin.addTransition(Edge.epsilon(), end);
            }
            if(middle !=null) {
                begin.addTransition(middle.getInEdge(), middle.getStart());
                middle.getEnd().addTransition(Edge.epsilon(), end);
            }
            return SubGraph.of(Edge.epsilon(), begin, end);
        }else {
            int max = node.getMax();
            int min = node.getMin();
            List<State> fastJump = new ArrayList<>();
            SubGraph middle= null;
            for (int i = 0; i < max; i++) {
                SubGraph subNFA = process(node.getInner(), context);
                if(middle ==null){
                    middle = subNFA;
                }else {
                    middle.getEnd().addTransition(subNFA.getInEdge(), subNFA.getStart());
                    middle = SubGraph.of(middle.getInEdge(), middle.getStart(), subNFA.getEnd());
                }
                if (i >= min - 1 && i != max - 1) {
                    fastJump.add(subNFA.getEnd());
                }
            }
            State end = context.createNFAState();
            if (node.getMin() == 0) {
                // 0次
                begin.addTransition(Edge.epsilon(), end);
            }
            if (!fastJump.isEmpty()) {
                fastJump.forEach(x -> x.addTransition(Edge.epsilon(), end));
            }
            if(middle!=null) {
                begin.addTransition(middle.getInEdge(), middle.getStart());
                middle.getEnd().addTransition(Edge.epsilon(), end);
            }
            return SubGraph.of(Edge.epsilon(), begin, end);
        }
    }

    @Override
    public NFAGraph apply(RegexExp regexExp, StateManager stateManager)
    {
        SubGraph subGraph = process(regexExp, stateManager);
        subGraph.getEnd().setAccept(true);
        return NFAGraph.of(subGraph.getStart(), stateManager);
    }
}
