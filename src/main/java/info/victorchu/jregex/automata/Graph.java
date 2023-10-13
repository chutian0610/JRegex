package info.victorchu.jregex.automata;

import info.victorchu.jregex.misc.GraphMermaidJSFormatter;
import info.victorchu.jregex.misc.MermaidJsChartGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author victorchu
 */
public interface Graph extends MermaidJsChartGenerator
{
    State getStart();

    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     * @return
     */
    @Override
    default List<String> toMermaidJsChartLines(){
        return GraphMermaidJSFormatter.INSTANCE.printLines(this);
    }


    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     * @return
     */
    default String toMermaidJsChart(){
        return String.join("\n", toMermaidJsChartLines()) + "\n";
    }
}
