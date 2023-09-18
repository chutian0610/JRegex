package info.victorchu.jregex.automata.state;

import com.google.common.collect.Sets;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.util.Transition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */
@Getter
@Slf4j
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenericState implements State {
    /**
     * {@code @Immutable}
     */
    @EqualsAndHashCode.Include
    private final int stateId;
    @Setter
    private boolean accept;
    /**
     * {@code @Immutable}
     */
    private final boolean deterministic;

    /**
     * hold Transition
     */
    private final Set<Transition> transitionSet = new HashSet<>();
    /**
     * fast index edge -> Transition
     */
    private final Map<Edge, Set<Transition>> edge2TransitionMap = new HashMap<>();

    public GenericState(int stateId, boolean deterministic) {
        this.stateId = stateId;
        this.deterministic = deterministic;
    }

    @Override
    public Set<Transition> getTransitions() {
        return transitionSet;
    }

    @Override
    public Set<Transition> getTransitionsOfEdge(Edge edge)
    {
        return edge2TransitionMap.get(edge);
    }

    @Override
    public void addTransition(Transition transition) {
        if (isDeterministic() && hasTransitionsOfSameEdge(transition)) {
            log.error("transition must deterministic, insert:{} , already exists: {}",
                    transition, edge2TransitionMap.get(transition.getEdge()));
            throw new UnsupportedOperationException("transition must deterministic");
        }
        transitionSet.add(transition);
        indexEdgeTransitionMap(transition);
    }

    private void indexEdgeTransitionMap(Transition transition) {
        if (edge2TransitionMap.containsKey(transition.getEdge())) {
            Set<Transition> set = edge2TransitionMap.get(transition.getEdge());
            set.add(transition);
        } else {
            edge2TransitionMap.put(transition.getEdge(), Sets.newHashSet(transition));
        }
    }

    @Override
    public boolean hasTransitionsOfSameEdge(Transition transition) {
        if(edge2TransitionMap.containsKey(transition.getEdge())){
            return edge2TransitionMap.get(transition.getEdge()).stream().anyMatch(x -> !x.equals(transition));
        }
        return false;
    }
}
