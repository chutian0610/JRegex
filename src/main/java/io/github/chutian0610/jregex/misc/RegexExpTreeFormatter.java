package io.github.chutian0610.jregex.misc;

import io.github.chutian0610.jregex.ast.CharClassExp;
import io.github.chutian0610.jregex.ast.CharExp;
import io.github.chutian0610.jregex.ast.CharRangeExp;
import io.github.chutian0610.jregex.ast.ConcatExp;
import io.github.chutian0610.jregex.ast.MetaCharExp;
import io.github.chutian0610.jregex.ast.OrExp;
import io.github.chutian0610.jregex.ast.RegexExp;
import io.github.chutian0610.jregex.ast.RegexExpVisitor;
import io.github.chutian0610.jregex.ast.RepeatExp;

import java.util.LinkedList;
import java.util.Optional;

/**
 * print regex expression tree
 *
 * @author victorchu
 */
public class RegexExpTreeFormatter
        implements RegexExpVisitor<Object, Pair<RegexExpTreeFormatter.PrintStackContext, Boolean>>
{
    private static final RegexExpTreeFormatter INSTANCE = new RegexExpTreeFormatter();

    public static String format(RegexExp node)
    {
        PrintStackContext context = new PrintStackContext();
        INSTANCE.process(node, Pair.of(context, false));
        return context.sb.toString();
    }

    @Override
    public Object visitChar(CharExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        context.getLeft().pop();

        return null;
    }

    @Override
    public Object visitCharRange(CharRangeExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitCharClass(CharClassExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        for (int i = 0; i < node.getRegexCharExpList().size(); i++) {
            if(i==node.getRegexCharExpList().size()-1){
                process(node.getRegexCharExpList().get(i), Pair.of(context.getLeft(), true));
            }else {
                process(node.getRegexCharExpList().get(i), Pair.of(context.getLeft(), false));
            }
        }
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitConcat(ConcatExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        process(node.getLeft(), Pair.of(context.getLeft(), false));
        process(node.getRight(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitOr(OrExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        process(node.getLeft(), Pair.of(context.getLeft(), false));
        process(node.getRight(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitRepeat(RepeatExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        process(node.getInner(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitMetaChar(MetaCharExp node, Pair<PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().peek().ifPresent(x -> context.getLeft().append(x));
        context.getLeft().pop();
        return null;
    }

    public static class PrintStackContext
    {

        private final LinkedList<StackItem> stack;
        private final StringBuilder sb;

        public PrintStackContext()
        {
            stack = new LinkedList<>();
            sb = new StringBuilder();
        }

        public void append(Object s)
        {
            sb.append(s);
        }

        public void push(RegexExp node, boolean last)
        {
            StackItem wrapper;
            if (stack.isEmpty()) {
                wrapper = StackItem.root(node);
            }
            else {
                wrapper = StackItem.of(node, last, stack.peek());
            }
            stack.push(wrapper);
        }

        public Optional<StackItem> pop()
        {
            if (stack.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(stack.pop());
        }

        public Optional<StackItem> peek()
        {
            return Optional.ofNullable(stack.peek());
        }
    }

    /**
     * 栈中的元素
     */
    public static class StackItem
    {
        public static StackItem of(RegexExp node, boolean last, StackItem parent)
        {
            return new StackItem(node, last, parent);
        }

        public static StackItem root(RegexExp node)
        {
            return new StackItem(node, false, null);
        }

        private final RegexExp node;
        private final boolean last;

        /**
         * 父元素 reference
         */
        private final StackItem parent;

        private StackItem(RegexExp node, boolean last, StackItem parent)
        {
            this.node = node;
            this.parent = parent;
            this.last = last;
        }

        public RegexExp getNode()
        {
            return node;
        }

        public StackItem getParent()
        {
            return parent;
        }

        public boolean isRoot()
        {
            return parent == null;
        }

        public boolean isLast()
        {
            return last;
        }

        @Override
        public String toString()
        {
            if (parent == null) {
                return node.toString() + "\n";
            }
            StringBuilder nodeStr = new StringBuilder(node.toString() + "\n");
            StackItem cursor = this;
            if (cursor.last) {
                nodeStr.insert(0, "└──");
            }
            else {
                nodeStr.insert(0, "├──");
            }
            cursor = cursor.parent;
            while (cursor.parent != null) {
                if (cursor.last) {
                    nodeStr.insert(0, "   ");
                }
                else {
                    nodeStr.insert(0, "│  ");
                }
                cursor = cursor.parent;
            }
            return nodeStr.toString();
        }
    }
}
