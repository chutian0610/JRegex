package info.victorchu.jregex.automata;

import info.victorchu.jregex.RegexContext;

import java.util.Optional;

/**
 * @author victorchu
 */
public interface StateManager
{
    State createNFAState(RegexContext context);

    Optional<State> getNFAState(Integer id);

    State createDFAState(RegexContext context);

    State createMinimizationDFAState(RegexContext context);

    void reset();
}
