package info.victorchu.jregex.misc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author victorchu
 */
@Slf4j
class CharacterUtilTest
{

    @Test
    void test01(){
      log.debug(CharacterUtil.getCharacterRange('a','z').toString());
    }
}