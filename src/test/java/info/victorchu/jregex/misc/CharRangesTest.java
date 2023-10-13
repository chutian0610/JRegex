package info.victorchu.jregex.misc;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author victorchu
 */
@Slf4j
@DisplayName("字符区间工具类测试")
class CharRangesTest
{
    @Test
    void testReduce01()
    {
        CharRanges charRangeHelper = new CharRanges();
        charRangeHelper.addSection('a', 'd');
        charRangeHelper.addSection('b', 'c');
        charRangeHelper.addSection('d', 'f');
        charRangeHelper.addChar('h');
        charRangeHelper.addSection('x', 'z');
        List<CharRanges.Range> list = charRangeHelper.reduce();
        Assertions.assertEquals("[[a-f], [h], [x-z]]", list.toString());
    }

    @Test
    void testNegative01()
    {
        CharRanges charRangeHelper = new CharRanges();
        charRangeHelper.addSection('a', 'd');
        charRangeHelper.addSection('b', 'c');
        charRangeHelper.addSection('d', 'f');
        charRangeHelper.addChar('h');
        charRangeHelper.addSection('x', 'z');
        List<CharRanges.Range> list = charRangeHelper.negative();
        Assertions.assertEquals("[[\\u0000-`], [g], [i-w], [{-\\uFFFF]]", list.toString());
    }

    @Test
    void testExpand01()
    {
        CharRanges charRangeHelper = new CharRanges();
        charRangeHelper.addSection('a', 'z');
        charRangeHelper.addSection('a', 'c');
        charRangeHelper.addSection('c', 'e');
        charRangeHelper.addChar('e');
        List<CharRanges.Range> list = charRangeHelper.expand();
        Assertions.assertEquals("[[a-b], [c], [d], [e], [f-z]]", list.toString());
    }

    @Test
    void testExpand02()
    {
        CharRanges charRangeHelper = new CharRanges();
        charRangeHelper.addSection('a', 'z');
        charRangeHelper.addSection('a', 'c');
        charRangeHelper.addSection('c', 'e');
        charRangeHelper.addChar('e');
        charRangeHelper.addChar('c');
        List<CharRanges.Range> list = charRangeHelper.expand();
        Assertions.assertEquals("[[a-b], [c], [d], [e], [f-z]]", list.toString());
    }

    @Test
    void testExpand03()
    {
        CharRanges charRangeHelper = new CharRanges();
        charRangeHelper.addSection('A', 'z');
        charRangeHelper.addSection('B', 'a');
        charRangeHelper.addChar('c');
        charRangeHelper.addChar('天');
        List<CharRanges.Range> list = charRangeHelper.expand();
        Assertions.assertEquals("[[A], [B-a], [b], [c], [d-z], [\\u5929]]", list.toString());
    }
}