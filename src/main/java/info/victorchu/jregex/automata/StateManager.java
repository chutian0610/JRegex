package info.victorchu.jregex.automata;

import info.victorchu.jregex.RegexContext;

/**
 * @author victorchu
 */
public interface StateManager {
    State createNFAState(RegexContext context);
    State createDFAState(RegexContext context);
    State createMinimizationDFAState(RegexContext context);

    void reset();
}
