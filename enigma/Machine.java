package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ruochen Liu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _plugboard = new Permutation("", _alphabet);
        _allRotors = new HashMap<>(12);
        for (Rotor r : allRotors) {
            _allRotors.put(r.name(), r);
        }
        slots = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {

        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {

        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        try {
            if (!_allRotors.get(rotors[0]).reflecting()) {
                throw error("the first rotor has to be reflector");
            }
            slots[0] = _allRotors.get(rotors[0]);
            for (int i = 1; i < Math.max(_numRotors, rotors.length); i++) {
                if (_allRotors.get(rotors[i]).reflecting()) {
                    throw error("have more than one reflector");
                } else if (i < numRotors() - numPawls()
                         && _allRotors.get(rotors[i]).rotates()) {
                    throw error("not enough fixed rotor or wrong order");
                } else if (i >= numRotors() - numPawls()
                         && !_allRotors.get(rotors[i]).rotates()) {
                    throw error("not enough moving rotor or wrong order");
                }
                slots[i] = _allRotors.get(rotors[i]);
            }
        } catch (IndexOutOfBoundsException excp) {
            throw error("wrong number of rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        try {
            if (setting.length() > slots.length) {
                throw error("setting is too long");
            }
            for (int i = 1; i < slots.length; i++) {
                slots[i].set(setting.charAt(i - 1));
            }
        } catch (IndexOutOfBoundsException excp) {
            throw error("setting is too short");
        } catch (NullPointerException excp) {
            throw error("there is no such rotor in the slot");
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */

    int convert(int c) {
        HashSet<Rotor> movemove = new HashSet<>();
        for (int i = _numRotors - 1; i > 1; i--) {
            if (slots[i].atNotch() && slots[i - 1].rotates()) {
                movemove.add(slots[i - 1]);
                movemove.add(slots[i]);
            }
        }
        movemove.add(slots[_numRotors - 1]);
        for (Rotor i: movemove) {
            i.advance();
        }
        int truec = _plugboard.permute(c);
        for (int i = slots.length - 1; i > 0; i--) {
            truec = slots[i].convertForward(truec);
        }
        truec = slots[0].permutation().permute(truec);
        for (int i = 1; i < slots.length; i++) {
            truec = slots[i].convertBackward(truec);
        }
        truec = _plugboard.permute(truec);
        return truec;

    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            int ii = _alphabet.toInt(msg.charAt(i));
            char temp = _alphabet.toChar(convert(ii));
            msg = msg.substring(0, i) + temp + msg.substring(i + 1);
        }

        return msg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of Rotors.*/
    private int _numRotors;
    /** Number of Pawals.*/
    private int _pawls;
    /** All rotors.*/
    private HashMap<String, Rotor> _allRotors;
    /** Plugboard of the machine.*/
    private Permutation _plugboard;
    /** Slots that are used this time.*/
    private Rotor[] slots;
}
