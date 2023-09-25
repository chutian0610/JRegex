package info.victorchu.jregex.automata;

/**
 * @author victorchu
 */
public interface GraphMatcher
{
    boolean matches(String str);

    boolean find(int index, String str);
    default boolean find(String str){
        return find(0,str);
    }
}
