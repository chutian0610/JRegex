package info.victorchu.jregex.util;

import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.automata.StateManager;
import info.victorchu.jregex.automata.Transition;
import info.victorchu.jregex.automata.dfa.DFAGraph;
import lombok.Getter;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 */
public class RegexTestContext
{
    @Getter
    private final StateManager stateManager;

    public RegexTestContext(StateManager stateManager)
    {
        this.stateManager = stateManager;
        reset();
    }

    public synchronized void reset()
    {
        stateManager.reset();
    }

    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Collection<T> items) {
        return IsIterableContainingInAnyOrder.<T>containsInAnyOrder((T[]) items.toArray());
    }
    public static String chart2ExpectString(List<String> chart){
        return "\"" + String.join("\",\n\"", chart) + "\"";
    }
}
