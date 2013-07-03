package com.example.treasury;


import java.util.Date;

import com.example.treasury.Treasury.TypeStatusCell;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

public class TreasuryView extends SurfaceView implements SurfaceHolder.Callback{
	
	static public enum Status {FULL, FREE};
	static public Status status=Status.FULL;

    private SurfaceHolder mSurfaceHolder;
    private TreasuryThread mThread; 
    private Treasury treasury;
    private AI ai;

    private boolean FlagMove;
    private float xstart;
    private float ystart;
    private Context context;
    
    private Resources resources;
    private Bitmap[] picture; 
    private Bitmap[] scalepicture;
    private Bitmap[] number;    
    
    private int SizeCell;
    public static int MaxSizeCell=64;
    public static int MinSizeCell=16;
    private int SizePicture;
    private float scale;
    private Matrix matrix;
    private float Left;
    private float Top;
    
    private String NamePlayer;
    
	SharedPreferences sPref;
	final String SAVED_SIZE="SAVED_SIZE";
	final String SAVED_AI="SAVED_AI";
	final String SAVED_SCALE="SAVED_SCALE";
	
	private int countPlayer;
	private int countComputer;
	private boolean save;
	
	DbOpenHelper dbHelper;
	SQLiteDatabase db;

    public TreasuryView(Context context, AttributeSet attrs){
        super(context, attrs);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        SizeCell = 48;
        FlagMove=false;
        this.context = context; 
        //treasury = new Treasury(new SizeBoard(SizeBoard.TypeSize.Beginner));  
        //ai = new AI(treasury, new LevelAI(LevelAI.TypeAI.Professional));
        //mThread = new TreasuryThread(mSurfaceHolder, context, treasury);
        loadOption();
        
        countPlayer=0;
        countComputer=0;
        NamePlayer = getResources().getString(R.string.Player);
        
        dbHelper=new DbOpenHelper(context);
        db = dbHelper.getWritableDatabase();
        save = false;
        
        resources = context.getResources();
        
        //SizeCell = 48;
        Left=0;
        Top=0;
        
        picture = new Bitmap[10];
        
        picture[0] = BitmapFactory.decodeResource(resources, R.drawable.pic0);
        picture[1] = BitmapFactory.decodeResource(resources, R.drawable.pic1);
        picture[2] = BitmapFactory.decodeResource(resources, R.drawable.pic2);
        picture[3] = BitmapFactory.decodeResource(resources, R.drawable.pic3);  
        picture[4] = BitmapFactory.decodeResource(resources, R.drawable.pic4);
        picture[5] = BitmapFactory.decodeResource(resources, R.drawable.pic5);
        picture[6] = BitmapFactory.decodeResource(resources, R.drawable.pic6); 
        picture[7] = BitmapFactory.decodeResource(resources, R.drawable.pic);  
        picture[8] = BitmapFactory.decodeResource(resources, R.drawable.picm);          
        picture[9] = BitmapFactory.decodeResource(resources, R.drawable.picfon);         
        
        SizePicture = picture[0].getWidth();
        scale = ((float) SizeCell) / SizePicture;
        matrix = new Matrix();
        matrix.postScale(scale, scale);
        
        scalepicture = new Bitmap[10];
        scalepicture[0] = Bitmap.createBitmap(picture[0], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[1] = Bitmap.createBitmap(picture[1], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[2] = Bitmap.createBitmap(picture[2], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[3] = Bitmap.createBitmap(picture[3], 0, 0,SizePicture, SizePicture, matrix, true); 
        scalepicture[4] = Bitmap.createBitmap(picture[4], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[5] = Bitmap.createBitmap(picture[5], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[6] = Bitmap.createBitmap(picture[6], 0, 0,SizePicture, SizePicture, matrix, true);  
        scalepicture[7] = Bitmap.createBitmap(picture[7], 0, 0,SizePicture, SizePicture, matrix, true);        
        scalepicture[8] = Bitmap.createBitmap(picture[8], 0, 0,SizePicture, SizePicture, matrix, true); 
        scalepicture[9] = Bitmap.createBitmap(picture[9], 0, 0,SizePicture, SizePicture, matrix, true);  
        
        number = new Bitmap[10];
        
        number[0] = BitmapFactory.decodeResource(resources, R.drawable.red0);
        number[1] = BitmapFactory.decodeResource(resources, R.drawable.red1);
        number[2] = BitmapFactory.decodeResource(resources, R.drawable.red2);
        number[3] = BitmapFactory.decodeResource(resources, R.drawable.red3);  
        number[4] = BitmapFactory.decodeResource(resources, R.drawable.red4);
        number[5] = BitmapFactory.decodeResource(resources, R.drawable.red5);
        number[6] = BitmapFactory.decodeResource(resources, R.drawable.red6); 
        number[7] = BitmapFactory.decodeResource(resources, R.drawable.red7);  
        number[8] = BitmapFactory.decodeResource(resources, R.drawable.red8);          
        number[9] = BitmapFactory.decodeResource(resources, R.drawable.red9);  
        
    }

	@Override protected void onSizeChanged(int w, int h, int oldw, int oldh){
	     super.onSizeChanged(w, h, oldw, oldh);
	     NewGame();
	}
	
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    	mThread.initPositions(height, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
    	mThread = new TreasuryThread(mSurfaceHolder, context, this);
        mThread.setRunning(true);
        mThread.start();     
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        
        saveOption();
        db.close();
        
        mThread.setRunning(false);
        while (retry){
            try{
                mThread.join();
                retry = false;
            }
            catch (InterruptedException e) { }
        }    	
    }
    
    public void stopGame(){
      if (mThread != null)
        mThread.setRunning(false);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	int action = event.getAction();
    	if(action == MotionEvent.ACTION_DOWN){
    	  FlagMove=false;
          xstart = event.getX();
          ystart = event.getY();
          return true;
    	}
    	
    	if(action == MotionEvent.ACTION_MOVE){
    		if(FlagMove || Math.abs(xstart - event.getX())>SizeCell/2 || Math.abs(ystart - event.getY())>SizeCell/2){
            FlagMove=true;
            if(Left + event.getX() - xstart < 0)
            	if(Left + event.getX() - xstart <= -SizeCell*treasury.GetSCurrent().Getcolumns() + this.getWidth())
            	  if(-SizeCell*treasury.GetSCurrent().Getcolumns() + this.getWidth()>0)
            		  Left = 0;
            	  else
            		  Left = -SizeCell*treasury.GetSCurrent().Getcolumns() + this.getWidth();
            	else
              	  Left = Left + event.getX() - xstart;	
            else
            	Left = 0;
            
            if(Top + event.getY() - ystart<=0)
            	if(Top + event.getY() - ystart < -SizeCell*treasury.GetSCurrent().Getlines() + this.getHeight())
            		if(-SizeCell*treasury.GetSCurrent().Getlines() + this.getHeight()>0)
                      Top = 0;
            		else
                      Top = -SizeCell*treasury.GetSCurrent().Getlines() + this.getHeight();           			
            	else
                  Top = Top + event.getY() - ystart;
            else
                Top = 0;
            
            xstart = event.getX();
            ystart = event.getY();
    		}
            return true;
      	}
    	
    	if(action == MotionEvent.ACTION_UP){
    		if(FlagMove){FlagMove=false; return false;}
            int x=-1;
            int y=-1;
            FlagMove=false;
            for(int line=0; line<treasury.GetSCurrent().Getlines(); line++){
              if(Top + line*SizeCell<=event.getY() && event.getY()<Top + (line+1)*SizeCell){
                  y=line;
              }
            }
            for(int column=0; column<treasury.GetSCurrent().Getcolumns(); column++){
              if(Left + column*SizeCell<=event.getX() && event.getX()<Left + (column+1)*SizeCell){
                x=column;
              }
            }  
          
            if(y<0 || x<0) return false;  
            
            if(treasury.GetStatusCell(y, x)==TypeStatusCell.Open && treasury.EndGame()==false) return false;
            
            if(treasury.EndGame()){
            	//this.mThread.setRunning(false);
            	if(save)
            	    showGameOverDialog();
            	else
                	save();
            	return true;
            }
            // Ход пользователя
            countPlayer = countPlayer + treasury.Course(y, x);
            
            if(treasury.EndGame()){
            	//this.mThread.setRunning(false);
            	if(save)
            	    showGameOverDialog();
            	else
                	save();
            	return true;
            }            
            
            // Ход Компьютера
            if(status == Status.FULL){
            	if(ai.getLevelAI().getTypeAI()!=LevelAI.TypeAI.NoAI){
            		countComputer = countComputer + ai.Course();
            	}
            }
            
            if(treasury.EndGame()){
            	//this.mThread.setRunning(false);
            	if(save)
            	    showGameOverDialog();
            	else
                	save();
            	return true;
            }
            
            return true;
      	}
    	
    	return false;
    }
    
    private void save(){
    	if(countPlayer>=countComputer){
    		final EditText input = new EditText(context);
    		input.setText(NamePlayer);
            new AlertDialog.Builder(context)
    		.setTitle(getResources().getString(R.string.InputName))
    		.setView(input)
    		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		         public void onClick(DialogInterface dialog, int whichButton) {
    		             //String editable = input.getText().toString(); 
    		             // deal with the editable
    		        	 ((TreasuryActivity)context).setNamePlayer(input.getText().toString()); 
    		        	 saveRecord();
    		         }
    		    }).show();              
    	}else{
            saveRecord();
    	}
    }
    private void saveRecord(){
    	int countVictory=0;
         ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.AI, ai.getLevelAI().getTypeAI().toString());
        cv.put(DbOpenHelper.SIZE, treasury.GetSCurrent().getTypeSize().toString());
        cv.put(DbOpenHelper.COURSE, treasury.GetCourse());   
        
    	if(countPlayer>=countComputer){
    		//cv.put(DbOpenHelper.WINNER, input.getText().toString());
            cv.put(DbOpenHelper.WINNER_COUNT, countPlayer);  
            cv.put(DbOpenHelper.LOSER, getResources().getString(R.string.Computer));
            cv.put(DbOpenHelper.LOSER_COUNT, countComputer); 
            countVictory = countPlayer;
            
    	}else{
            //cv.put(DbOpenHelper.WINNER, getResources().getString(R.string.Computer));
            cv.put(DbOpenHelper.WINNER_COUNT, countComputer);  
            cv.put(DbOpenHelper.LOSER, getResources().getString(R.string.Player));
            cv.put(DbOpenHelper.LOSER_COUNT, countPlayer);    
            countVictory = countComputer;
    	}
    	
    	Date d =new Date();
    	long dd = d.getTime();
        cv.put(DbOpenHelper.DATE_VICTORY, dd);
        //db.delete(DbOpenHelper.TABLE_NAME, null, null);
        
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + DbOpenHelper.TABLE_NAME
        		+ " WHERE " 
        		+ DbOpenHelper.AI + " = ?" + " AND "
        		+ DbOpenHelper.SIZE + " = ?"  + " AND "
        		+ DbOpenHelper.DATE_VICTORY + " < " + Long.toString(dd) + " AND "
        		+ DbOpenHelper.COURSE + " < " + Integer.toString(treasury.GetCourse()) + " OR " 
        		+ " (" + DbOpenHelper.COURSE + " = " + Integer.toString(treasury.GetCourse())
        		+ " AND " + DbOpenHelper.WINNER_COUNT + " < " + Integer.toString(countVictory) + ")", new String[]{ai.getLevelAI().getTypeAI().toString(), treasury.GetSCurrent().getTypeSize().toString()});
        
        if(c.moveToFirst()){
        	if(c.getInt(0)<DbOpenHelper.MAX_RECORDS_COUNT){
            	if(countPlayer>=countComputer){
            		cv.put(DbOpenHelper.WINNER, NamePlayer);
            	}else{
                    cv.put(DbOpenHelper.WINNER, getResources().getString(R.string.Computer)); 
            	}
        		
        		db.insert(DbOpenHelper.TABLE_NAME,null,cv);	
        	}
        }
        
        save = true;
        c.close();
    }
    
    public 	SQLiteDatabase getdb(){
    	return db;
    }
    
    public void restBase(){
    	db.delete(DbOpenHelper.TABLE_NAME, null, null);
    }
    
    public void setNamePlayer(String name){
        NamePlayer = name; 
    }
    
    private void showGameOverDialog(){
       final AlertDialog.Builder dialogBuilder = 
          new AlertDialog.Builder(getContext());
       String temp="";
       dialogBuilder.setTitle(getResources().getString(R.string.game_over));
       dialogBuilder.setCancelable(false);

       if(ai.getLevelAI().getTypeAI()==LevelAI.TypeAI.NoAI){
    	   temp = Integer.toString(countPlayer);
       }else{
           temp = Integer.toString(countPlayer) + "/" + Integer.toString(countComputer);
       }
    	   
       
       dialogBuilder.setMessage(getResources().getString(
          R.string.results_format, treasury.GetCourse(), temp));
       dialogBuilder.setPositiveButton(R.string.reset_game,
          new DialogInterface.OnClickListener(){

             @Override public void onClick(DialogInterface dialog, int which){
            	 NewGame(); // set up and start a new game
             } // end method onClick
          } // end anonymous inner class
       ); // end call to setPositiveButton
       
       dialogBuilder.setNegativeButton(R.string.cancel,
    	          new DialogInterface.OnClickListener(){

    	             @Override public void onClick(DialogInterface dialog, int which){
    	            	// NewGame(); // set up and start a new game
    	             } // end method onClick
    	          } // end anonymous inner class
    	       ); // end call to setPositiveButton       

       ((Activity)context).runOnUiThread(
          new Runnable() {
             public void run(){
                dialogBuilder.show();
             } // end method run
          } // end Runnable
       ); 
    } 
    
    @Override
	public void onDraw(Canvas canvas){
        Paint paint = new Paint(); 
        Rect bounds = new Rect();
        String str;
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.FILL);   
        paint.setAntiAlias(true);
        canvas.drawPaint(paint);
        
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);   
    
