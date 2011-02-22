package com.android.bingo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BingoPlayer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected GameState state;
	private static int moveCount = 0;
	private ArrayList<Integer> winCombination;
	
	public BingoPlayer(boolean randomize) {
		state = new GameState();
		if(randomize){
			state.setCellData(getRandomGrid());
		}
	}
	
	/**
	 * Mark your move on board with the number.
	 * Also check with current move have you reach win state.
	 * @param moveNumber
	 */
	public boolean markMove(int moveNumber){
		
		int position = state.getCellPosition(moveNumber);
		//System.out.println(position);
		state.currentMove = moveNumber;
		boolean winStatus = false;
		state.crossOutCell(position);
		moveCount++;
		if(moveCount>14)
			winStatus = isBingo(state.getCellData());
		if(winStatus){
			state.setWinStatus(winStatus);
			return true; //player has won the game.
		}
		return false;
	}
	/**
	 * Fills user board with numbers with range 1-25 in random order.
	 */
	private int[] getRandomGrid(){
		Random rand = new Random();
		int [] tempData = new int [25];
		int count =0;
		int newNumber=0;
		while(true){
			if(count==25)
				break;
			newNumber = rand.nextInt(26);
			if(newNumber == 0 || linearSearch(tempData, newNumber))
				continue;
			else
				tempData[count++]=newNumber;
		}
		return tempData;
	}
	private boolean linearSearch(int[] array,int key){
		
		for(int i=0;i<array.length;i++){
			if(key==array[i])
				return true;		
		}
		return false;
	}
	public ArrayList<Integer> getWinCombination(){
		return winCombination;
	}
	public boolean isBingo(int[] targetData){
		  ArrayList<Integer> wincombination = new ArrayList<Integer>();
		  int count=0;
		 
		  for(int i=0;i<12;i++){
			  if(i<5){
				int crossCount =0;
				for(int j=i*5,k=0;k<5;k++,j++){
					if(targetData[j]!=GameState.CELL_CROSSED)
						break;
					else
						crossCount++;
				}
				if(crossCount==5){	
					count++;				//row i is full crossed.
					wincombination.add(i);
				}
			  }
			  if(i>4&&i<10){
				  int crossCount =0;
					for(int j=i-5,k=0;k<5;k++,j+=5){
						if(targetData[j]!=GameState.CELL_CROSSED)
							break;
						else
							crossCount++;
					}
					if(crossCount==5){
						count++;		//Column i is full crossed.
						wincombination.add(i);
					}
			  }
			  if(i==10){
				 int crossCount =0; 
				 for(int j=0,k=0;k<5;k++,j+=6){
					 if(targetData[j]!=GameState.CELL_CROSSED){
						 break;
					 }
					 else
						 crossCount++;
				 }
				 if(crossCount==5){
					 count++;
					 wincombination.add(i);
				 }
			  }
			  if(i==11){
					 int crossCount =0; 
					 for(int j=4,k=0;k<5;k++,j+=4){
						 if(targetData[j]!=GameState.CELL_CROSSED){
							 break;
						 }
						 else
							 crossCount++;
					 }
					 if(crossCount==5){
						 count++;
						 wincombination.add(i);
					 }
			  }
			  if(count>=5){
				  winCombination = wincombination;
				  return true;
			  }
		  } 
		 return false;
		  
	  }
	
}
