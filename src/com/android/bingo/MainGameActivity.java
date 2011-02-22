package com.android.bingo;



import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class MainGameActivity extends Activity {
	
	private static final String TAG="BINGO";
	private static final int MSG_COMPUTER_TURN = 1;
	private static final long COMPUTER_DELAY_MS = 100;
	
	private MainGameView gameView;
	private  BingoPlayer player = new BingoPlayer(true);
	private  BingoAI computer = new BingoAI(true);
	private Button nextTurn;
	private Button viewCompBoard; 
	private TextView infoText;
	private Handler msgHandler = new Handler(new MessageHandler());
	
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i(TAG,"onCreate");
        setContentView(R.layout.game_layout);
        
        nextTurn = (Button)findViewById(R.id.next_turn);
        gameView = (MainGameView)findViewById(R.id.game_view);
        infoText = (TextView) findViewById(R.id.info_turn);
        viewCompBoard = (Button) findViewById(R.id.show_board);
        
        gameView.setCellListener(new BingoCellListener());
        
        gameView.setFocusable(true);
        gameView.setFocusableInTouchMode(true);
        nextTurn.setOnClickListener(new NextTurnButtomListener());
        viewCompBoard.setOnClickListener(new ViewBoardButtonListener());
        nextTurn.setEnabled(false);
        
        updatePlayerCellDate();
	}
	
	private class BingoCellListener implements com.android.bingo.MainGameView.ICellListener {
        public void onCellSelected() {
          
                int cell = gameView.getSelection();
                int cellValue = player.state.getCellDataAt(cell);
                Log.i(TAG,"Cell Listener : cell selected="+cell+"Cell value="+cellValue);
                if(cellValue==-1)
                	infoText.setText("Cell is crossed");
                else
                	infoText.setText("You selected : "+cellValue);
                nextTurn.setEnabled(cellValue > 0); //if cell selected is already crossed the disable nextTurn button.
            
        }
    }
	private class NextTurnButtomListener implements OnClickListener{

		@Override
		public void onClick(View currentView) {
			//Toast.makeText(getApplicationContext(),"Your Move is ?",Toast.LENGTH_SHORT).show();
			if(player.state.getWinStatus()||computer.state.getWinStatus()){
				MainGameActivity.this.finish();
			}
			int selectedCell = gameView.getSelection();
			int selectedValue = player.state.getCellDataAt(selectedCell);
			infoText.setText("Your move : "+selectedValue);
        	gameView.stopBlink();
        	
        	player.markMove(selectedValue);
        	updatePlayerCellDate();
        	if(player.state.getWinStatus()){
        		infoText.setText("You won");
        		updateWinCombination();
        		nextTurn.setEnabled(true);
        		nextTurn.setText("Main Menu");     		
        		
        	}
        	else        		
        		computer.markMove(selectedValue);
        	if(computer.state.getWinStatus()){
        		infoText.setText("Computer won");
        		nextTurn.setEnabled(true);
        		nextTurn.setText("Main Menu");
        		if(!player.state.getWinStatus())
        		viewCompBoard.setVisibility(View.VISIBLE);
        	}
        	infoText.setText("Computer Turn");
        	nextTurn.setEnabled(false);
        	
        	msgHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
        	gameView.invalidate();
		}
		
	}
	private class ViewBoardButtonListener implements OnClickListener{
		@Override
		public void onClick(View v){
			gameView.playerCellData = computer.state.getCellData();
			gameView.winStatus = true;
			viewCompBoard.setVisibility(View.GONE);
			gameView.winCombination = computer.getWinCombination();
			gameView.invalidate();
			
		}
	}
	
	 private class MessageHandler implements Callback {
	        
			public boolean handleMessage(Message msg) {
	        	if (msg.what == MSG_COMPUTER_TURN) {
	        		
	        		int selectedValue = computer.playNextMove();
	            	infoText.setText("Computer Move : "+selectedValue+".Your Turn");
	            	if(computer.state.getWinStatus()){
	            		infoText.setText("Computer won");
	            		nextTurn.setEnabled(true);
	            		nextTurn.setText("Main Menu");
	            		viewCompBoard.setVisibility(View.VISIBLE);
	            	}
	            	else
	            		player.markMove(selectedValue);
	            	updatePlayerCellDate();
	            	if(player.state.getWinStatus()){
	            		infoText.setText("You won");
	            		updateWinCombination();
	            		nextTurn.setEnabled(true);
	            		nextTurn.setText("Main Menu");
	            	}        	
	    			gameView.invalidate();
	    			return true;
	        	}
	        	return false;
	        }
	 }
	public void updatePlayerCellDate(){
		gameView.playerCellData = player.state.getCellData();
	}
	private void updateWinCombination(){
		gameView.winStatus = true;
		gameView.winCombination = player.getWinCombination();
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		Log.i(TAG,"onSaveInstanceState");
		byte [] playerArray = getBytes(player);
		byte [] compterArray = getBytes(computer);
		outState.putByteArray("player", playerArray);
		outState.putByteArray("computer", compterArray);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		Log.i(TAG,"onRestoreInstanceState");
		player = (BingoPlayer) toObject(savedInstanceState.getByteArray("player"));
		computer = (BingoAI) toObject(savedInstanceState.getByteArray("computer"));
		updatePlayerCellDate();
	}
	private byte[] getBytes(Object obj){
		byte [] data = null;
		try{
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream(bos);
	      oos.writeObject(obj);
	      oos.flush();
	      oos.close();
	      bos.close();
	      data = bos.toByteArray();
		}
		catch(java.io.IOException e){
			android.util.Log.i(TAG, "Error", e);
		}
	      return data;
	}
	public static Object toObject(byte[] bytes){
		Object object = null;
		try{
			object = new java.io.ObjectInputStream(new
					java.io.ByteArrayInputStream(bytes)).readObject();
		}catch(java.io.IOException ioe){
			android.util.Log.i(TAG, "Error", ioe);
			
		}catch(java.lang.ClassNotFoundException cnfe){
			android.util.Log.i(TAG, "Error", cnfe);
			
		}
		return object;
	} 

}
