package amazons;

import java.util.Iterator;
import java.time.Duration;
import java.time.Instant;

import static java.lang.Math.*;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Ruochen Liu
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;
    /** Limited duration time. */
    private static final Duration FOREVER = Duration.ofNanos(1100000000);
    /** My starting time. */
    private Instant now;
    /** Compare the runtime and duration.*/
    private Boolean ifStop = false;
    /** whether the search is exhausted. */
    private Boolean exhausted = false;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */

    private Move findMove() {
        Board b = new Board(board());
        int depth = 1;
        Move result = null;
        ifStop = false;
        exhausted = false;
        now = Instant.now();
        while (!ifStop && !exhausted) {
            result = _lastFoundMove;
            if (_myPiece == WHITE) {
                findMove(b, depth, true, 1, -INFTY, INFTY);
            } else {
                findMove(b, depth, true, -1, -INFTY, INFTY);
            }
            depth += 1;
        }

        if (depth == 2) {
            result = _lastFoundMove;
        } else if (exhausted && !ifStop) {
            result = _lastFoundMove;
        }
        return result;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        } else {
            Iterator<Move> copymoves = board.legalMoves();
            Move result = null;
            if (sense == 1) {
                int maxEval = -INFTY;
                while (!ifStop && copymoves.hasNext()) {
                    Move temp = copymoves.next();
                    board.makeMove(temp);
                    int eval = findMove(board, depth - 1,
                            false, -1, alpha, beta);
                    maxEval = max(maxEval, eval);
                    if (maxEval == eval) {
                        result = temp;
                    }
                    alpha = max(alpha, eval);
                    board.undo();
                    if (beta < alpha) {
                        break;
                    }
                }
                if (saveMove) {
                    _lastFoundMove = result;
                }
                return maxEval;
            } else if (sense == -1) {
                int minEval = INFTY;
                while (!ifStop && copymoves.hasNext()) {
                    Move temp = copymoves.next();
                    board.makeMove(temp);
                    int eval = findMove(board, depth - 1,
                            false, 1, alpha, beta);
                    minEval = min(minEval, eval);
                    if (minEval == eval) {
                        result = temp;
                    }
                    beta = min(beta, eval);
                    board.undo();
                    if (beta < alpha) {
                        break;
                    }
                }
                if (saveMove) {
                    _lastFoundMove = result;
                }
                return minEval;
            }
        }
        return 0;
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD.
     private int maxDepth(Board board) {
     long start = System.currentTimeMillis();
     int sense, depth;
     depth = 1;
     if (_myPiece.equals(WHITE)) {
     sense = 1;
     } else {
     sense = -1;
     }
     findMove(board, 1, false, sense, -INFTY, INFTY);
     long end = System.currentTimeMillis();
     long timeUsed = end - start;
     for (long i = timeUsed; i <= 100000; i *= 2) {
     depth += 1;
     }
     return depth;
     } */

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Instant then = Instant.now();
        if (FOREVER.compareTo(Duration.between(now, then)) <= 0) {
            ifStop = true;
            return -1;
        }

        Piece winner = board.winner();
        int whiteScore = WINNING_VALUE;
        if (winner == BLACK) {
            exhausted = true;
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            exhausted = true;
            return WINNING_VALUE;
        }

        Iterator<Move> blackMove = board.legalMoves(BLACK);
        while (blackMove.hasNext()) {
            blackMove.next();
            whiteScore -= 1;
        }

        return whiteScore;
    }
}
