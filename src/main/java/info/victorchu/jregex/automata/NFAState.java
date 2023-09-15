package info.victorchu.jregex.automata;

import info.victorchu.jregex.util.Pair;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * NFA 状态，在State上封装了 Transition 映射.
 *
 * @author victorchu
 * @date 2023/9/5 15:34
 */
public class NFAState {
    /**
     * 是否是可接受状态
     */
    private boolean accept;
    /**
     * state id ( must unique )
     */
    private final int id;
    private final Map<Transition, Set<NFAState>> transitions;

    /**
     * 增加转换
     *
     * @param transition 转换
     * @param to         转换后的NFA状态
     */
    public void addTransition(Transition transition, NFAState to) {
        Set<NFAState> stateSet = transitions.computeIfAbsent(transition, k -> new HashSet<>());
        stateSet.add(to);
    }

    /**
     * 获取转换对应的所有NFA 状态
     *
     * @param transition 转换
     * @return
     */
    @Nonnull
    public List<NFAState> getSortedToStatesOfTransition(Transition transition) {
        return transitions.getOrDefault(transition, new HashSet<>(0)).stream().sorted(Comparator.comparing(NFAState::getId)).collect(Collectors.toList());
    }

    /**
     * 获取所有转换(排查Epsilon转换)
     *
     * @return
     */
    @Nonnull
    public List<Transition> getAllTransitionExceptEpsilon() {
        return transitions.keySet().stream().filter(t -> !t.equals(EpsilonTransition.INSTANCE)).collect(Collectors.toList());
    }

    /**
     * 获取所有转换(排序后)
     *
     * @return
     */
    @Nonnull
    public List<Transition> getSortedAllTransition() {
        return transitions.keySet().stream().map(x -> {
            Integer weight = transitions.get(x).stream().map(NFAState::getId).min(Comparator.naturalOrder()).orElse(0);
            return Pair.of(x, weight);
        }).sorted((o1, o2) -> o2.getRight().compareTo(o1.getRight())).map(Pair::getLeft).collect(Collectors.toList());
    }

    public NFAState(Supplier<Integer> id) {
        this.id = id.get();
        this.accept = false;
        transitions = new HashMap<>();
    }

    /**
     * 获取NFA 状态ID
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 是否是可接受状态
     *
     * @return isAccept
     */
    public boolean isAccept() {
        return accept;
    }

    /**
     * 设置NFA状态是否可接受
     *
     * @param accept 是否可接受
     */
    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    @Override
    public String toString() {

        if (isAccept()) {
            return String.format("ns_%d((%d))", getId(), getId());
        } else {
            return String.format("ns_%d(%d)", getId(), getId());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NFAState)) {
            return false;
        }
        NFAState nfaState = (NFAState) o;
        return Objects.equals(id, nfaState.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
