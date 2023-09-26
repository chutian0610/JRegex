package info.victorchu.jregex.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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