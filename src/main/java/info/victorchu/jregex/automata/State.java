package info.victorchu.jregex.automata;

import info.victorchu.jregex.util.Transition;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 * 
 */
public interface State {
    /**
     * 状态ID(Unique).
     * @return
     */
    int getStateId();

    /**
     * 状态是否是接受状态
     * @return
     */
    boolean isAccept();

    void setAccept(boolean accept);

    boolean isDeterministic();

    /**
     * 获取状态的转换
     * @return
     */
    Set<Transition> getTransitions();
    /**
     * 增加状态的转换
     * @return
     */
    void addTransition(Transition transition);

    default void addTransition(Edge edge,State state){
        addTransition(Transition.of(edge,state));
    }

    /**
     * 判断是否存在相同边的transition
     * @param transition
     * @return
     */
    boolean hasTransitionsOfSameEdge(Transition transition);

}

