/**
 * A class providing the communication mechanism between the player and
 * the game.  All methods are synchronized because the player and the
 * game run in different threads.  All cross-thread communication is
 * managed by this class.
 * 
 * @author Daniel Szafir
 *
 */
public class AlphaBetaPlayer implements Player {
	
	int id;
	int cols;
	int opponent_id;
	
	public String name() {
		return "Palika";
	}
	
	public void init(int id, int msecPerMove, int rows, int cols) {
		this.id = id;
		this.cols = cols;

	}
	
	@Override
	public void calcMove(
			
			Connect4Board board, int oppMoveCol, Arbitrator arb)
	
			throws TimeUpException {
		
		if (board.isFull()) {
			throw new Error("Complaint: The board is full!");
		}
		
		int maxDepth = 1;
		
		int[] scoreHolder = new int[cols];
		
		int maxScore = -100;
		
		int maxScoreCol = 1;
		
		while (!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
			for (int i = 0; i < cols; i++) {
				
				if (board.isValidMove(i) == true) {
					board.move(i,  id);
					
					int alphabetaScore = alphabeta(board, maxDepth, -1000, 1000, false, arb);
					
					scoreHolder[i] = alphabetaScore;
					
					board.unmove(i, id);
				}
				else {
					scoreHolder[i] = -101;
				}
			}
			
			for (int i = 0; i < scoreHolder.length; i++) {
				if (scoreHolder[i] > maxScore) {
					maxScore = scoreHolder[i];
					maxScoreCol = i;
				}
			}
			
			arb.setMove(maxScoreCol);
			
		}
	}
	
	public int alphabeta(Connect4Board board, int depth, int a, int b, boolean isMaximizing, Arbitrator arb) {
		
		if (depth == 0 || board.isFull() || arb.isTimeUp()) {
			return calcScore(board, id) - calcScore(board, opponent_id);
		}
		
		if (isMaximizing == true) {
			int bestScore = -1000;
			
			for (int i = 0; i < cols; i++) {
				bestScore = Math.max(bestScore, alphabeta(board, depth -1, a, b, false, arb));
				a = Math.max(a, bestScore);
				
				if (a >= b) {
					break;

				}
			}
			return bestScore;
		}
		
		else {
			int bestScore = 1000;
			for (int i = 0; i < cols; i++) {
				bestScore = Math.min(bestScore, alphabeta(board, depth -1, a, b, true, arb));
				b = Math.min(a, bestScore);
				
				if (a >= b) {
					break;

				}
			}
			return bestScore;
		}
	}
	
	public int calcScore(Connect4Board board, int id) {
		
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}	
}