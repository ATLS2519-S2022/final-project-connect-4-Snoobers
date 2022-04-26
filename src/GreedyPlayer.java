/**
 * A class providing the communication mechanism between the player and
 * the game.  All methods are synchronized because the player and the
 * game run in different threads.  All cross-thread communication is
 * managed by this class.
 * 
 * @author Daniel Szafir
 *
 */
public class GreedyPlayer implements Player {
	
	int id;
	int cols;
	
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
		
		int[] scoreHolder = new int[cols];
		
		int maxScore = -100;
		
		int maxScoreCol = 1;
		
		for (int i = 0; i < cols; i++) {
			
			if (board.isValidMove(i) == true) {
				
				board.move(i,  id);
				
				int playerScore = (int) calcScore(board, id);
				int opponentScore = (int) calcScore(board, 3 - id);
				
				int finalScore = opponentScore - playerScore;
				
				scoreHolder[i] = finalScore;
				
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