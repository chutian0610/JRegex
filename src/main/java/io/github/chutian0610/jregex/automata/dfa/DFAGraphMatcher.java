package io.github.chutian0610.jregex.automata.dfa;

import io.github.chutian0610.jregex.automata.Edge;
import io.github.chutian0610.jregex.automata.GraphMatcher;
import io.github.chutian0610.jregex.automata.State;
import io.github.chutian0610.jregex.automata.Transition;

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
