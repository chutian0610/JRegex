package info.victorchu.jregex.automata.nfa;

import info.victorchu.jregex.RegexContext;
import info.victorchu.jregex.automata.State;
import info.victorchu.jregex.util.AutoMateMermaidJSFormatter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author victorchu
 */

@Getter
@RequiredArgsConstructor(staticName = "of")
public class NFAGraph
{
    @NonNull
    private State start;
    @NonNull
    private RegexContext regexContext;

    public List<String> toMermaidJsChartLines()
    {
        return AutoMateMermaidJSFormatter.INSTANCE.convertNFA2FlowChartLines(this);
    }

    public String toMermaidJsChart()
    {
        return AutoMateMermaidJSFormatter.INSTANCE.convertNFA2FlowChart(this);
    }
}
