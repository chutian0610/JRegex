package io.github.chutian0610.jregex.misc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.github.chutian0610.jregex.ast.CharExp;
import io.github.chutian0610.jregex.ast.CharRangeExp;
import io.github.chutian0610.jregex.ast.MetaCharExp;
import io.github.chutian0610.jregex.ast.RegexCharExp;
import io.github.chutian0610.jregex.automata.Edge;
import io.github.chutian0610.jregex.automata.edge.CharacterEdge;
import io.github.chutian0610.jregex.automata.edge.CharacterRangeEdge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Collection;
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
    private final Set<CharRange> rangeSet = new HashSet<>();

    @Data
    @AllArgsConstructor
    public static class Splitter
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

    public static CharRanges of(Collection<CharRange> ranges)
    {
        CharRanges charRanges = new CharRanges();
        charRanges.addRanges(ranges);
        return charRanges;
    }

    public static CharRanges fromRegexCharExprs(Collection<RegexCharExp> exps)
    {
        CharRanges charRanges = new CharRanges();
        if (exps != null && !exps.isEmpty()) {
            exps.forEach(x -> {
                if (x instanceof CharExp) {
                    charRanges.addChar(((CharExp) x).getCharacter());
                }
                else if (x instanceof CharRangeExp) {
                    charRanges.addSection(((CharRangeExp) x).getFrom(), ((CharRangeExp) x).getTo());
                }
                else if (x instanceof MetaCharExp) {
                    charRanges.addRanges(MetaChars.getMeta(((MetaCharExp) x).getMetaName()));
                }
            });
        }
        return charRanges;
    }

    public static CharRanges fromEdges(Collection<Edge> edges)
    {
        CharRanges charRanges = new CharRanges();
        if (edges != null && !edges.isEmpty()) {
            edges.forEach(x -> {
                if (x instanceof CharacterEdge) {
                    charRanges.addChar(((CharacterEdge) x).getCharacter());
                }
                else if (x instanceof CharacterRangeEdge) {
                    charRanges.addSection(((CharacterRangeEdge) x).getFrom()
                            , ((CharacterRangeEdge) x).getTo());
                }
            });
        }
        return charRanges;
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
    public static boolean canMerge(CharRange left, CharRange right)
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

    private static boolean isRangeIntersect(CharRange left, CharRange right)
    {
        return right.from <= left.to;
    }

    private static boolean isRangeAdjacent(CharRange left, CharRange right)
    {
        // [a-c] [d-e]
        if (right.from == (left.to + 1)) {
            return true;
        }

        return false;
    }

    public static CharRange merge(CharRange left, CharRange right)
    {
        char from = right.from <= left.from ? right.from : left.from;
        char to = right.to >= left.to ? right.to : left.to;
        return new CharRange(from, to);
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
        CharRange CharRange = new CharRange(from, to);
        rangeSet.add(CharRange);
    }

    public void addRanges(Collection<CharRange> charRange)
    {
        Validate.isTrue(charRange != null);
        rangeSet.addAll(charRange);
    }

    /**
     * 获取range的补集
     *
     * @return
     */
    public List<CharRange> negative()
    {
        if (rangeSet.isEmpty()) {
            return Lists.newArrayList(new CharRange(Character.MIN_VALUE, Character.MAX_VALUE));
        }
        // reduce后的 CharRange list
        List<CharRange> reduced = reduce();
        LinkedList<Splitter> negative = Lists.newLinkedList();
        LinkedList<CharRange> ranges = Lists.newLinkedList();
        reduced.forEach(x -> x.toSplit().forEach(negative::push));
        Splitter previous = null;
        Splitter current = null;
        while (negative.peekLast() != null) {
            current = negative.removeLast();
            if (current.start) {
                if (previous == null) {
                    if (current.v != Character.MIN_VALUE) {
                        ranges.push(new CharRange(Character.MIN_VALUE, previous(current.v)));
                    }
                }
                else {
                    ranges.push(new CharRange(next(previous.v), previous(current.v)));
                }
            }
            previous = current;
        }
        // handle end
        if (!Objects.requireNonNull(current).start) {
            if (current.v != Character.MAX_VALUE) {
                ranges.push(new CharRange(next(current.v), Character.MAX_VALUE));
            }
        }
        Collections.reverse(ranges);
        return ranges;
    }

    /**
     * reduce优化
     *
     * @return reduced CharRange list (sorted)
     */
    public List<CharRange> reduce()
    {
        if (rangeSet.isEmpty()) {
            return Lists.newArrayList();
        }
        LinkedList<CharRange> ranges = rangeSet.stream()
                .sorted(Comparator.comparing(CharRange::getFrom))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<CharRange> reduced = Lists.newLinkedList();
        for (CharRange current : ranges) {
            if (reduced.peek() == null) {
                reduced.push(current);
            }
            else {
                if (canMerge(reduced.peek(), current)) {
                    CharRange merged = merge(
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
     * @return expanded CharRange list
     */
    public List<CharRange> expand()
    {
        if (rangeSet.isEmpty()) {
            return Lists.newArrayList();
        }
        LinkedList<CharRange> ranges = rangeSet.stream()
                .sorted(Comparator.comparing(CharRange::getFrom))
                .collect(Collectors.toCollection(LinkedList::new));
        LinkedList<Splitter> expand = new LinkedList<>();
        LinkedList<Splitter> stack = new LinkedList<>();
        List<CharRange> result = Lists.newArrayList();
        for (CharRange CharRange : ranges) {
            insertRange(expand, stack, CharRange);
        }
        Splitter previous = null;
        Splitter current = null;
        while (expand.peekLast() != null) {
            current = expand.removeLast();
            validateSplitter(previous, current);
            if (!current.start) {
                result.add(new CharRange(Objects.requireNonNull(previous).v, current.v));
            }
            previous = current;
        }
        Preconditions.checkArgument(current != null && !current.start);
        return result;
    }

    private static void validateSplitter(Splitter previous, Splitter current)
    {
        Preconditions.checkArgument(current != null);
        if (previous == null) {
            Preconditions.checkArgument(current.start);
        }
        else {
            Preconditions.checkArgument(current.start != previous.start);
        }
    }

    public static void insertRange(LinkedList<Splitter> expand, LinkedList<Splitter> stack, CharRange charRange)
    {
        if (expand.isEmpty()) {
            charRange.toSplit().forEach(x -> pushSplitter(expand, x));
            return;
        }
        if (expand.peek().v < charRange.from) {
            // 不相交
            charRange.toSplit().forEach(x -> pushSplitter(expand, x));
            return;
        }
        stack.clear();
        Splitter end = expand.pop();
        stack.push(end);

        // 找到range.from 应该存放的位置
        while (!expand.isEmpty() && expand.peek().v > charRange.from) {
            Splitter splitter = expand.pop();
            stack.push(splitter);
        }
        if (expand.isEmpty()) {
            // CharRange.from 应该存放在最开头
            pushSplitter(expand, new Splitter(charRange.from, true));
            // 插入range.to
            insertRangeEnd(expand, stack, charRange);
        }
        else {
            pushSplitter(expand, new Splitter(charRange.from, true));
            // 插入range.to
            insertRangeEnd(expand, stack, charRange);
        }
    }

    private static void pushSplitter(LinkedList<Splitter> expand, Splitter splitter)
    {
        if (expand.isEmpty()) {
            if (splitter.start) {
                expand.push(splitter);
            }
            else {
                expand.push(new Splitter(Character.MIN_VALUE, true));
                expand.push(new Splitter(splitter.v == Character.MIN_VALUE ? Character.MIN_VALUE : previous(splitter.v), false));
                expand.push(splitter);
            }
            return;
        }
        Splitter pre = expand.peek();
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
                        expand.push(new Splitter(pre.v, true));
                        expand.push(new Splitter(pre.v, false));
                    }
                    else {
                        if (expand.peek().isStart()) {
                            expand.push(new Splitter(previous(pre.v), false));
                            expand.push(new Splitter(pre.v, true));
                            expand.push(new Splitter(pre.v, false));
                        }
                        else {
                            expand.push(new Splitter(pre.v, true));
                            expand.push(new Splitter(pre.v, false));
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
                expand.push(new Splitter(previous(splitter.v), false));
                expand.push(splitter);
            }
        }
        else {
            if (splitter.start) {
                expand.push(splitter);
            }
            else {
                expand.push(new Splitter(next(pre.v), true));
                expand.push(splitter);
            }
        }
    }

    public static void insertRangeEnd(LinkedList<Splitter> expand, LinkedList<Splitter> stack, CharRange CharRange)
    {
        boolean pushed = false;
        while (!stack.isEmpty()) {
            Splitter splitter = stack.pop();
            if (splitter.v <= CharRange.to) {
                pushSplitter(expand, splitter);
            }
            else {
                if (!pushed) {
                    pushSplitter(expand, new Splitter(CharRange.to, false));
                    pushed = true;
                }
                pushSplitter(expand, splitter);
            }
        }
        if (!pushed) {
            pushSplitter(expand, new Splitter(CharRange.to, false));
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
