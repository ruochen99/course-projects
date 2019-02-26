package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** An alphabet consist of Unicode characters not limited by range or order.
 * Not including any lower-case English character or "()*-".
 * @author  Ruochen liu
 * */
class VerySmartAlphabet extends Alphabet {

    /** An alphabet consist of everything in ALPHABET. */
    VerySmartAlphabet(String alphabet) {
        _characters = new HashMap<>();
        _reverseCharacter = new HashMap<>();
        int index = 0;
        alphabet = alphabet.toUpperCase();
        for (char ch : alphabet.toCharArray()) {
            if (_characters.containsValue(ch)) {
                throw error("Got repeated characters");
            } else if ("()-*".indexOf(ch) != -1) {
                throw error("illegal characters in alphabet");
            } else {
                _characters.put(index, ch);
                _reverseCharacter.put(ch, index);
                index++;
            }
        }
    }

    @Override
    int size() {
        return _characters.size();
    }

    @Override
    boolean contains(char ch) {
        return _characters.containsValue(ch);
    }

    @Override
    char toChar(int index) {
        if (!_characters.containsKey(index)) {
            throw error("character not found in alphabet");
        }
        return _characters.get(index);
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("character not found in alphabet");
        }
        return _reverseCharacter.get(ch);
    }


    /** All the characters in this. */
    private HashMap<Integer, Character> _characters;

    /** Reverse mapping of all characters to its index. */
    private HashMap<Character, Integer> _reverseCharacter;
}
