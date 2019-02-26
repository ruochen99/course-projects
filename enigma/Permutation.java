package enigma;

import java.util.ArrayList;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ruochen Liu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        permap = new HashMap<Integer, Integer>();
        ArrayList<Integer> start = new ArrayList<Integer>();
        ArrayList<Integer> end = new ArrayList<Integer>();
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                start.add(i);
            }
            if (cycles.charAt(i) == ')') {
                end.add(i);
            }
        }
        for (int i = 0; i < start.size(); i++) {
            String thisone = cycles.substring(start.get(i) + 1, end.get(i));
            addCycle(thisone);
        }
        for (int i = 0; i < _alphabet.size(); i++) {
            if (!permap.containsKey(i)) {
                permap.put(i, i);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            permap.put(_alphabet.toInt(cycle.charAt(i)),
                    _alphabet.toInt(cycle.charAt((i + 1) % cycle.length())));
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return permap.get(wrap(p));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        for (int key: permap.keySet()) {
            if (permap.get(key) == wrap(c)) {
                return key;
            }
        }
        return 0;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int result = permute(alphabet().toInt(p));
        return alphabet().toChar(result);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        int result = invert(alphabet().toInt(c));
        return alphabet().toChar(result);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < permap.keySet().size(); i++) {
            if (permap.get(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycles of this permutation. */
    private String _cycles;
    /** Permap of this permutation.*/
    private HashMap<Integer, Integer> permap;

}
