package com.android.bingo;

import java.util.ArrayList;

public class BingoAI extends BingoPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	ArrayList<Integer> positions; // holds actual positions for next move.

	public BingoAI(boolean randomize) {
		super(randomize);

	}

	public int playNextMove() {
		int position = findNextMove();
		int nextMove = state.getCellDataAt(position);
		markMove(nextMove);
		return nextMove;
	}

	/**
	 * Find the best possible position for next move. 1.Idea here is first
	 * identify what is minimum number of moves required to win the game. 2.Next
	 * step is to get the actual positions of the moves which can take you to
	 * win state. 3.Final step is find the best position out of available win
	 * positions.
	 * 
	 * @return
	 */
	private int findNextMove() {
		
// hold numbers of moves required to win from current state for each WinMatrixCollection
		int[] movesRequired = new int[WinMatrixCollection.collection.size()]; 
		ArrayList<Integer> positions; // holds actual positions for next move.
		int nextBestPosition=0;
		for (int i = 0; i < movesRequired.length; i++) {
			movesRequired[i] = getMovesRequired(i);
		}

		/**
		 * Find minimum moves required to win.
		 */
		int min = movesRequired[0]; // start with the first value
		int minIndex = 0;
		for (int i = 1; i < movesRequired.length; i++) {
			if (movesRequired[i] < min) {
				min = movesRequired[i]; // new minimum value
				minIndex = i;
			}
		}

		/**
		 * Identify best position out of available positions.
		 */
		int[] bestPosition = new int[25];
		for (int i = minIndex; i < movesRequired.length; i++) {
			if (movesRequired[i] == min) {
				positions=getMovePositions(i);
				for (int j = 0; j < positions.size(); j++) {
					bestPosition[positions.get(j)] += 1;

				}
			}
		}

		int max = bestPosition[0];		
		for (int i = 0; i < bestPosition.length; i++) {
			if (bestPosition[i] > max) {

				max = bestPosition[i];
				nextBestPosition = i;
			}
		}

		return nextBestPosition;
	}

	/**
	 * Calculates the numbers on moves required to win from current state by
	 * comparing with WinMatrixCollection. It also update the actual position
	 * for the moves.
	 * 
	 * @param index
	 * @return int - numbers of moves required
	 */
	private int getMovesRequired(int index) {
		int moveRequiredCount = 0;
		ArrayList<Integer> positions = new ArrayList<Integer>();
		int[] currentState = state.getCellData();
		Integer[] targetMatrix = WinMatrixCollection.collection.elementAt(index);
		for (int i = 0; i < targetMatrix.length; i++) {
			if (currentState[i] != -1 && targetMatrix[i] == 1) {
				moveRequiredCount++;
				//positions.add(i);
			}
		}

		this.positions = positions;
		return moveRequiredCount;
	}
	private ArrayList<Integer> getMovePositions(int index) {
		
		ArrayList<Integer> positions = new ArrayList<Integer>();
		int[] currentState = state.getCellData();
		Integer[] targetMatrix = WinMatrixCollection.collection.elementAt(index);
		for (int i = 0; i < targetMatrix.length; i++) {
			if (currentState[i] != -1 && targetMatrix[i] == 1) {
				
				positions.add(i);
			}
		}

		return positions;
	
	}
}
