package info.victorchu.jregex.util;

/**
 * pair with two unmodified elements
 *
 * @param <L> type of left element
 * @param <R> type of right element
 * @author victorchu
 */
public class Pair<L, R> {
    public L getLeft() {
        return left;
    }

    private final L left;

    public R getRight() {
        return right;
    }

    private final R right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
