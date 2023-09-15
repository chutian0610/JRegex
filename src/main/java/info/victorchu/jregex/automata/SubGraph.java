package info.victorchu.jregex.automata;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author victorchu
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
public class SubGraph {
    @NonNull
    private Edge inEdge;
    @NonNull
    private State start;
    @NonNull
    private State end;
}
