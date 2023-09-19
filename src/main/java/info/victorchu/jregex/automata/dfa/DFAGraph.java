package info.victorchu.jregex.automata.dfa;

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
public class DFAGraph
{
    @NonNull
    private State start;
    @NonNull
    private RegexContext context;

    public List<String> toMermaidJsChartLines()
    {
        return AutoMateMermaidJSFormatter.INSTANCE.convertDFA2FlowChartLines(this);
    }

    public String toMermaidJsChart()
    {
        return AutoMateMermaidJSFormatter.INSTANCE.convertDFA2FlowChart(this);
    }
}
