package info.victorchu.jregex.util;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;

import java.util.Collection;
import java.util.List;

/**
 * @author victorchu
 */
public class TestUtil {
    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Collection<T> items) {
        return IsIterableContainingInAnyOrder.<T>containsInAnyOrder((T[]) items.toArray());
    }
    public static String chart2ExpectString(List<String> chart){
        return "\""+String.join("\",\"",chart)+"\"";
    }
}
