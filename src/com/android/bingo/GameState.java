package com.android.bingo;

import java.io.Serializable;


/**
 * GameState class contains state of the Bingo Board.
 * @author Manoj
 *
 */
public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int CELL_CROSSED = -1;
	private int [] cellData = new int[25];
	public int currentMove =-1;
	private boolean winStatus=false; 
	
	
	/**
	 * getCellNumber returns the position of a number on board. Ranging form 1-25. 
	 * @param celldata
	 * @return integer
	 */
	public int getCellPosition(int celldata){
		
		for(int i=0;i<cellData.length;i++){
			if(cellData[i]==celldata)
				return i;
		}		
		return -1;
	}
	public int getCellDataAt(int position){
		for(int i=0;i<cellData.length;i++){
			if(i==position)
				return cellData[i];
		}		
		return -1;
	}
	
	
	public int[] getCellData() {
		return cellData;
	}

	public void setCellData(int[] cellData) {
		this.cellData = cellData;
	}

	public void crossOutCell(int position){
		cellData[position]=CELL_CROSSED;		
	}
	public void setWinStatus(boolean staus){
		this.winStatus = staus;
	}
	public boolean getWinStatus(){
		return this.winStatus;
	}

	
	
}
