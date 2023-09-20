package info.victorchu.jregex.automata.state;

import info.victorchu.jregex.RegexContext;
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
    private final Map<Integer, State> minDfaStateMap = new HashMap<>();
    private final Map<Set<Integer>, Integer> nfa2dfaStateMap = new HashMap<>();
    private final Map<Integer, Set<Integer>> dfa2nfaStateMap = new HashMap<>();

    private final Map<Set<Integer>, Integer> dfa2minDfaStateMap = new HashMap<>();
    private final Map<Integer, Set<Integer>> minDfa2dfaStateMap = new HashMap<>();


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
            dfa2nfaStateMap.put(dfaState.getStateId(), nfaStates);
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
        return dfa2nfaStateMap.get(dfaState.getStateId())
                .stream()
                .flatMap(x -> getNFAState(x).<Stream<? extends State>>map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<State> getMinDFAMappedDFAState(State dfaState)
    {
        return minDfa2dfaStateMap.get(dfaState.getStateId())
                .stream()
                .flatMap(x -> getDFAState(x).<Stream<? extends State>>map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<State> getDFAState(Integer id)
    {
        return Optional.ofNullable(dfaStateMap.get(id));
    }

    @Override
    public State createOrGetMinimizationDFAState(RegexContext context, Set<Integer> dfaStates)
    {
        if (dfa2minDfaStateMap.containsKey(dfaStates)) {
            return tryGetMinimizationDFAState(dfa2minDfaStateMap.get(dfaStates));
        }
        else {
            State minDfaState = new GenericState(
                    dfaStates.stream()
                            .sorted()
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("empty dfa states:" + dfaStates)),
                    true, isDFASetAccept(dfaStates));
            dfa2minDfaStateMap.put(dfaStates, minDfaState.getStateId());
            minDfa2dfaStateMap.put(minDfaState.getStateId(), dfaStates);
            minDfaStateMap.put(minDfaState.getStateId(), minDfaState);
            return minDfaState;
        }
    }

    @Override
    public Optional<State> getMinimizationDFAState(RegexContext context, Set<Integer> dfaStates)
    {
        return Optional.ofNullable(dfa2minDfaStateMap.get(dfaStates)).flatMap(this::getMinimizationDFAState);
    }

    @Override
    public Optional<State> getMinimizationDFAState(Integer id)
    {
        return Optional.ofNullable(minDfaStateMap.get(id));
    }

    @Override
    public void reset() {
        nfaStateMap.clear();
        dfaStateMap.clear();
        minDfaStateMap.clear();
        nfa2dfaStateMap.clear();
        dfa2nfaStateMap.clear();
        dfa2minDfaStateMap.clear();
        minDfa2dfaStateMap.clear();
    }
}
