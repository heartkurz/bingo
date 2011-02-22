package com.android.bingo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
//import android.util.Log;
import android.widget.TextView;

import com.android.bingo.MainGameActivity;

public class BingoLauncher extends Activity{
	
	static final int MAIN_MENU_DIALOG=1;
	static final int INSTRUCTIONS_DIALOG=2;
	static final int ABOUT_DIALOG=3;
	private static final String TAG="BINGO";
	Runnable win  = new WinMatrixCollection();
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        System.setProperty("log.tag.BINGO", "SILENT");
        
        new Thread(win).start();
        showDialog(MAIN_MENU_DIALOG);
        
    }
    @Override
    protected void onStart() {
    	super.onStart();
    	showDialog(MAIN_MENU_DIALOG);
    }
    private void exit()
    {
    	this.finish();
    }
    @Override
    protected Dialog onCreateDialog(int dialog_id){
    	Dialog dialog = null;
    	Log.i(TAG, "Diaglod id = "+dialog_id);
    	switch(dialog_id){
    	case MAIN_MENU_DIALOG:
    		
    		Log.i(TAG,"MAIN MENU DIALOG CALLED");
    		final CharSequence[] items = { "Start Game", "How to Play", "About", "Exit" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setTitle("Bingo Menu");
			builder.setIcon(R.drawable.icon);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					//Toast.makeText(getApplicationContext(), items[item],
					//		Toast.LENGTH_SHORT).show();
					switch(item){
					case 0:
						Intent myIntent = new Intent(getApplicationContext(),MainGameActivity.class);
		                startActivity(myIntent);
						break;
					case 1:
						showDialog(INSTRUCTIONS_DIALOG);
						break;
					case 2:
						showDialog(ABOUT_DIALOG);						
						break;			
					case 3:
						exit();
					}
					
				}
			});
			dialog = builder.create();
			break;
    	case INSTRUCTIONS_DIALOG:
    		
    		Log.i(TAG,"INSTRUCTIONS_DIALOG CALLED");
    		builder = new AlertDialog.Builder(this);
    		builder.setTitle("How to Play");
    		builder.setIcon(R.drawable.icon);
    		builder.setMessage("Goal of the game is to achive 5 straight lines by crossingout numbers on board." +
    				"Straight line can be made by combination of horizonatl,vertical and diagonal.");
    		builder.setCancelable(false);
    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog,int id){
    				showDialog(MAIN_MENU_DIALOG);
    			}
    		});
    		dialog= builder.create();
    		break;
    	case ABOUT_DIALOG:
    		
    		Log.i(TAG,"ABOUT_DIALOG CALLED");
    		builder = new AlertDialog.Builder(this);
    		builder.setIcon(R.drawable.icon);
    		TextView mTextSample = new TextView(this);
    		mTextSample.setMovementMethod(LinkMovementMethod.getInstance());
    		String text = "Visit my <a href='http://gogreen.isgreat.org/about'>blog</a>";
    		mTextSample.setText(Html.fromHtml(text));
    		mTextSample.setTextSize(20);
    		builder.setTitle("About Bingo 1.0")
    			.setMessage("\nDeveloper : Manoj S\n")
    			.setView(mTextSample)
    			.setCancelable(false)
    			.setPositiveButton("ok", new DialogInterface.OnClickListener(){
    				public void onClick(DialogInterface dialog,int id){
    					showDialog(MAIN_MENU_DIALOG);
    				}
    			});
    		dialog= builder.create();
    		break;   		
    	}
    	
    	return dialog;
    }
}