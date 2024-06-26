package io.github.chutian0610.jregex.automata.nfa;

import io.github.chutian0610.jregex.automata.Edge;
import io.github.chutian0610.jregex.automata.GraphMatcher;

import java.util.Set;

/**
 * NFA 匹配器
 *
 * @author victorchu
 */
public class NFAGraphMatcher implements GraphMatcher
{
    private final NFAGraph nfaGraph;

    public NFAGraphMatcher(NFAGraph nfaGraph) {
        this.nfaGraph = nfaGraph;
    }

    @Override
    public boolean matches(String str)
    {
      Set<Integer> set= nfaGraph.computeEpsilonClosure(nfaGraph.getStart().getStateId());
      for (int i = 0, length = str.length(); i < length; i++) {
          char ch = str.charAt(i);
          set = nfaGraph.findDFAMoveSet(set,Edge.character(ch));
      }
        return nfaGraph.isNFASetAccept(set);
    }
}
