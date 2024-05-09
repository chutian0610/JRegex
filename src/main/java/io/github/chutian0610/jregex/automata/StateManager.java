package io.github.chutian0610.jregex.automata;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author victorchu
 */
public interface StateManager
{
    // ================== State ID =========================
    Integer getNextNFAID();

    Integer getNextDFAID();

    // ===================== NFA ===========================
    State createNFAState();

    Optional<State> getNFAState(Integer id);

    default State tryGetNFAState(Integer id)
    {
        Optional<State> current = getNFAState(id);
        if (!current.isPresent()) {
            throw new IllegalArgumentException("无效的NFA StateId:" + id);
        }
        return current.get();
    }

    /**
     * 判断 NFA状态集合 S 是否为接受状态。
     */
    default boolean isNFASetAccept(Set<Integer> nfaStates)
    {
        return nfaStates.stream().flatMap(x -> {
            Optional<State> y = getNFAState(x);
            return y.<Stream<? extends State>>map(Stream::of).orElseGet(Stream::empty);
        }).anyMatch(State::isAccept);
    }

    /**
     * 判断 DFA状态集合 S 是否为接受状态。
     */
    default boolean isDFASetAccept(Set<Integer> nfaStates)
    {
        return nfaStates.stream().flatMap(x -> {
            Optional<State> y = getDFAState(x);
            return y.<Stream<? extends State>>map(Stream::of).orElseGet(Stream::empty);
        }).anyMatch(State::isAccept);
    }

    // ===================== DFA ==================================
    /**
     * 根据 NFA 状态集合，创建或获取DFA 状态
     *
     * @param context
     * @param nfaStates
     * @return
     */
    State createOrGetDFAState(Set<Integer> nfaStates);

    Optional<State> getDFAState(Set<Integer> nfaStates);

    Set<State> getDFAMappedNFAState(State dfaState);

    Set<State> getMinDFAMappedDFAState(State dfaState);

    Optional<State> getDFAState(Integer id);

    default State tryGetDFAState(Integer id)
    {
        Optional<State> current = getDFAState(id);
        if (!current.isPresent()) {
            throw new IllegalArgumentException("无效的DFA StateId:" + id);
        }
        return current.get();
    }

    default Set<Edge> getNfAEdges(Set<Integer> set)
    {
        return set.stream()
                .map(this::getNFAState)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(x -> x.getTransitions().stream())
                .map(Transition::getEdge)
                .collect(Collectors.toSet());
    }

    default Set<Edge> getDfAEdges(Set<Integer> set)
    {
        return set.stream()
                .map(this::getDFAState)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(x -> x.getTransitions().stream())
                .map(Transition::getEdge)
                .collect(Collectors.toSet());
    }

    State createOrGetMinimizationDFAState(Set<Integer> dfaStates);

    Optional<State> getMinimizationDFAState(Set<Integer> dfaStates);

    Optional<State> getMinimizationDFAState(Integer id);

    default State tryGetMinimizationDFAState(Integer id)
    {
        Optional<State> current = getMinimizationDFAState(id);
        if (!current.isPresent()) {
            throw new IllegalArgumentException("无效的 MIN-DFA StateId:" + id);
        }
        return current.get();
    }
    void reset();
}
