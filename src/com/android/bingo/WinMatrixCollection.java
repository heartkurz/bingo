package com.android.bingo;

import java.util.Vector;


public class WinMatrixCollection implements Runnable
{
  
  public static final Vector<Integer[]> collection = new Vector<Integer[]>();
  //public static int[][] winCombination = new int[728][];
  
  public void run() {
	initWinMatrices();
  }
  
  /**
   * Check whether the given state matrix has reached win status or not.
   * @param targetData
   * @return boolean on win status.
   */
  	
  private void initWinMatrices()
  {
    int[] elements = {0,1,2,3,4,5,6,7,8,9,10,11};
    int[] indices;
    CombinationGenerator x = new CombinationGenerator (elements.length, 5);
    int i=0;
    while (x.hasMore ()) {
      indices = x.getNext ();      
      //printMatrix(indices);      
      collection.add(genrateWinMatrix(indices));
      //printMatrix(collection.get(i));
      i++;
    }
    System.out.println(collection.size());
  }
  private Integer[] genrateWinMatrix(int[] combination)
  {
	Integer[] data = new Integer[25];
	resetData(data);
    for(int i=0;i<combination.length;i++)
    {
      switch(combination[i])
      {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:initRow(combination[i],data);break;
        case 5: 
        case 6:
        case 7:
        case 8:
        case 9:initColoum(combination[i],data);break;
        case 10:initDiagonal(0,data);break;
        case 11:initDiagonal(4,data);break;
        
      }
    }
    return data;
  }
  private void initRow(int row,Integer[] data)
  {
    for(int i=row*5,k=0;k<5;i++,k++)
    {
      data[i]=1;
    }
  }
  private void initColoum(int coloum,Integer[] data)
  {
    for(int i=coloum-5,k=0;k<5;i+=5,k++)
    {
      data[i]=1;
    }
  }
  private void initDiagonal(int dig,Integer[] data)
  {
    
      for(int i=dig,k=0;k<5;k++)
      {
    	  data[i]=1;
    	  if(dig==0)
    		  i+=6;
    	  else if(dig==4)
    		  i+=4;
      }
    
  }
  public static void printMatrix(int[] data)
  {
    for(int i=0;i<data.length;i++)
    {
        System.out.print(data[i]+"  ");
        if((i+1)%5==0)
        	System.out.println();
      
    }
  }
  public static void printMatrix(Integer[] data)
  {
    for(int i=0;i<data.length;i++)
    {
        System.out.print(data[i]+"  ");
        if((i+1)%5==0)
        	System.out.println();
      
    }
  }
  private void resetData(Integer [] data){
	  for(int i=0;i<data.length;i++)
		  data[i]=0;
  }
  
  public static boolean compareMatrix(Integer[]source,int[] target)
  {
    boolean flag=true;
    for(int i=0;i<source.length;i++)
    {
      if(source[i]==1&&target[i]!=-1)
        return false;
    }
    return flag;
  }
  
}
