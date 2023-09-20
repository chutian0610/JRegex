package info.victorchu.jregex.automata.state;

import com.google.common.collect.Sets;
import info.victorchu.jregex.automata.Edge;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.Transition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author victorchu
 */
@Getter
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenericState
        implements State
{
    /**
     * {@code @Immutable}
     */
    @EqualsAndHashCode.Include
    @ToString.Include
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

    public GenericState(int stateId, boolean deterministic)
    {
        this.stateId = stateId;
        this.deterministic = deterministic;
    }

    public GenericState(int stateId, boolean deterministic, boolean accept)
    {
        this.stateId = stateId;
        this.deterministic = deterministic;
        this.accept = accept;
    }

    @Override
    public Set<Transition> getTransitions()
    {
        return transitionSet;
    }

    @Override
    public Set<Transition> getTransitionsOfInputEdge(Edge edge)
    {
        Set<Transition> set = edge2TransitionMap.get(edge);
        return set == null ? new HashSet<>() : set;
    }

    @Override
    public void addTransition(Transition transition)
    {
        if (isDeterministic() && hasTransitionsOfSameEdge(transition)) {
            log.error("transition must deterministic, insert:{} , already exists: {}",
                    transition, edge2TransitionMap.get(transition.getEdge()));
            throw new UnsupportedOperationException("transition must deterministic");
        }
        transitionSet.add(transition);
        indexEdgeTransitionMap(transition);
    }

    private void indexEdgeTransitionMap(Transition transition)
    {
        if (edge2TransitionMap.containsKey(transition.getEdge())) {
            Set<Transition> set = edge2TransitionMap.get(transition.getEdge());
            set.add(transition);
        }
        else {
            edge2TransitionMap.put(transition.getEdge(), Sets.newHashSet(transition));
        }
    }

    @Override
    public boolean hasTransitionsOfSameEdge(Transition transition)
    {
        if (edge2TransitionMap.containsKey(transition.getEdge())) {
            return edge2TransitionMap.get(transition.getEdge()).stream().anyMatch(x -> !x.equals(transition));
        }
        return false;
    }
}
