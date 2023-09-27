package info.victorchu.jregex.util;

import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.Transition;
import info.victorchu.jregex.automata.edge.CharacterEdge;
import info.victorchu.jregex.automata.edge.EpsilonEdge;
import info.victorchu.jregex.automata.State;
import lombok.NonNull;
import java.util.List;
import java.util.Set;

/**
 * @author victorchu
 */
public class GraphMermaidJSFormatter
{

    public static final GraphMermaidJSFormatter INSTANCE = new GraphMermaidJSFormatter();

    public void handleState(State cursor, List<String> sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getStateId())) {
            markSet.add(cursor.getStateId());
            Set<Transition> transitions = cursor.getTransitions();
            for (Transition transition : transitions) {
                State state = transition.getState();
                sb.add(convertState2Line(cursor, transition));
                handleState(state, sb, markSet);
            }
        }
    }

    protected String convertState2Line(@NonNull State state, @NonNull Transition transition)
    {
        return convertState2Node(state) + "-->|" + convertEdge2Str(transition.getEdge()) + "|" + convertState2Node(transition.getState()) + "";
    }

    protected String convertEdge2Str(Edge edge){
        if(edge instanceof EpsilonEdge){
            return edge.toString();
        }
        if(edge instanceof CharacterEdge){
            CharacterEdge characterEdge= (CharacterEdge) edge;
            // html format
            return String.format("\"#%04d;\"",(int)characterEdge.getCharacter());
        }
        return edge.toString();
    }

    protected String convertState2Node(@NonNull State state)
    {
        return state.isAccept() ?
                String.format("s_%d((%d))", state.getStateId(), state.getStateId()) :
                String.format("s_%d(%d)", state.getStateId(), state.getStateId());
    }
}
