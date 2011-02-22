package com.android.bingo;



import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
//import android.util.Log;

public class MainGameView extends View {
	
	private static final String TAG="BINGO";
	private static final int MARGIN = 6;
	private static final int MSG_BLINK = 1;
	public static final long FPS_MS = 1000/2;
	public int [] playerCellData;
	public ArrayList<Integer> winCombination = new ArrayList<Integer>();
	public boolean winStatus = false;
	public boolean compWinStatus =false ;
	private int mSelectedCell = -1;
	private String infomsg;
	private Paint borderPaint;
	private Paint numerPaint;
	private Paint winPaint;
    private Bitmap bmpNumbers;    
    private Drawable background;
	private Paint linePaint;
	private int offSetX=0;
	private int OffSetY=0;
	private int cellSize;
	private Rect cellRect = new Rect();
	private Rect sourceRect = new Rect();
	private final Rect cellBlinkRect = new Rect();
	private boolean cellBlinkDisplayOff;
	private ICellListener cellListener;
	private final Handler msgHandler = new Handler(new MessageHandler());
	private final int [] bmpNumberId = { R.drawable.number1,R.drawable.number2,R.drawable.number3,R.drawable.number4,R.drawable.number5,
										 R.drawable.number6,R.drawable.number7,R.drawable.number8,R.drawable.number9,R.drawable.number10,
										 R.drawable.number11,R.drawable.number12,R.drawable.number13,R.drawable.number14,R.drawable.number15,
										 R.drawable.number16,R.drawable.number17,R.drawable.number18,R.drawable.number19,R.drawable.number20,
										 R.drawable.number21,R.drawable.number22,R.drawable.number23,R.drawable.number24,R.drawable.number25};


	public MainGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		requestFocus();
		
		background = getResources().getDrawable(R.drawable.backgroung2);
        setBackgroundDrawable(background);
        
        linePaint = new Paint();
        linePaint.setColor(Color.LTGRAY);
        linePaint.setStrokeWidth(3);
        linePaint.setStyle(Style.STROKE);
        
        winPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        winPaint.setColor(Color.rgb(255,247, 153));
        winPaint.setStrokeWidth(4);
        borderPaint = new Paint();        
        borderPaint.setColor(Color.LTGRAY);
        borderPaint.setStrokeWidth(5);
        borderPaint.setStyle(Style.STROKE);
        
