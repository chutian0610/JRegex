package info.victorchu.jregex.misc;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

/**
 * @author victorchu
 */
@Data
@AllArgsConstructor
public class CharRange
{
    char from;
    char to;

    public boolean isSingle()
    {
        return from == to;
    }

    public static CharRange of(char from, char to)
    {
        return new CharRange(from, to);
    }

    public static CharRange of(char v)
    {
        return new CharRange(v, v);
    }

    public List<CharRanges.Splitter> toSplit()
    {
        return Lists.newArrayList(new CharRanges.Splitter(from, true), new CharRanges.Splitter(to, false));
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
}
