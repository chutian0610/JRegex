package info.victorchu.jregex.ast;

import info.victorchu.jregex.util.RegexExpTreeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class RegexParserTest {
    private static final Logger log = LoggerFactory.getLogger(RegexParserTest.class);

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
}