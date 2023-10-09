package info.victorchu.jregex.misc;

import lombok.Getter;

/**
 * pair with two unmodified elements
 *
 * @param <L> type of left element
 * @param <R> type of right element
 * @author victorchu
 */
@Getter
public class Pair<L, R>
{

    private final L left;

    private final R right;

    public static <L, R> Pair<L, R> of(L left, R right)
    {
        return new Pair<>(left, right);
    }

    private Pair(L left, R right)
    {
        this.left = left;
        this.right = right;
    }
}
