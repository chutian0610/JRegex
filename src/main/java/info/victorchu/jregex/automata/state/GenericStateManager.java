package info.victorchu.jregex.automata.state;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.automata.DFAState;
import info.victorchu.jregex.automata.NFAState;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author victorchu
 */
public class GenericStateManager implements StateManager {
    private final Map<Integer, State> nfaStateMap = new HashMap<>();
    private final Map<Integer, State> dfaStateMap = new HashMap<>();
    private final Map<Set<Integer>, Integer> nfa2dfaStateMap = new HashMap<>();
    private final Map<Integer, Set<Integer>> dfs2nfaStateMap = new HashMap<>();


    @Override
    public State createNFAState(RegexContext context) {
        State nfaState= new GenericState(context.getNextNFAID(),false);
        nfaStateMap.put(nfaState.getStateId(), nfaState);
        return nfaState;
    }

    @Override
    public Optional<State> getNFAState(Integer id)
    {
        return Optional.ofNullable(nfaStateMap.get(id));
    }

    @Override
    public State createOrGetDFAState(RegexContext context, Set<Integer> nfaStates)
    {
        if (nfa2dfaStateMap.containsKey(nfaStates)) {
            return tryGetDFAState(nfa2dfaStateMap.get(nfaStates));
        }
        else {
            State dfaState = new GenericState(context.getNextDFAID(), true, isNFASetAccept(nfaStates));
            nfa2dfaStateMap.put(nfaStates, dfaState.getStateId());
            dfs2nfaStateMap.put(dfaState.getStateId(), nfaStates);
            dfaStateMap.put(dfaState.getStateId(), dfaState);
            return dfaState;
        }
    }

    @Override
    public Optional<State> getDFAState(RegexContext context, Set<Integer> nfaStates)
    {
        return Optional.ofNullable(nfa2dfaStateMap.get(nfaStates)).flatMap(this::getDFAState);
    }

    @Override
    public Set<State> getDFAMappedNFAState(State dfaState)
    {
        return dfs2nfaStateMap.get(dfaState.getStateId())
                .stream()
                .flatMap(x -> getNFAState(x).<Stream<? extends State>>map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<State> getDFAState(Integer id)
    {
        return Optional.ofNullable(dfaStateMap.get(id));
    }

    @Override
    public State createMinimizationDFAState(RegexContext context) {
        return null;
    }

    @Override
    public void reset() {
        nfaStateMap.clear();
    }
}
