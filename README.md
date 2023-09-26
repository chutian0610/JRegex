# JRegex

Regex Engine written by Java.

## Feature

* 将正则表达式解析为语法树

```
RegexExp regexExp=RegexParser.parse("(a|b)*abb");
log.info(RegexExpTreeFormatter.format(regexExp));
```

```
[Concat]
├──[Repeat:*]
│  └──[Or]
│     ├──[Char:a]
│     └──[Char:b]
└──[Concat]
   ├──[Char:a]
   └──[Concat]
      ├──[Char:b]
      └──[Char:b]
```

* 使用Thompson构造法从语法树构建NFA

```
NFAGraph nfaGraph=NFAGraph.build(regexExp);
log.info(nfaGraph.toMermaidJsChart());
```

打印Mermaid流程图

```mermaid
flowchart LR
    s_0(0) -->|ϵ| s_7(7)
    s_7(7) -->|ϵ| s_8(8)
    s_8(8) -->|'a'| s_9(9)
    s_9(9) -->|ϵ| s_10(10)
    s_10(10) -->|'b'| s_11(11)
    s_11(11) -->|ϵ| s_12(12)
    s_12(12) -->|'b'| s_13((13))
    s_0(0) -->|ϵ| s_1(1)
    s_1(1) -->|ϵ| s_4(4)
    s_4(4) -->|'b'| s_5(5)
    s_5(5) -->|ϵ| s_6(6)
    s_6(6) -->|ϵ| s_7(7)
    s_6(6) -->|ϵ| s_1(1)
    s_1(1) -->|ϵ| s_2(2)
    s_2(2) -->|'a'| s_3(3)
    s_3(3) -->|ϵ| s_6(6)
```

* 使用子集构造法从NFA构造DFA

```
DFAGraph dfa=nfa.toDFA();
log.info(dfa.toMermaidJsChart());
// 状态映射
log.info(dfa.printStateMapping());
```

DFA流程图

```mermaid
flowchart LR
    s_0(0) -->|'b'| s_4(4)
    s_4(4) -->|'b'| s_4(4)
    s_4(4) -->|'a'| s_1(1)
    s_1(1) -->|'b'| s_2(2)
    s_2(2) -->|'b'| s_3((3))
    s_3((3)) -->|'b'| s_4(4)
    s_3((3)) -->|'a'| s_1(1)
    s_2(2) -->|'a'| s_1(1)
    s_1(1) -->|'a'| s_1(1)
    s_0(0) -->|'a'| s_1(1)
```

状态映射

```
<<<<<<<<<<<< NFA -> DFA >>>>>>>>>>>>>
s_0<==>(s_7,s_8,s_0,s_1,s_2,s_4)
s_4<==>(s_5,s_6,s_7,s_8,s_1,s_2,s_4)
s_1<==>(s_6,s_7,s_8,s_9,s_10,s_1,s_2,s_3,s_4)
s_2<==>(s_5,s_6,s_7,s_8,s_11,s_12,s_1,s_2,s_4)
s_3<==>(s_5,s_6,s_7,s_8,s_13,s_1,s_2,s_4)
```

* DFA化简

```
DFAGraph minDfa=dfa.simplify();
log.info(minDfa.toMermaidJsChart());
// 状态映射
log.info(minDfa.printStateMapping());
```

min DFA状态

```mermaid
flowchart LR
    s_0(0) -->|'b'| s_0(0)
    s_0(0) -->|'a'| s_1(1)
    s_1(1) -->|'b'| s_2(2)
    s_2(2) -->|'b'| s_3((3))
    s_3((3)) -->|'b'| s_0(0)
    s_3((3)) -->|'a'| s_1(1)
    s_2(2) -->|'a'| s_1(1)
    s_1(1) -->|'a'| s_1(1)
```

状态映射:

```
s_0<==>(s_0,s_4)
s_1<==>(s_1)
s_2<==>(s_2)
s_3<==>(s_3)
```

* NFA 模拟执行

```
void matches(){
    RegexExp regexExpression=RegexParser.parse("(a|b)*abb");
    NFAGraph nfa=NFAGraph.build(regexExpression);
    NFAGraphMatcher nfaGraphMatcher=new NFAGraphMatcher(nfa);
    Assertions.assertTrue(nfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaabb"));
}
```

* DFA 模拟执行

```
void matches(){
    RegexExp regexExpression=RegexParser.parse("(a|b)*abb");
    NFAGraph nfa=NFAGraph.build(regexExpression);
    DFAGraph dfa=nfa.toDFA();
    DFAGraph minDfa=dfa.simplify();
    DFAGraphMatcher dfaGraphMatcher=new DFAGraphMatcher(minDfa);
    Assertions.assertTrue(dfaGraphMatcher.matches("aaaaaaaaaaaaaaaaaaaaaabb"));
}
```

## 支持的语法

- | 或
- 连接
- 重复
    - *,+,?
    - {n,m}
- 字符
- 括号()