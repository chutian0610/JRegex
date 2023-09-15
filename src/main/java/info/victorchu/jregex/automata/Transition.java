package info.victorchu.jregex.automata;

/**
 * state ==|Transition|==> state
 *
 * @author victorchu
 * @date 2023/9/5 14:55
 */
public interface Transition {

    static Transition epsilon() {
        return EpsilonTransition.INSTANCE;
    }

    static Transition character(Character c) {
        return new CharacterTransition(c);
    }
}
