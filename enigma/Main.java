package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ruochen Liu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine haha = readConfig();
        if (!_input.hasNext("\\*")) {
            throw error("input file not start with a setting");
        }
        while (_input.hasNextLine()) {
            String line = _input.nextLine().trim();
            if (line.indexOf("*") == 0) {
                setUp(haha, line.substring(1));
            } else {
                line = line.replaceAll("\\s+", "").toUpperCase();
                String result = haha.convert(line);
                printMessageLine(result);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.next();
            if (alphabet.charAt(1) == '-') {
                _alphabet = new CharacterRange(alphabet.charAt(0),
                        alphabet.charAt(2));
            } else {
                _alphabet = new VerySmartAlphabet(alphabet);
            }
            int ns = _config.nextInt();
            int np = _config.nextInt();
            if (ns <= np) {
                throw error("wrong number of pawls");
            }
            ArrayList<Rotor> rotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, ns, np, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        } catch (IndexOutOfBoundsException excp) {
            throw error("wrong description of alphabet");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next().toUpperCase();
            String property = _config.next();
            Permutation perm;
            if (_config.hasNext("\\(+.+")) {
                String cycle = new String();
                while (_config.hasNext("\\(+.+")) {
                    cycle = cycle + _config.next();
                }
                perm = new Permutation(cycle, _alphabet);
            } else {
                perm = new Permutation("", _alphabet);
            }

            char type = property.charAt(0);
            if (type == 'M') {
                return new MovingRotor(name, perm, property.substring(1));
            } else if (type == 'N') {
                return new FixedRotor(name, perm);
            } else if (type == 'R') {
                return new Reflector(name, perm);
            } else {
                throw error("bad rotor description");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            Scanner s = new Scanner(settings);
            String[] rotors = new String[M.numRotors()];
            for (int i = 0; i < M.numRotors(); i++) {
                rotors[i] = s.next();
            }
            String initialp = s.next();
            String plugboard = s.findWithinHorizon("\\(.+\\)", 0);
            M.insertRotors(rotors);
            M.setRotors(initialp);
            if (plugboard != null) {
                M.setPlugboard(new Permutation(plugboard, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad machine setting");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int i = 0;
        while (i + 5 < msg.length()) {
            _output.print(msg.substring(i, i + 5) + " ");
            i += 5;
        }
        _output.println(msg.substring(i));
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
