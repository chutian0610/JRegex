package io.github.chutian0610.jregex.misc;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author victorchu
 */
public class MetaChars
{
    public static List<CharRange> all = Lists.newArrayList(CharRange.of(Character.MIN_VALUE, Character.MAX_VALUE));
    public static List<CharRange> digit = Lists.newArrayList(CharRange.of('0', '9'));
    public static List<CharRange> word = Lists.newArrayList(
            CharRange.of('a', 'z'),
            CharRange.of('A', 'Z'),
            CharRange.of('0', '9'),
            CharRange.of('_')
    );

    public static List<CharRange> white = Lists.newArrayList(
            CharRange.of('\f'),
            CharRange.of('\n'),
            CharRange.of('\r'),
            CharRange.of('\t'),
            CharRange.of('\u000B') // \x0B
    );

    public static Map<String, List<CharRange>> MetaMap = new HashMap<>();

    static {
        MetaMap.put("\\d", digit);
        MetaMap.put("\\D", CharRanges.of(digit).negative());
        MetaMap.put("\\w", word);
        MetaMap.put("\\W", CharRanges.of(word).negative());
        MetaMap.put("\\s", white);
        MetaMap.put("\\S", CharRanges.of(white).negative());
        MetaMap.put(".", all);
    }

    public static List<CharRange> getMeta(String metaName)
    {
        Validate.isTrue(MetaMap.containsKey(metaName));
        return MetaMap.get(metaName);
    }
}
