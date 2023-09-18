package info.victorchu.jregex;

import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.dfa.DFAGraph;
import info.victorchu.jregex.util.Transition;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */
public class RegexContext
{

    /**
     * NFA state id generator
     */
    private AtomicInteger nextNFAId;
    /**
     * DFA state id generator
     */
    private AtomicInteger nextDFAId;

    @Getter private final StateManager stateManager;

    public RegexContext(StateManager stateManager)
    {
        this.stateManager = stateManager;
        reset();
    }

    public synchronized void reset()
    {
        nextNFAId = new AtomicInteger(0);
        nextDFAId = new AtomicInteger(0);
        stateManager.reset();
    }

    public Integer getNextNFAID()
    {
        return nextNFAId.getAndIncrement();
    }

    public Integer getNextDFAID()
    {
        return nextDFAId.getAndIncrement();
    }

    public State createNFAState()
    {
        return stateManager.createNFAState(this);
    }

    public State tryGetNFAState(Integer id)
    {
        return stateManager.tryGetNFAState(id);
    }

    public State createDFAState(Set<Integer> nfaState)
    {
        return stateManager.createOrGetDFAState(this, nfaState);
    }

    public Optional<State> getDFAState(Set<Integer> nfaState)
    {
        return stateManager.getDFAState(this, nfaState);
    }

    public String printDFA2NFAMapping(DFAGraph dfaGraph)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<<<<<<<<<<<< NFA -> DFA >>>>>>>>>>>>>\n");
        Set<Integer> markSet = new HashSet<>();
        printDFA2NFAMapping(dfaGraph.getStart(), sb, markSet);
        return sb.toString();
    }

    private void printDFA2NFAMapping(State cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getStateId())) {
            String nfaStr = String.format("(%s)", stateManager.getDFAMappedNFAState(cursor).stream().map(x -> "ns_" + x.getStateId()).collect(Collectors.joining(",")));
            sb.append("ds_").append(cursor.getStateId()).append("<==>").append(nfaStr).append("\n");
            markSet.add(cursor.getStateId());
            Set<Transition> transitions = cursor.getTransitions();
            for (Transition transition : transitions) {
                printDFA2NFAMapping(transition.getState(), sb, markSet);
            }
        }
    }
}
