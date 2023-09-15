package info.victorchu.jregex.automata;

import info.victorchu.jregex.util.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author victorchu
 * @date 2023/9/13 16:13
 */
public class DFAState {
    protected boolean accept;
    /**
     * state id ( must unique )
     */
    protected final int id;

    /**
     * DFA的状态变更是确定的
     */
    protected final Map<Transition, DFAState> transitions;

    private final Set<DFAState> dfaSets;

    /**
     * DFA 对应 NFA 状态集合
     */
    protected final Set<NFAState> nfaSets;

    public DFAState(Supplier<Integer> id, Set<NFAState> nfaSets, boolean isAccept) {
        this.id = id.get();
        this.accept = isAccept;
        this.nfaSets = nfaSets;
        this.transitions = new HashMap<>();
        this.dfaSets = new HashSet<>(0);
    }

    public DFAState(Supplier<Integer> id, Set<NFAState> nfaSets, boolean isAccept, Set<DFAState> dfaStateSet) {
        this.id = id.get();
        this.accept = isAccept;
        this.nfaSets = nfaSets;
        this.transitions = new HashMap<>();
        this.dfaSets = dfaStateSet;
    }

    public boolean isMinimizationDFA() {
        return !dfaSets.isEmpty();
    }

    public void addTransition(Transition transition, DFAState target) {
        transitions.put(transition, target);
    }

    public boolean containTransition(Transition transition) {
        return transitions.containsKey(transition);
    }

    @Nonnull
    public Optional<DFAState> getToStateOfTransition(Transition transition) {
        return Optional.ofNullable(transitions.get(transition));
    }

    @Nonnull
    public Set<Transition> getAllTransition() {
        return transitions.keySet();
    }

    @Nonnull
    public List<Transition> getSortedAllTransition() {
        return transitions.keySet().stream().map(x -> {
            Integer weight = transitions.get(x).getId();
            return Pair.of(x, weight);
        }).sorted((o1, o2) -> o2.getRight().compareTo(o1.getRight())).map(Pair::getLeft).collect(Collectors.toList());
    }

    @Nonnull
    public Set<DFAState> getToStates() {
        return new HashSet<>(transitions.values());
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public int getId() {
        return id;
    }

    @Nonnull
    public Set<DFAState> getMappedDFAStates() {
        return dfaSets;
    }

    @Nonnull
    public Set<NFAState> getMappedNFAStates() {
        return nfaSets;
    }

    @Override
    public String toString() {

        if (isAccept()) {
            return String.format("%ss_%d((%d))", isMinimizationDFA() ? "m" : "d", getId(), getId());
        } else {
            return String.format("%ss_%d(%d)", isMinimizationDFA() ? "m" : "d", getId(), getId());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DFAState)) {
            return false;
        }
        DFAState dfaState = (DFAState) o;
        return Objects.equals(id, dfaState.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
