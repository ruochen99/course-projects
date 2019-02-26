package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Ruochen Liu
 */

public class MachineTest {
    /** Testing time limit. */
   /* @Rule
    public Timeout globalTimeout = Timeout.seconds(5); /

    /* ***** TESTING UTILITIES ***** */
    private Machine machine;

    /** Creates a default machine and test if it works correctly */
    public MachineTest() {
        ArrayList<Rotor> rotors;
        rotors = new ArrayList<>();
        rotors.add(new MovingRotor("I",
                new Permutation(NAVALA.get("I"), UPPER), "Q"));
        rotors.add(new MovingRotor("II",
                new Permutation(NAVALA.get("II"), UPPER), "E"));
        rotors.add(new MovingRotor("III",
                new Permutation(NAVALA.get("III"), UPPER), "V"));
        rotors.add(new MovingRotor("IV",
                new Permutation(NAVALA.get("IV"), UPPER), "J"));
        rotors.add(new MovingRotor("V",
                new Permutation(NAVALA.get("V"), UPPER), "Z"));
        rotors.add(new MovingRotor("VI",
                new Permutation(NAVALA.get("VI"), UPPER), "ZM"));
        rotors.add(new MovingRotor("VII",
                new Permutation(NAVALA.get("VII"), UPPER), "ZM"));
        rotors.add(new MovingRotor("VIII",
                new Permutation(NAVALA.get("VIII"), UPPER), "ZM"));
        rotors.add(new FixedRotor("Beta",
                new Permutation(NAVALA.get("Beta"), UPPER)));
        rotors.add(new FixedRotor("Gamma",
                new Permutation(NAVALA.get("Gamma"), UPPER)));
        rotors.add(new Reflector("B",
                new Permutation(NAVALA.get("B"), UPPER)));
        rotors.add(new Reflector("C",
                new Permutation(NAVALA.get("C"), UPPER)));

        machine = new Machine(UPPER, 5, 3, rotors);

    }


    /* ***** TESTS ***** */

    @Test
    public void checkVar() {
        MachineTest test1 = new MachineTest();
        Machine navy = test1.machine;
        assertEquals(5, machine.numRotors());
        assertEquals(3, machine.numPawls());
    }

    @Test
    public void checkExample() {
        MachineTest t1 = new MachineTest();
        Machine navy = t1.machine;
        navy.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        navy.setRotors("AXLE");
        navy.setPlugboard(new Permutation("(YF) (ZH)", UPPER));
        assertEquals(25, navy.convert(24));

        navy.setRotors("AXLE");
        assertEquals("Z", navy.convert("Y"));

    }

    @Test
    public void checkNewExample() {
        MachineTest t1 = new MachineTest();
        Machine navy = t1.machine;
        navy.insertRotors(new String[] {"B", "Beta", "I", "II", "III"});

        navy.setRotors("AAAA");
        assertEquals("ILBDAAMTAZ",
                navy.convert("HELLOWORLD"));

        navy.setRotors("AAAA");
        assertEquals("HELLOWORLD",
                navy.convert("ILBDAAMTAZ"));

        navy.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        navy.setRotors("AAAA");
        assertEquals("IHBDQQMTQZ",
                navy.convert("HELLOWORLD"));

        navy.setRotors("AAAA");
        assertEquals("HELLOWORLD",
                navy.convert("IHBDQQMTQZ"));

    }

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1",
                new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2",
                new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3",
                new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4",
                new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotorss = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotorss);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AACD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABDA", getSetting(ac, machineRotors));

    }

    @Test
    public void testStep1() {
        MachineTest t1 = new MachineTest();
        Machine navy = t1.machine;
        navy.insertRotors(new String[] {"B", "Beta", "III", "II", "I"});
        navy.setRotors("AAEQ");
        assertEquals("LG", navy.convert("AA"));

    }

    /** Helper method to get the String
     * representation of the current Rotor settings */
    private String getSetting(Alphabet alph,
                              Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }

}
