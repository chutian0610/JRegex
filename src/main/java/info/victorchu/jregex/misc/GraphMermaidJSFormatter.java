package info.victorchu.jregex.misc;

import info.victorchu.jregex.automata.AbstractGraphConsolePrinter;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.edge.CharacterEdge;
import info.victorchu.jregex.automata.edge.CharacterRangeEdge;
import info.victorchu.jregex.automata.edge.EpsilonEdge;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author victorchu
 */
public class GraphMermaidJSFormatter
        extends AbstractGraphConsolePrinter
{

    @Override
    protected void traverse(State cursor, List<String> sb)
    {
        sb.add("flowchart LR");
        Set<Integer> markSet = new HashSet<>();
        traverse(cursor, sb, markSet);
    }

    public static final GraphMermaidJSFormatter INSTANCE = new GraphMermaidJSFormatter();

    protected String edge2Str(@NonNull Edge edge)
    {
        if(edge instanceof EpsilonEdge){
            return edge.toString();
        }
        if(edge instanceof CharacterEdge){
            CharacterEdge characterEdge= (CharacterEdge) edge;
            // html format
            return String.format("\"#%04d;\"",(int)characterEdge.getCharacter());
        }
        if (edge instanceof CharacterRangeEdge) {
            CharacterRangeEdge characterRangeEdge = (CharacterRangeEdge) edge;
            // html format
            return String.format("\"#%04d; - #%04d;\"", (int) characterRangeEdge.getFrom(), (int) characterRangeEdge.getTo());
        }
        return edge.toString();
    }

    protected String state2Str(@NonNull State state)
    {
        return state.isAccept() ?
                String.format("s_%d((%d))", state.getStateId(), state.getStateId()) :
                String.format("s_%d(%d)", state.getStateId(), state.getStateId());
    }
}
