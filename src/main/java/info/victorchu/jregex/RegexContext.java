package info.victorchu.jregex;

import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author victorchu
 */
public class RegexContext
{

    /**
     * NFA state id generator
     */
    private AtomicInteger nextNFAId;
    /**
     * DFA state id generator
     */
    private AtomicInteger nextDFAId;

    @Getter private final StateManager stateManager;

    public RegexContext(StateManager stateManager)
    {
        this.stateManager = stateManager;
        reset();
    }

    public synchronized void reset()
    {
        nextNFAId = new AtomicInteger(0);
        nextDFAId = new AtomicInteger(0);
        stateManager.reset();
    }

    public Integer getNextNFAID()
    {
        return nextNFAId.getAndIncrement();
    }

    public Integer getNextDFAID()
    {
        return nextDFAId.getAndIncrement();
    }

    public State createNFAState()
    {
        return stateManager.createNFAState(this);
    }
}
