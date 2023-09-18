package info.victorchu.jregex.automata.state;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author victorchu
 */
public class GenericStateManager implements StateManager {
    private final Map<Integer, State> nfaStateMap = new HashMap<>();


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
    public State createDFAState(RegexContext context) {
        return null;
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