        for(int l=0; l<treasury.GetSCurrent().Getlines();l++)
            for(int c=0;c<treasury.GetSCurrent().Getcolumns();c++){
             	if(treasury.GetStatusCell(l, c) == Treasury.TypeStatusCell.Open) { 
             		 canvas.drawBitmap(scalepicture[9], Left + SizeCell*c, Top + SizeCell*l, null); 
                     if(treasury.GetValueCell(l, c) == -1) canvas.drawBitmap(scalepicture[1], Left + SizeCell*c, Top + SizeCell*l, null);
                     if(treasury.GetValueCell(l, c) == -2) canvas.drawBitmap(scalepicture[2], Left + SizeCell*c, Top + SizeCell*l, null);
                     if(treasury.GetValueCell(l, c) == -4) canvas.drawBitmap(scalepicture[3], Left + SizeCell*c, Top + SizeCell*l, null);
                     if(treasury.GetValueCell(l, c) == -8) canvas.drawBitmap(scalepicture[4], Left + SizeCell*c, Top + SizeCell*l, null); 
                     if(treasury.GetValueCell(l, c) == -16) canvas.drawBitmap(scalepicture[5], Left + SizeCell*c, Top + SizeCell*l, null);
                     if(treasury.GetValueCell(l, c) == -32) canvas.drawBitmap(scalepicture[6], Left + SizeCell*c, Top + SizeCell*l, null);    
                     //if(treasury.GetValueCell(l, c) >= 0) canvas.drawText(String.valueOf(treasury.GetValueCell(l, c)), Left + SizeCell*c + (int)(SizeCell/3), Top + SizeCell*l + (int)(SizeCell/3), paint);     
                     //if(treasury.GetValueCell(l, c) >= 0) canvas.drawBitmap(getBitmapText(String.valueOf(treasury.GetValueCell(l, c))), Left + SizeCell*c, Top + SizeCell*l, null);
                     if(treasury.GetValueCell(l, c) > 0)
                     {
                    	 paint.setTextSize((int)SizeCell/2); 
                    	 str = String.valueOf(treasury.GetValueCell(l, c));
                         paint.getTextBounds(str, 0, str.length(), bounds);
                         paint.setColor(getColorNumber(treasury.GetValueCell(l, c)));
                         canvas.drawText(str, Left + SizeCell*c + (int)((SizeCell - bounds.width())/2), Top + SizeCell*l + (int)((SizeCell - bounds.height())/2) + bounds.height(), paint); 
                     }
             	}
             	else{
             		if(treasury.GetStatusCell(l, c) == Treasury.TypeStatusCell.Close)canvas.drawBitmap(scalepicture[7], Left + SizeCell*c, Top + SizeCell*l, null);  
         			if(treasury.GetStatusCell(l, c) == Treasury.TypeStatusCell.Metka)canvas.drawBitmap(scalepicture[8], Left + SizeCell*c, Top + SizeCell*l, null);   
             	}
            }
        DrawResult(canvas);
    }
    
    private int getColorNumber(int Number){
    	
    	double len = treasury.getMAX() - treasury.getMIN();
    	double num = Number - treasury.getMIN();
    	if(len==0 && treasury.getMAX()==0) return 0xFF8080FF;
    	if(len==0 && treasury.getMAX()!=0) return 0xFF000080;    	
    	num = num/len;
    	if(num>0.8) return 0xFF000080;
    	if(num>0.6) return 0xFF0000C0;
    	if(num>0.4) return 0xFF0000FF;
    	if(num>0.2) return 0xFF6464FF;
    	if(num>=0) return 0xFF8080FF;   	
    	return 0xFF8080FF;
    }
    
    private void DrawResult(Canvas canvas){
    	if(ai.getLevelAI().getTypeAI()==LevelAI.TypeAI.NoAI){
    		DrawResultSingleGame(canvas);
    	}else{
    		DrawResultGameWithAI(canvas);
    	}
    }
    
    private void DrawResultSingleGame(Canvas canvas){
    	int temp=0;
    	int res=0;
    	int h,w;
    	int x=0;
    	int top=0;
    	int left=5;
    	h = number[0].getHeight();
    	w = number[0].getWidth();  
    	top=this.getHeight() - h - 5;
    	
    	left=5 - w;
    	temp = treasury.GetCourse();
    	res = temp/100;
    	temp=temp%100;
    	if(res>=10) temp=9;
    	if(res>0) {x+=w; canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp/10;
    	temp=temp%10; 
    	if(res>0 || x!=0) {x+=w; canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp;   	
    	{x+=w;canvas.drawBitmap(number[res], left + x,top, null);} 

    	x=0;
    	temp = countPlayer;
    	res=0;
    	left=this.getWidth() - 5 - w;
    	res = temp%10;
    	temp=temp/10;
    	{canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp%10;
    	temp=temp/10; 
    	if(res>0 || (res==0 && temp>0)) {x-=w; canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp;   	
    	if(res>0){x-=w;canvas.drawBitmap(number[res], left + x,top, null);}    	
    }
    
    private void DrawResultGameWithAI(Canvas canvas){
    	int temp=0;
    	int res=0;
    	int h,w;
    	int x=0;
    	int top=0;
    	int left=5;
    	h = number[0].getHeight();
    	w = number[0].getWidth();  
    	top=this.getHeight() - h - 5;
    	
    	left=5 - w;
    	temp = countPlayer;
    	res = temp/100;
    	temp=temp%100;
    	if(res>=10) temp=9;
    	if(res>0) {x+=w; canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp/10;
    	temp=temp%10; 
    	if(res>0 || x!=0) {x+=w; canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp;   	
    	{x+=w;canvas.drawBitmap(number[res], left + x,top, null);} 

    	x=0;
    	temp = countComputer;
    	res=0;
    	left=this.getWidth() - 5 - w;
    	res = temp%10;
    	temp=temp/10;
    	{canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp%10;
    	temp=temp/10; 
    	if(res>0 || (res==0 && temp>0)) {x-=w; canvas.drawBitmap(number[res], left + x,top, null);} 
    	res = temp;   	
    	if(res>0){x-=w;canvas.drawBitmap(number[res], left + x,top, null);}    	
    }    
    
    private Bitmap getBitmapText(String str){
    	Bitmap result;
    	Bitmap result2;
    	int Size=40;
        Paint paint = new Paint(); 
        paint.setColor(Color.BLACK);
        paint.setTextSize(Size); 
        Rect bounds = new Rect();
        
        paint.getTextBounds(str, 0, str.length(), bounds);
        //matrix.postScale(1, 1);
        //result = Bitmap.createBitmap(picture[9], 0, 0,SizePicture, SizePicture, matrix, true);
        result = Bitmap.createBitmap(picture[9]);
        Canvas c = new Canvas(result);
        //c.drawRect(0, 0, SizePicture, SizePicture, paint);
        //c.drawText(str, (int)((SizePicture - Size)/2), (int)((SizePicture - Size)/2) + Size, paint);
        c.drawText(str, (int)((SizePicture - bounds.width())/2), (int)((SizePicture - bounds.height())/2) + bounds.height(), paint);
        //matrix.postScale(1, 1);
    	result2 = Bitmap.createBitmap(result, 0, 0,SizePicture, SizePicture, matrix, true);  
    	return result2;
    }
    
    public void NewGame(){
        countPlayer=0;
        countComputer=0;
        save=false;
    	treasury.NewGame();
    	if(mThread!=null) 
    		mThread.setRunning(true);
    	else{
    		mThread = new TreasuryThread(mSurfaceHolder, context, this);
		    mThread.start();  
		} 	
    }
    
    public void NewGame(SizeBoard SCurrent){
        countPlayer=0;
        countComputer=0;
        save=false;
    	treasury.NewGame(SCurrent);
    	if(mThread!=null) 
    		mThread.setRunning(true);
    	else{
    		mThread = new TreasuryThread(mSurfaceHolder, context, this);
		    mThread.start(); 
		}    	
    }
    
    public SizeBoard getSizeBoard(){
    	return treasury.GetSCurrent();
    }
    
    public int getSizeCell(){
    	return SizeCell;
    }
    
    public LevelAI getLevelAI(){
    	return ai.getLevelAI();
    }
    
    public void setLevelAI(LevelAI ai){
    	this.ai.setLevelAI(ai);
    }
    
    public void setSizeCell(int SizeCell){
    	this.SizeCell = SizeCell;
    	
        scale = ((float) SizeCell) / SizePicture;
        matrix = new Matrix();
        matrix.postScale(scale, scale);
        
        scalepicture = new Bitmap[10];
        scalepicture[0] = Bitmap.createBitmap(picture[0], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[1] = Bitmap.createBitmap(picture[1], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[2] = Bitmap.createBitmap(picture[2], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[3] = Bitmap.createBitmap(picture[3], 0, 0,SizePicture, SizePicture, matrix, true); 
        scalepicture[4] = Bitmap.createBitmap(picture[4], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[5] = Bitmap.createBitmap(picture[5], 0, 0,SizePicture, SizePicture, matrix, true);
        scalepicture[6] = Bitmap.createBitmap(picture[6], 0, 0,SizePicture, SizePicture, matrix, true);  
        scalepicture[7] = Bitmap.createBitmap(picture[7], 0, 0,SizePicture, SizePicture, matrix, true);        
        scalepicture[8] = Bitmap.createBitmap(picture[8], 0, 0,SizePicture, SizePicture, matrix, true); 
        scalepicture[9] = Bitmap.createBitmap(picture[9], 0, 0,SizePicture, SizePicture, matrix, true);  
    }
    
	private void saveOption(){
		sPref = ((Activity)context).getPreferences(android.content.Context.MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(SAVED_SIZE, treasury.GetSCurrent().getTypeSize().name());
		ed.putString(SAVED_AI, ai.getLevelAI().getTypeAI().name());
		ed.putInt(SAVED_SCALE, SizeCell);		    
		ed.commit();
    }
	
	private void loadOption(){
		sPref = ((Activity)context).getPreferences(android.content.Context.MODE_PRIVATE);
		
		SizeBoard.TypeSize tsize;
		try{
	      tsize = SizeBoard.TypeSize.valueOf(sPref.getString(SAVED_SIZE, SizeBoard.TypeSize.Beginner.name()));
		}catch(IllegalArgumentException ex){
		  tsize = SizeBoard.TypeSize.Beginner;
		}
		
		LevelAI.TypeAI tai;
		try{
		  tai = LevelAI.TypeAI.valueOf(sPref.getString(SAVED_AI, LevelAI.TypeAI.NoAI.name()));	
		}catch(IllegalArgumentException ex){
		  tai = LevelAI.TypeAI.NoAI;	
		}
		
        if(status == Status.FREE){
  		  tai = LevelAI.TypeAI.NoAI;	        
        }	
        
        treasury = new Treasury(new SizeBoard(tsize));  
        ai = new AI(treasury, new LevelAI(tai));
		
		SizeCell = sPref.getInt(SAVED_SCALE, 48);
		if(SizeCell<MinSizeCell) SizeCell=MinSizeCell;
		if(SizeCell>MaxSizeCell) SizeCell=MaxSizeCell;
	}
}
