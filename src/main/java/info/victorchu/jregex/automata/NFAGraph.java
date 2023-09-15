package info.victorchu.jregex.automata;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.util.AutoMateMermaidJSFormatter;
import info.victorchu.jregex.util.Transition;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author victorchu
 */

@Getter
@RequiredArgsConstructor(staticName = "of")
public class NFAGraph {
    @NonNull
    private State start;
    @NonNull
    private RegexContext regexContext;

    public List<String> toMermaidJsChart() {
        return AutoMateMermaidJSFormatter.INSTANCE.convertNFA2FlowChart(this);
    }
}
