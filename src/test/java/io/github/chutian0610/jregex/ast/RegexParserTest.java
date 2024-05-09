package io.github.chutian0610.jregex.ast;

import io.github.chutian0610.jregex.misc.RegexExpTreeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@DisplayName("语法解析测试")
class RegexParserTest {
    private static final Logger log = LoggerFactory.getLogger(RegexParserTest.class);

    @DisplayName("测试基础功能(concat,or,char,*)")
    @Test
    void parse01()
            throws IOException {
        RegexExp regexExp = RegexParser.parse("ab*c|bc");
        String tree = RegexExpTreeFormatter.format(regexExp);
        log.debug("\n================== tree ================\n{}======================================", tree);
        Assertions.assertEquals("[Or]\n" +
                "├──[Concat]\n" +
                "│  ├──[Char:a]\n" +
                "│  └──[Concat]\n" +
                "│     ├──[Repeat:*]\n" +
                "│     │  └──[Char:b]\n" +
                "│     └──[Char:c]\n" +
                "└──[Concat]\n" +
                "   ├──[Char:b]\n" +
                "   └──[Char:c]\n", tree);
    }

    @Test
    @DisplayName("测试解析括号")
    void parse02()
            throws IOException {
        RegexExp regexExp = RegexParser.parse("(A|a)b*c|bc");
        String tree = RegexExpTreeFormatter.format(regexExp);
        log.debug("\n================== tree ================\n{}======================================", tree);
        Assertions.assertEquals("[Or]\n" +
                "├──[Concat]\n" +
                "│  ├──[Or]\n" +
                "│  │  ├──[Char:A]\n" +
                "│  │  └──[Char:a]\n" +
                "│  └──[Concat]\n" +
                "│     ├──[Repeat:*]\n" +
                "│     │  └──[Char:b]\n" +
                "│     └──[Char:c]\n" +
                "└──[Concat]\n" +
                "   ├──[Char:b]\n" +
                "   └──[Char:c]\n", tree);
    }
    @Test
    @DisplayName("测试解析字符类")
    void parse03()
            throws IOException {
        RegexExp regexExp = RegexParser.parse("[a-zA-]+b");
        String tree = RegexExpTreeFormatter.format(regexExp);
        log.debug("\n================== tree ================\n{}======================================", tree);
        Assertions.assertEquals("[Concat]\n" +
                "├──[Repeat:+]\n" +
                "│  └──[CharClass: negative=false]\n" +
                "│     ├──[CharRange:a-z]\n" +
                "│     ├──[Char:A]\n" +
                "│     └──[Char:-]\n" +
                "└──[Char:b]\n", tree);
    }

    @Test
    @DisplayName("测试解析元字符")
    void parse04()
            throws IOException
    {
        RegexExp regexExp = RegexParser.parse("\\d+b");
        String tree = RegexExpTreeFormatter.format(regexExp);
        log.debug("\n================== tree ================\n{}======================================", tree);
        Assertions.assertEquals("[Concat]\n" +
                "├──[Repeat:+]\n" +
                "│  └──[MetaChar:\\d]\n" +
                "└──[Char:b]\n", tree);
    }
}