package info.victorchu.jregex.automata.dfa;

import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.GraphMatcher;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.Transition;

import java.util.Optional;

/**
 * DFA 匹配器
 *
 * @author victorchu
 */
public class DFAGraphMatcher
        implements GraphMatcher
{
    private final DFAGraph dfaGraph;

    public DFAGraphMatcher(DFAGraph dfaGraph)
    {
        this.dfaGraph = dfaGraph;
    }

    @Override
    public boolean matches(String str)
    {
        State state = dfaGraph.getStart();
        for (int i = 0, length = str.length(); i < length; i++) {
            char ch = str.charAt(i);
            Optional<State> to = state.getTransitionsOfInputEdge(Edge.character(ch)).stream().findFirst().map(Transition::getState);
            if (!to.isPresent()) {
                return false;
            }
            state = to.get();
        }
        return state.isAccept();
    }
}
