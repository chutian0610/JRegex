package info.victorchu.jregex.misc;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

/**
 * @author victorchu
 */
public class CharacterUtil
{
    public static List<Character> getCharacterRange(Character from,Character to){
        List<Character> characters = Lists.newArrayList();
        int begin =(int) from;
        int end = (int) to;
        for (int i = begin; i <=end ; i++) {
            characters.add((char)i);
        }
        return characters;
    }

    public static List<Character> getComplementaryAscii(Set<Character> characterSet){
        List<Character> characters = Lists.newArrayList();
        for (int i = 0; i < 128; i++) {
            if(!characterSet.contains((char)i)){
                characters.add((char)i);
            }
        }
        return characters;
    }
}
