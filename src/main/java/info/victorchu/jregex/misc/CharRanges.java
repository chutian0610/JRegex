package info.victorchu.jregex.misc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字符range 工具类.
 *
 * @author victorchu
 */
@Slf4j
public class CharRanges
{
    private final Set<Range> rangeSet = new HashSet<>();

    @Data
    @AllArgsConstructor
    public static class RangeSplitter
    {
        char v;
        boolean start;

        @Override
        public String toString()
        {
            if (start) {
                return String.format("{%s", StringEscapeUtils.escapeJava(String.valueOf(v)));
            }
            else {
                return String.format("%s}", StringEscapeUtils.escapeJava(String.valueOf(v)));
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class Range
    {
        char from;
        char to;

        public boolean isSingle()
        {
            return from == to;
        }

        private static Range of(RangeSplitter start, RangeSplitter end)
        {
            return new Range(start.v, end.v);
        }

        private static List<RangeSplitter> toSplit(Range range)
        {
            return Lists.newArrayList(new RangeSplitter(range.from, true), new RangeSplitter(range.to, false));
        }

        @Override
        public String toString()
        {
            if (from == to) {
                return String.format("[%s]", StringEscapeUtils.escapeJava(String.valueOf(from)));
            }
            else {
                return String.format("[%s-%s]", StringEscapeUtils.escapeJava(String.valueOf(from)), StringEscapeUtils.escapeJava(String.valueOf(to)));
            }
        }

        /**
         * 判断两个区间是否可以merge(相交或相邻).
         *
         * <pre>
         *     1. [a-d] & [b-c]: 相交区间 可以merge
         *     2. [a-c] & [d-f]: 由于是离散区间，所以相邻的区间也可以merge)
         * </pre>
         *
         * @param left
         * @param right
         * @return
         */
        public static boolean canMerge(Range left, Range right)
        {
            if (left == null || right == null) {
                return false;
            }
            if (left.from <= right.from) {
                return isRangeIntersect(left, right) || isRangeAdjacent(left, right);
            }
            else {
                return isRangeIntersect(right, left) || isRangeAdjacent(right, left);
            }
        }

        private static boolean isRangeIntersect(Range left, Range right)
        {
            return right.from <= left.to;
        }

        private static boolean isRangeAdjacent(Range left, Range right)
        {
            // [a-c] [d-e]
            if (right.from == (left.to + 1)) {
                return true;
            }

            return false;
        }

        public static Range merge(Range left, Range right)
        {
            char from = right.from <= left.from ? right.from : left.from;
            char to = right.to >= left.to ? right.to : left.to;
            return new Range(from, to);
        }
    }

    /**
     * 添加一个字符
     *
     * @param c
     */
    public void addChar(char c)
    {
        addSection(c, c);
    }

    /**
     * 添加一个区间
     *
     * @param from
     * @param to
     */
    public void addSection(char from, char to)
    {
        Validate.isTrue(from <= to);
        Range range = new Range(from, to);
        rangeSet.add(range);
    }

    /**
     * 获取range的补集
     *
     * @return
     */
    public List<Range> negative()
    {
        if (rangeSet.isEmpty()) {
            return Lists.newArrayList(new Range(Character.MIN_VALUE, Character.MAX_VALUE));
        }
        // reduce后的 range list
        List<Range> reduced = reduce();
        LinkedList<RangeSplitter> negative = Lists.newLinkedList();
        LinkedList<Range> ranges = Lists.newLinkedList();
        reduced.forEach(x -> Range.toSplit(x).forEach(negative::push));
        RangeSplitter previous = null;
        RangeSplitter current = null;
        while (negative.peekLast() != null) {
            current = negative.removeLast();
            if (current.start) {
                if (previous == null) {
                    if (current.v != Character.MIN_VALUE) {
                        ranges.push(new Range(Character.MIN_VALUE, previous(current.v)));
                    }
                }
                else {
                    ranges.push(new Range(next(previous.v), previous(current.v)));
                }
            }
            previous = current;
        }
        // handle end
        if (!Objects.requireNonNull(current).start) {
            if (current.v != Character.MAX_VALUE) {
                ranges.push(new Range(next(current.v), Character.MAX_VALUE));
            }
        }
        Collections.reverse(ranges);
        return ranges;
    }

    /**
     * reduce优化
     *
     * @return reduced range list (sorted)
     */
    public List<Range> reduce()
    {
        if (rangeSet.isEmpty()) {
            return Lists.newArrayList();
        }
        LinkedList<Range> ranges = rangeSet.stream()
                .sorted(Comparator.comparing(Range::getFrom))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<Range> reduced = Lists.newLinkedList();
        for (Range current : ranges) {
            if (reduced.peek() == null) {
                reduced.push(current);
            }
            else {
                if (Range.canMerge(reduced.peek(), current)) {
                    Range merged = Range.merge(
                            Objects.requireNonNull(reduced.peek()), current);
                    reduced.pop();
                    reduced.push(merged);
                }
                else {
                    reduced.push(current);
                }
            }
        }
        Collections.reverse(reduced);
        return reduced;
    }

    /**
     * 拆分为互不重叠的子集
     *
     * @return expanded range list
     */
    public List<Range> expand()
    {
        if (rangeSet.isEmpty()) {
            return Lists.newArrayList();
        }
        LinkedList<Range> ranges = rangeSet.stream()
                .sorted(Comparator.comparing(Range::getFrom))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<RangeSplitter> expand = new LinkedList<>();
        LinkedList<RangeSplitter> stack = new LinkedList<>();
        List<Range> result = Lists.newArrayList();
        for (Range range : ranges) {
            insertRange(expand, stack, range);
        }
        RangeSplitter previous = null;
        RangeSplitter current = null;
        while (expand.peekLast() != null) {
            current = expand.removeLast();
            validateSplitter(previous, current);
            if (!current.start) {
                result.add(new Range(Objects.requireNonNull(previous).v, current.v));
            }
            previous = current;
        }
        Preconditions.checkArgument(current != null && !current.start);
        return result;
    }

    private static void validateSplitter(RangeSplitter previous, RangeSplitter current)
    {
        Preconditions.checkArgument(current != null);
        if (previous == null) {
            Preconditions.checkArgument(current.start);
        }
        else {
            Preconditions.checkArgument(current.start != previous.start);
        }
    }

    public static void insertRange(LinkedList<RangeSplitter> expand, LinkedList<RangeSplitter> stack, Range range)
    {
        if (expand.isEmpty()) {
            Range.toSplit(range).forEach(x -> pushSplitter(expand, x));
            return;
        }
        if (expand.peek().v < range.from) {
            // 不相交
            Range.toSplit(range).forEach(x -> pushSplitter(expand, x));
            return;
        }
        stack.clear();
        RangeSplitter end = expand.pop();
        stack.push(end);

        // 找到range.from 应该存放的位置
        while (!expand.isEmpty() && expand.peek().v > range.from) {
            RangeSplitter splitter = expand.pop();
            stack.push(splitter);
        }
        if (expand.isEmpty()) {
            // range.from 应该存放在最开头
            pushSplitter(expand, new RangeSplitter(range.from, true));
            // 插入range.to
            insertRangeEnd(expand, stack, range);
        }
        else {
            pushSplitter(expand, new RangeSplitter(range.from, true));
            // 插入range.to
            insertRangeEnd(expand, stack, range);
        }
    }

    private static void pushSplitter(LinkedList<RangeSplitter> expand, RangeSplitter splitter)
    {
        if (expand.isEmpty()) {
            if (splitter.start) {
                expand.push(splitter);
            }
            else {
                expand.push(new RangeSplitter(Character.MIN_VALUE, true));
                expand.push(new RangeSplitter(splitter.v == Character.MIN_VALUE ? Character.MIN_VALUE : previous(splitter.v), false));
                expand.push(splitter);
            }
            return;
        }
        RangeSplitter pre = expand.peek();
        if (pre.v > splitter.v) {
            return;
        }
        if (splitter.v == pre.v) {
            if (splitter.start == pre.start) {
                return;
            }
            else {
                if (pre.start) {
                    expand.push(splitter);
                }
                else {
                    while (expand.peek() != null && expand.peek().v == pre.v) {
                        expand.pop();
                    }
                    if (expand.isEmpty()) {
                        expand.push(new RangeSplitter(pre.v, true));
                        expand.push(new RangeSplitter(pre.v, false));
                    }
                    else {
                        if (expand.peek().isStart()) {
                            expand.push(new RangeSplitter(previous(pre.v), false));
                            expand.push(new RangeSplitter(pre.v, true));
                            expand.push(new RangeSplitter(pre.v, false));
                        }
                        else {
                            expand.push(new RangeSplitter(pre.v, true));
                            expand.push(new RangeSplitter(pre.v, false));
                        }
                    }
                }
                return;
            }
        }
        if (pre.start) {
            if (!splitter.start) {
                expand.push(splitter);
            }
            else {
                expand.push(new RangeSplitter(previous(splitter.v), false));
                expand.push(splitter);
            }
        }
        else {
            if (splitter.start) {
                expand.push(splitter);
            }
            else {
                expand.push(new RangeSplitter(next(pre.v), true));
                expand.push(splitter);
            }
        }
    }

    public static void insertRangeEnd(LinkedList<RangeSplitter> expand, LinkedList<RangeSplitter> stack, Range range)
    {
        boolean pushed = false;
        while (!stack.isEmpty()) {
            RangeSplitter splitter = stack.pop();
            if (splitter.v <= range.to) {
                pushSplitter(expand, splitter);
            }
            else {
                if (!pushed) {
                    pushSplitter(expand, new RangeSplitter(range.to, false));
                    pushed = true;
                }
                pushSplitter(expand, splitter);
            }
        }
        if (!pushed) {
            pushSplitter(expand, new RangeSplitter(range.to, false));
        }
    }

    public static char next(char c)
    {
        if (c == Character.MAX_VALUE) {
            return Character.MAX_VALUE;
        }
        return (char) (c + 1);
    }

    public static char previous(char c)
    {
        if (c == Character.MIN_VALUE) {
            return Character.MIN_VALUE;
        }
        return (char) (c - 1);
    }
}
