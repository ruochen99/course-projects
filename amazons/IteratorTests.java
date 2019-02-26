package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static amazons.Move.mv;
import static amazons.Square.*;

/** Junit tests for our Board iterators.
 *  @author Ruochen Liu
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        legalMoveTestSquares.add(mv(sq("g8"), sq("g9"), sq("g10")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g9"), sq("f10")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g9"), sq("h10")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g9"), sq("g8")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g10"), sq("g9")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g10"), sq("f10")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g10"), sq("h10")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("g10"), sq("g8")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("h7"), sq("i7")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("h7"), sq("g6")));
        legalMoveTestSquares.add(mv(sq("g8"), sq("h7"), sq("g8")));

        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(legalMoveTestSquares.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(legalMoveTestSquares.size(), numMoves);
        assertEquals(legalMoveTestSquares.size(), moves.size());
    }

    @Test
    public void testLegalMoves2() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETESTBOARDD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        legalMoveSet.add(mv(sq("j10"), sq("j9"), sq("j10")));
        legalMoveSet.add(mv(sq("j10"), sq("j9"), sq("i9")));
        legalMoveSet.add(mv(sq("j10"), sq("j9"), sq("i10")));
        legalMoveSet.add(mv(sq("j10"), sq("i10"), sq("j10")));
        legalMoveSet.add(mv(sq("j10"), sq("i10"), sq("j9")));
        legalMoveSet.add(mv(sq("j10"), sq("i10"), sq("i9")));
        legalMoveSet.add(mv(sq("j10"), sq("i9"), sq("j10")));
        legalMoveSet.add(mv(sq("j10"), sq("i9"), sq("i10")));
        legalMoveSet.add(mv(sq("j10"), sq("i9"), sq("j9")));

        legalMoveSet.add(mv(sq("a1"), sq("a2"), sq("a1")));
        legalMoveSet.add(mv(sq("a1"), sq("a2"), sq("b1")));
        legalMoveSet.add(mv(sq("a1"), sq("a2"), sq("b2")));
        legalMoveSet.add(mv(sq("a1"), sq("b1"), sq("a1")));
        legalMoveSet.add(mv(sq("a1"), sq("b1"), sq("b2")));
        legalMoveSet.add(mv(sq("a1"), sq("b1"), sq("a2")));
        legalMoveSet.add(mv(sq("a1"), sq("b2"), sq("a1")));
        legalMoveSet.add(mv(sq("a1"), sq("b2"), sq("b1")));
        legalMoveSet.add(mv(sq("a1"), sq("b2"), sq("a2")));


        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(legalMoveSet.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(legalMoveSet.size(), numMoves);
        assertEquals(legalMoveSet.size(), moves.size());
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD =
    {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Piece[][] LEGALMOVETESTBOARD =
    {
            { E, E, E, E, S, E, E, E, S, E },
            { E, E, E, E, E, S, E, S, S, E },
            { E, E, E, E, E, S, W, S, S, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, S, B, E },
            { E, E, E, S, E, S, E, E, B, E },
            { E, E, E, S, S, S, B, S, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    Set<Move> legalMoveTestSquares = new HashSet<>();

    static final Piece[][] LEGALMOVETESTBOARDD =
    {
            { W, S, E, E, S, E, E, S, E, W },
            { S, S, E, E, E, S, E, S, E, E },
            { E, E, E, E, E, S, E, S, S, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, S, B, E },
            { E, E, E, S, E, S, E, E, B, E },
            { E, E, E, S, S, S, B, S, B, E },
            { S, S, S, E, E, E, E, E, E, E },
            { E, E, S, E, E, E, E, E, S, S },
            { W, E, S, E, E, E, E, E, S, W },
    };
    Set<Move> legalMoveSet = new HashSet<>();
}

