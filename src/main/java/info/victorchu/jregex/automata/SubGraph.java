package info.victorchu.jregex.automata;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * subGraph.
 * NFA 构建的中间结果
 * <pre>
 *  -- inEdge-->start  ... ... ->end)
 * </pre>

 * @author victorchu
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
public class SubGraph
{
    @NonNull
    private Edge inEdge;
    @NonNull
    private State start;
    @NonNull
    private State end;
}