        numerPaint = new Paint();
        numerPaint.setAntiAlias(true);        
        bmpNumbers = getResBitmap(R.drawable.number22);
        if (bmpNumbers != null) {
        	sourceRect.set(0, 0, bmpNumbers.getWidth(), bmpNumbers.getHeight());
        	Log.i(TAG,"number rect(0,0,"+(bmpNumbers.getWidth() -1)+","+(bmpNumbers.getHeight() - 1));
        }
        if (isInEditMode()) {
            // In edit mode (e.g. in the Eclipse ADT graphical layout editor)
            // we'll use some random data to display the state.
        	int [] tempcelldata = {2,4,5,8,1,3,6,9,7,11,15,10,13,16,12,20,24,17,14,18,21,19,23,25,22};
        	playerCellData = tempcelldata;
        	
        }
	}
	
	public void setCellListener(ICellListener cellListener) {
        this.cellListener = cellListener;
    }

    public int getSelection() {
       
            return mSelectedCell;
    }

	
	 @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        //Log.i(TAG, "onDrwa call");
	        int sxy = cellSize;
	        int s3=sxy*5;
	        int lineX=offSetX;
	        int lineY=OffSetY;
	        
	        /*Draw outer rectangle on board */
	        Rect window = new Rect();	        
	        window.set(offSetX, OffSetY, s3+offSetX, s3+OffSetY);	               
	        canvas.drawRect(window,borderPaint);
	        
	        
	        /* Draw vertical and horizontal line on board to form 5x5 grid. */
	        for (int i = 0, k = sxy; i < 4; i++, k += sxy) {
	        	
	            canvas.drawLine(lineX    , lineY + k, lineX + s3 - 1, lineY + k     , linePaint);
	            canvas.drawLine(lineX + k, lineY    , lineX + k     , lineY + s3 - 1, linePaint);
	        }
	        
	        /*Draw Number bitmaps on board. */
	        for (int j = 0, k = 0, y = lineY; j < 5; j++, y += sxy) {
	            for (int i = 0, x = lineX; i < 5; i++, k++, x += sxy) {
	            	
	                cellRect.offsetTo(MARGIN+x, MARGIN+y);
	                
	                
	                if (mSelectedCell == k) {
	                    if (cellBlinkDisplayOff) {
	                        continue;
	                    }
	                }
	                if(playerCellData[k]==GameState.CELL_CROSSED){
	                	bmpNumbers = getResBitmap(R.drawable.cross);
	                }
	                else{
	                	bmpNumbers = getResBitmap(bmpNumberId[playerCellData[k]-1]);
	                }
	                canvas.drawBitmap(bmpNumbers, sourceRect, cellRect, linePaint);
	                
	        
	            }
	        }
	        if(winStatus){
	        	for(Integer combination : winCombination){
	        		
	        		
	        		if (combination >= 0 && combination <5) {
	        			// row 0 - 4
	        			int y = lineY + combination * sxy + sxy / 2;
	                    canvas.drawLine(lineX + MARGIN, y, lineX + s3 - 1 - MARGIN, y, winPaint);

	                } else if (combination >= 5 && combination < 10) {
	                	// column 0(5) - 4(9)
	                	int x = lineX + (combination - 5) * sxy + sxy / 2;
	                    canvas.drawLine(x, lineY + MARGIN, x, lineY + s3 - 1 - MARGIN, winPaint);

	                } else if (combination == 10) {
	                    // diagonal 10 is from (0,0) to (4,4)
	                	canvas.drawLine(lineX + MARGIN, lineY + MARGIN,
	                            lineX + s3 - 1 - MARGIN, lineY + s3 - 1 - MARGIN, winPaint);

	                } else if (combination == 11) {
	                    // diagonal 11 is from (0,4) to (4,0)
	                	canvas.drawLine(lineX + MARGIN, lineY + s3 - 1 - MARGIN,
	                            lineX + s3 - 1 - MARGIN, lineY + MARGIN, winPaint);
	                }
	        	}
	        }
	        
	 }
	 
	 @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        // Keeping the board squared
	        int w = MeasureSpec.getSize(widthMeasureSpec);
	        int h = MeasureSpec.getSize(heightMeasureSpec);
	        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
	        setMeasuredDimension(d, d);
	    }
	 
	 @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        super.onSizeChanged(w, h, oldw, oldh);
	        //Log.i(TAG,"Onsizechanged");
	        infomsg="newwidth="+w+" newheight="+h+" oldwd"+oldw+" oldh"+oldh;        
	        Log.i(TAG, infomsg);

	        int sx = (w - 2 * MARGIN) / 5;
	        int sy = (h - 2 * MARGIN) / 5;

	        int size = sx < sy ? sx : sy;

	        cellSize = size;
	        offSetX = (w - 5 * size) / 2;
	        OffSetY = (h - 5 * size) / 2;
	        infomsg="sizex="+sx+"sizey"+sy+"size"+size+"offx"+offSetX+"offy"+OffSetY;
	        Log.i(TAG, infomsg);
	        cellRect.set(MARGIN-1, MARGIN-1, size-3, size -3 );
	    }
	 
	 @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        int action = event.getAction();

	        if(winStatus)
	        	return true;
	        
	        if (action == MotionEvent.ACTION_DOWN) {
	            return true;

	        } else if (action == MotionEvent.ACTION_UP) {
	            int x = (int) event.getX();
	            int y = (int) event.getY();

	            int sxy = cellSize;
	            x = (x - MARGIN) / sxy;
	            y = (y - MARGIN) / sxy;

	            if (isEnabled() && x >= 0 && x < 5 && y >= 0 & y < 5) {
	                int cell = x + 5 * y;

	                stopBlink();

	                mSelectedCell = cell;
	                Log.i(TAG,"selected cell is ="+cell);	              
	                cellBlinkDisplayOff = false;
	                cellBlinkRect.set(MARGIN + x * sxy, MARGIN + y * sxy,
	                               MARGIN + (x + 1) * sxy, MARGIN + (y + 1) * sxy);

	               
	                // Start the blinker if cell is not already crossed out.
	                if(playerCellData[mSelectedCell]!=GameState.CELL_CROSSED)
	                    msgHandler.sendEmptyMessageDelayed(MSG_BLINK, FPS_MS);
	              

	                if (cellListener != null) {
	                	Log.i(TAG, "Cell listened");
	                    cellListener.onCellSelected();
	                }
	            }

	            return true;
	        }

	        return false;
	    }
	 
	 public void stopBlink() {
	        boolean hadSelection = false ;
	        mSelectedCell = -1;	       
	        if (!cellBlinkRect.isEmpty()) {
	            invalidate(cellBlinkRect);
	        }
	        cellBlinkDisplayOff = false;
	        cellBlinkRect.setEmpty();
	        msgHandler.removeMessages(MSG_BLINK);
	        if (hadSelection && cellListener != null) {
	            cellListener.onCellSelected();
	        }
	    }

	 private Bitmap getResBitmap(int bmpResId) {
	        Options opts = new Options();
	        opts.inDither = false;
	        
	        Resources res = getResources();
	        Bitmap bmp = BitmapFactory.decodeResource(res, bmpResId, opts);

	        if (bmp == null && isInEditMode()) {
	            // BitmapFactory.decodeResource doesn't work from the rendering
	            // library in Eclipse's Graphical Layout Editor. Use this workaround instead.

	            Drawable d = res.getDrawable(bmpResId);
	            int w = d.getIntrinsicWidth();
	            int h = d.getIntrinsicHeight();
	            bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
	            Canvas c = new Canvas(bmp);
	            d.setBounds(0, 0, w - 1, h - 1);
	            d.draw(c);
	        }

	        return bmp;
	    }
	 public interface ICellListener {
	        abstract void onCellSelected();
	    }
	 
	 
	 private class MessageHandler implements Callback {
	        public boolean handleMessage(Message msg) {
	            if (msg.what == MSG_BLINK) {
	                if (mSelectedCell >= 0 && cellBlinkRect.top != 0) {
	                    cellBlinkDisplayOff = !cellBlinkDisplayOff;
	                    invalidate(cellBlinkRect);

	                    if (!msgHandler.hasMessages(MSG_BLINK)) {
	                        msgHandler.sendEmptyMessageDelayed(MSG_BLINK, FPS_MS);
	                    }
	                }
	                return true;
	            }
	            return false;
	        }
	    }

}
