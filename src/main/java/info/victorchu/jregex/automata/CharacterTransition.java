package info.victorchu.jregex.automata;

import java.util.Objects;

/**
 * @author victorchu
 * @date 2023/9/5 15:10
 */
public class CharacterTransition implements Transition {
    protected Character character;

    public CharacterTransition(Character character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterTransition)) {
            return false;
        }
        CharacterTransition that = (CharacterTransition) o;
        return Objects.equals(character, that.character);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }

    @Override
    public String toString() {
        return String.format("'%s'", character);
    }
}
