package info.victorchu.jregex.automata;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.automata.edge.EpsilonEdge;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author victorchu
 */
public interface StateManager
{
    State createNFAState(RegexContext context);

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
     * 根据 NFA 状态集合，创建或获取DFA 状态
     *
     * @param context
     * @param nfaStates
     * @return
     */
    State createOrGetDFAState(RegexContext context, Set<Integer> nfaStates);

    Optional<State> getDFAState(RegexContext context, Set<Integer> nfaStates);

    Set<State> getDFAMappedNFAState(State dfaState);

    Optional<State> getDFAState(Integer id);

    default State tryGetDFAState(Integer id)
    {
        Optional<State> current = getDFAState(id);
        if (!current.isPresent()) {
            throw new IllegalArgumentException("无效的DFA StateId:" + id);
        }
        return current.get();
    }

    State createMinimizationDFAState(RegexContext context);

    /**
     * 判断 NFA 状态集合 S 是否为 DFA 的接受状态。
     */
    default boolean isNFASetAccept(Set<Integer> nfaStates)
    {
        return nfaStates.stream().flatMap(x -> {
            Optional<State> y = getNFAState(x);
            return y.<Stream<? extends State>>map(Stream::of).orElseGet(Stream::empty);
        }).anyMatch(State::isAccept);
    }

    void reset();
}
