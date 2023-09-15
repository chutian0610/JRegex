package info.victorchu.jregex.automata;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */
public class Context
{
    /**
     * NFA state id generator
     */
    private AtomicInteger NEXT_NFA_ID;
    /**
     * DFA state id generator
     */
    private AtomicInteger NEXT_DFA_ID;

    private final Map<Integer, NFAState> nfaStateMap = new HashMap<>();
    private final Map<Integer, DFAState> dfaStateMap = new HashMap<>();
    private final Map<Integer, DFAState> minimizationDFAStateMap = new HashMap<>();
    private final Map<Set<NFAState>, DFAState> nfa2dfaStateMap = new HashMap<>();
    private final Map<Set<DFAState>, DFAState> dfa2dfaStateMap = new HashMap<>();

    public Map<DFAState, Set<DFAState>> getDfaStateSetMap()
    {
        return dfaStateSetMap;
    }

    private Map<DFAState, Set<DFAState>> dfaStateSetMap;

    private Set<Set<DFAState>> singleDFASet = new HashSet<>();

    public Context()
    {
        reset();
    }

    public synchronized void reset()
    {
        NEXT_NFA_ID = new AtomicInteger(0);
        NEXT_DFA_ID = new AtomicInteger(0);
        nfaStateMap.clear();
        dfaStateMap.clear();
        if (dfaStateSetMap != null) {
            dfaStateSetMap.clear();
        }
        singleDFASet.clear();
    }

    public void registerSingleMinimizationSet(Set<DFAState> state)
    {
        singleDFASet.add(state);
    }

    public Set<Set<DFAState>> getSingleMinimizationSet()
    {
        return singleDFASet;
    }

    public void initMinimizationIndexMap(Collection<Set<DFAState>> dfaStateSets)
    {
        dfaStateSetMap = new HashMap<>(dfaStateMap.size());
        refreshMinimizationIndexMap(dfaStateSets);
    }

    public void refreshMinimizationIndexMap(Collection<Set<DFAState>> dfaStateSets)
    {
        dfaStateSets.forEach(x -> x.forEach(y -> {
            dfaStateSetMap.put(y, x);
        }));
    }

    public Set<DFAState> searchDFAStateSet(DFAState state)
    {
        return dfaStateSetMap.get(state);
    }

    public Integer getNextNFAID()
    {
        return NEXT_NFA_ID.getAndIncrement();
    }

    public Integer getNextDFAID()
    {
        return NEXT_DFA_ID.getAndIncrement();
    }

    public void registerNFAState(NFAState nfaState)
    {
        nfaStateMap.put(nfaState.getId(), nfaState);
    }

    public NFAState getNFAState(Integer nfaStateId)
    {
        return nfaStateMap.get(nfaStateId);
    }

    public void registerDFAState(DFAState dfaState)
    {
        dfaStateMap.put(dfaState.getId(), dfaState);
    }

    public DFAState getDFAState(Integer dfaStateId)
    {
        return dfaStateMap.get(dfaStateId);
    }

    public Collection<DFAState> getDFAStates()
    {
        return dfaStateMap.values();
    }

    public DFAState getDFAState(Set<NFAState> nfaStateSet)
    {
        return nfa2dfaStateMap.get(nfaStateSet);
    }

    public DFAState getMinimizationDFAState(Set<DFAState> dfaStateSet)
    {
        return dfa2dfaStateMap.get(dfaStateSet);
    }

    public void registerMinimizationDFAState(DFAState dfaState)
    {
        minimizationDFAStateMap.put(dfaState.getId(), dfaState);
    }

    public DFAState getMinimizationDFAState(Integer dfaStateId)
    {
        return minimizationDFAStateMap.get(dfaStateId);
    }

    public DFAState createDFAState(Set<NFAState> nfaStateSet)
    {
        if (nfa2dfaStateMap.containsKey(nfaStateSet)) {
            return nfa2dfaStateMap.get(nfaStateSet);
        }
        else {
            DFAState dfaState = new DFAState(this::getNextDFAID, nfaStateSet, isAcceptN(nfaStateSet));
            nfa2dfaStateMap.put(nfaStateSet, dfaState);
            this.registerDFAState(dfaState);
            return dfaState;
        }
    }

    public NFAState createNFAState()
    {
        NFAState state = new NFAState(this::getNextNFAID);
        this.registerNFAState(state);
        return state;
    }

    public DFAState getRepresentation(Set<DFAState> dfaStateSet)
    {
        return dfaStateSet.stream().min(Comparator.comparingInt(DFAState::getId)).get();
    }

    public DFAState createMinimizationDFAState(Set<DFAState> dfaStateSet)
    {
        if (dfa2dfaStateMap.containsKey(dfaStateSet)) {
            return dfa2dfaStateMap.get(dfaStateSet);
        }
        else {
            Integer id = getRepresentation(dfaStateSet).getId();
            Set<NFAState> nfaStateSet = dfaStateSet.stream().flatMap(x -> x.getMappedNFAStates().stream()).collect(Collectors.toSet());
            DFAState dfaState = new DFAState(() -> id, nfaStateSet, isAcceptD(dfaStateSet), dfaStateSet);
            this.registerMinimizationDFAState(dfaState);
            dfa2dfaStateMap.put(dfaStateSet, dfaState);
            return dfaState;
        }
    }

    /**
     * 判断 NFA 状态集合 S 是否为 DFA 的接受状态。
     */
    private static boolean isAcceptN(Set<NFAState> states)
    {
        return states.stream().anyMatch(NFAState::isAccept);
    }

    /**
     * 判断 NFA 状态集合 S 是否为 DFA 的接受状态。
     */
    private static boolean isAcceptD(Set<DFAState> states)
    {
        return states.stream().anyMatch(DFAState::isAccept);
    }
}
