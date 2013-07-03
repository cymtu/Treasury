package com.example.treasury;


import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TreasuryActivity extends Activity {
	
	private TreasuryView treasuryview;
	private Dialog currentDialog; 
	private AtomicBoolean dialogIsVisible = new AtomicBoolean();
	final int MIN_SIZE = 9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasury);
    	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        treasuryview = (TreasuryView) findViewById(R.id.game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_treasury, menu);
        return true;
    }
    
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    	this.setTitle("LAND");
        // определяем текущую ориентаци и что-то делаем
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        	this.setTitle("LAND");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   
        	this.setTitle("PORT");
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override 
    protected void onDestroy(){
       super.onDestroy();
    } 
    
    @Override public void onPause()
    {
       super.onPause(); 
       treasuryview.stopGame(); 
    } 
    
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Log.d(LOG_TAG, "onRestoreInstanceState");
      }

      protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.d(LOG_TAG, "onSaveInstanceState");
      }
    
    public void setNamePlayer(String name){
        treasuryview.setNamePlayer(name); 
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch (item.getItemId()) {
          case R.id.menu_newgame:
              treasuryview.NewGame();
              return true; 
          case R.id.menu_records:
        	  showRecordsDialog();
              return true; 
          case R.id.menu_settings:
             showSettingDialog();
             return true;
         case R.id.menu_scale:
        	 showScaleDialog();
        	 return true; 
       } 
       
       return super.onOptionsItemSelected(item); 
    } 
    
    private void showRecordsDialog(){
        currentDialog = new Dialog(this);
        currentDialog.setContentView(R.layout.records);
        currentDialog.setTitle(R.string.title_records_dialog);
        currentDialog.setCancelable(true);
        
        Button bOk = (Button) currentDialog.findViewById(R.id.butOk);
        Button bReset = (Button) currentDialog.findViewById(R.id.butReset);      
        Button bSizePrev = (Button) currentDialog.findViewById(R.id.butSizePrev);
        Button bSizeNext = (Button) currentDialog.findViewById(R.id.butSizeNext);
        Button bAiPrev = (Button) currentDialog.findViewById(R.id.butAiPrev);
        Button bAiNext = (Button) currentDialog.findViewById(R.id.butAiNext); 
        
        final TextView tfiltr = (TextView) currentDialog.findViewById(R.id.tfiltr);
        
        final LevelAI ai = new LevelAI(treasuryview.getLevelAI().getTypeAI());
        final SizeBoard size = new SizeBoard(treasuryview.getSizeBoard().getTypeSize());
        updateRecords(size, ai);
        
        if(TreasuryView.status == TreasuryView.Status.FREE){
    	    bAiPrev.setVisibility(View.GONE);
    	    bAiNext.setVisibility(View.GONE);
    	    ai.setLevelAI(LevelAI.TypeAI.NoAI);
         }
        
        bSizePrev.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(size.getTypeSize() == SizeBoard.TypeSize.Beginner) size.setTypeSize(SizeBoard.TypeSize.Beginner);
                if(size.getTypeSize() == SizeBoard.TypeSize.Beginner2) size.setTypeSize(SizeBoard.TypeSize.Beginner);
                if(size.getTypeSize() == SizeBoard.TypeSize.Beginner3) size.setTypeSize(SizeBoard.TypeSize.Beginner2);  
                if(size.getTypeSize() == SizeBoard.TypeSize.Expert) size.setTypeSize(SizeBoard.TypeSize.Beginner3);
                if(size.getTypeSize() == SizeBoard.TypeSize.Expert2) size.setTypeSize(SizeBoard.TypeSize.Expert);
                if(size.getTypeSize() == SizeBoard.TypeSize.Expert3) size.setTypeSize(SizeBoard.TypeSize.Expert2);   
                if(size.getTypeSize() == SizeBoard.TypeSize.Professional) size.setTypeSize(SizeBoard.TypeSize.Expert3);
                if(size.getTypeSize() == SizeBoard.TypeSize.Professional2) size.setTypeSize(SizeBoard.TypeSize.Professional);
                if(size.getTypeSize() == SizeBoard.TypeSize.Professional3) size.setTypeSize(SizeBoard.TypeSize.Professional2);  
                
                updateRecords(size, ai);
            }
          });    
        
        bSizeNext.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
            	if(size.getTypeSize() == SizeBoard.TypeSize.Professional3) size.setTypeSize(SizeBoard.TypeSize.Professional3); 
                if(size.getTypeSize() == SizeBoard.TypeSize.Professional2) size.setTypeSize(SizeBoard.TypeSize.Professional3);
                if(size.getTypeSize() == SizeBoard.TypeSize.Professional) size.setTypeSize(SizeBoard.TypeSize.Professional2);
                if(size.getTypeSize() == SizeBoard.TypeSize.Expert3) size.setTypeSize(SizeBoard.TypeSize.Professional);  
                if(size.getTypeSize() == SizeBoard.TypeSize.Expert2) size.setTypeSize(SizeBoard.TypeSize.Expert3);  
                if(size.getTypeSize() == SizeBoard.TypeSize.Expert) size.setTypeSize(SizeBoard.TypeSize.Expert2);
                if(size.getTypeSize() == SizeBoard.TypeSize.Beginner3) size.setTypeSize(SizeBoard.TypeSize.Expert);           
                if(size.getTypeSize() == SizeBoard.TypeSize.Beginner2) size.setTypeSize(SizeBoard.TypeSize.Beginner3);
                if(size.getTypeSize() == SizeBoard.TypeSize.Beginner) size.setTypeSize(SizeBoard.TypeSize.Beginner2);               
                
                updateRecords(size, ai);
            }
          }); 
        
        bAiPrev.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ai.getTypeAI() == LevelAI.TypeAI.NoAI) ai.setLevelAI(LevelAI.TypeAI.NoAI);
                if(ai.getTypeAI() == LevelAI.TypeAI.Beginner) ai.setLevelAI(LevelAI.TypeAI.NoAI);
                if(ai.getTypeAI() == LevelAI.TypeAI.Expert) ai.setLevelAI(LevelAI.TypeAI.Beginner);  
                if(ai.getTypeAI() == LevelAI.TypeAI.Professional) ai.setLevelAI(LevelAI.TypeAI.Expert);                  
                
                updateRecords(size, ai);
            }
          });    
        
        bAiNext.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(ai.getTypeAI() == LevelAI.TypeAI.Professional) ai.setLevelAI(LevelAI.TypeAI.Professional); 
                if(ai.getTypeAI() == LevelAI.TypeAI.Expert) ai.setLevelAI(LevelAI.TypeAI.Professional);   
                if(ai.getTypeAI() == LevelAI.TypeAI.Beginner) ai.setLevelAI(LevelAI.TypeAI.Expert);   
                if(ai.getTypeAI() == LevelAI.TypeAI.NoAI) ai.setLevelAI(LevelAI.TypeAI.Beginner);   
                
                updateRecords(size, ai);
            }
          });       
        
        bReset.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
            	treasuryview.restBase();
            }
          });
        
        bOk.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                dialogIsVisible.set(false); 
                currentDialog.dismiss(); 
                currentDialog = null; 
            }
          });
        
        currentDialog.show(); // show the dialog
    }
    
    
    private void updateRecords(SizeBoard size,LevelAI ai) {
		// TODO Auto-generated method stub
    	String str="";
        final TextView tfiltr = (TextView) currentDialog.findViewById(R.id.tfiltr);
        final TextView trec1 = (TextView) currentDialog.findViewById(R.id.trec1);
        final TextView trec2 = (TextView) currentDialog.findViewById(R.id.trec2);
        final TextView trec3 = (TextView) currentDialog.findViewById(R.id.trec3);
        final TextView trec4 = (TextView) currentDialog.findViewById(R.id.trec4);
        final TextView trec5 = (TextView) currentDialog.findViewById(R.id.trec5);       

        if(size.getTypeSize() == SizeBoard.TypeSize.Beginner) str = getResources().getString(R.string.s9x9);
        if(size.getTypeSize() == SizeBoard.TypeSize.Beginner2) str = getResources().getString(R.string.s9x18);
        if(size.getTypeSize() == SizeBoard.TypeSize.Beginner3) str = getResources().getString(R.string.s14x18);
        if(size.getTypeSize() == SizeBoard.TypeSize.Expert) str = getResources().getString(R.string.s13x13);
        if(size.getTypeSize() == SizeBoard.TypeSize.Expert2) str = getResources().getString(R.string.s13x26);
        if(size.getTypeSize() == SizeBoard.TypeSize.Expert3) str = getResources().getString(R.string.s20x26);
        if(size.getTypeSize() == SizeBoard.TypeSize.Professional) str = getResources().getString(R.string.s18x18);
        if(size.getTypeSize() == SizeBoard.TypeSize.Professional2) str = getResources().getString(R.string.s18x36);
        if(size.getTypeSize() == SizeBoard.TypeSize.Professional3) str = getResources().getString(R.string.s27x36);
        
        if(TreasuryView.status != TreasuryView.Status.FREE){
          if(ai.getTypeAI() == LevelAI.TypeAI.NoAI) str =str + " & " + getResources().getString(R.string.Single);
          if(ai.getTypeAI() == LevelAI.TypeAI.Beginner) str =str + " & " + getResources().getString(R.string.Beginner);
          if(ai.getTypeAI() == LevelAI.TypeAI.Expert) str =str + " & " + getResources().getString(R.string.Expert);
          if(ai.getTypeAI() == LevelAI.TypeAI.Professional) str =str + " & " + getResources().getString(R.string.Professional); 
        }
        
        tfiltr.setText(str);
        
        trec1.setText("1. ");
        trec2.setText("2. ");
        trec3.setText("3. ");
        trec4.setText("4. ");
        trec5.setText("5. ");
        
    	SQLiteDatabase db = treasuryview.getdb();
    	
        Cursor c = db.rawQuery("SELECT * FROM " + DbOpenHelper.TABLE_NAME
        		+ " WHERE " 
        		+ DbOpenHelper.AI + " = ?" + " AND "
        		+ DbOpenHelper.SIZE + " = ?"
        		+ " ORDER BY " + DbOpenHelper.COURSE + " ASC, " + DbOpenHelper.WINNER_COUNT + " DESC, " + DbOpenHelper.DATE_VICTORY + " ASC"
        		+ " LIMIT " + DbOpenHelper.MAX_RECORDS_COUNT, new String[]{ai.getTypeAI().toString(), size.getTypeSize().toString()});
        
        int n=0;
        if(c.moveToFirst()){
           do{
        	   n++;
               str=Integer.toString(n) + ". " + format(c.getInt(3)) + " " + format(c.getInt(5)) + " "  + c.getString(4);
               if(n==1)trec1.setText(str);
               if(n==2)trec2.setText(str);
               if(n==3)trec3.setText(str);
               if(n==4)trec4.setText(str);
               if(n==5)trec5.setText(str);        
           }while(c.moveToNext());
        }
        
        c.close();
    	
	}

	private void showSettingDialog(){
        currentDialog = new Dialog(this);
        currentDialog.setContentView(R.layout.dialog);
        currentDialog.setTitle(R.string.title_dialog);
        currentDialog.setCancelable(true);  
        
        LevelAI.TypeAI ai = treasuryview.getLevelAI().getTypeAI();
        SizeBoard.TypeSize size = treasuryview.getSizeBoard().getTypeSize();

        final RadioButton b1 = (RadioButton) currentDialog.findViewById(R.id.radioButton1);
        final RadioButton b2 = (RadioButton) currentDialog.findViewById(R.id.radioButton2);
        final RadioButton b3 = (RadioButton) currentDialog.findViewById(R.id.radioButton3);
        final RadioButton b4 = (RadioButton) currentDialog.findViewById(R.id.radioButton4); 
        final RadioButton b5 = (RadioButton) currentDialog.findViewById(R.id.radioButton5);
        final RadioButton b6 = (RadioButton) currentDialog.findViewById(R.id.radioButton6);
        final RadioButton b7 = (RadioButton) currentDialog.findViewById(R.id.radioButton7);
        final RadioButton b8 = (RadioButton) currentDialog.findViewById(R.id.radioButton8);     
        final RadioButton b9 = (RadioButton) currentDialog.findViewById(R.id.radioButton9);         
        
        final RadioButton bSingl = (RadioButton) currentDialog.findViewById(R.id.radioButton10);
        final RadioButton bBeg = (RadioButton) currentDialog.findViewById(R.id.radioButton11);
        final RadioButton bExp = (RadioButton) currentDialog.findViewById(R.id.radioButton12);
        final RadioButton bProf = (RadioButton) currentDialog.findViewById(R.id.radioButton13);

        if(size == SizeBoard.TypeSize.Beginner) b1.setChecked(true);
        if(size == SizeBoard.TypeSize.Beginner2) b2.setChecked(true);
        if(size == SizeBoard.TypeSize.Beginner3) b3.setChecked(true);  
        if(size == SizeBoard.TypeSize.Expert) b4.setChecked(true);
        if(size == SizeBoard.TypeSize.Expert2) b5.setChecked(true);
        if(size == SizeBoard.TypeSize.Expert3) b6.setChecked(true);   
        if(size == SizeBoard.TypeSize.Professional) b7.setChecked(true);
        if(size == SizeBoard.TypeSize.Professional2) b8.setChecked(true);
        if(size == SizeBoard.TypeSize.Professional3) b9.setChecked(true);          
        
        if(ai == LevelAI.TypeAI.NoAI) bSingl.setChecked(true);
        if(ai == LevelAI.TypeAI.Beginner) bBeg.setChecked(true);
        if(ai == LevelAI.TypeAI.Expert) bExp.setChecked(true);
        if(ai == LevelAI.TypeAI.Professional) bProf.setChecked(true);        
       
        currentDialog.show(); // show the dialog
        
        Button setScaleButton = (Button) currentDialog.findViewById(R.id.setOKButton);
        setScaleButton.setOnClickListener(new OnClickListener() {
           @Override public void onClick(View v) {
               dialogIsVisible.set(false); 
               currentDialog.dismiss(); 
               currentDialog = null; 
               
               if(b1.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Beginner));    
               if(b2.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Beginner2)); 
               if(b3.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Beginner3));                    
               if(b4.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Expert));  
               if(b5.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Expert2)); 
               if(b6.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Expert3));                    
               if(b7.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Professional));  
               if(b8.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Professional2)); 
               if(b9.isChecked()) treasuryview.NewGame(new SizeBoard(SizeBoard.TypeSize.Professional3)); 
               
               if(bSingl.isChecked()) treasuryview.setLevelAI(new LevelAI(LevelAI.TypeAI.NoAI));
               if(bBeg.isChecked()) treasuryview.setLevelAI(new LevelAI(LevelAI.TypeAI.Beginner));
               if(bExp.isChecked()) treasuryview.setLevelAI(new LevelAI(LevelAI.TypeAI.Expert));
               if(bProf.isChecked()) treasuryview.setLevelAI(new LevelAI(LevelAI.TypeAI.Professional));              
           }
        });
        
        OnClickListener ai_radio_listener = new OnClickListener (){	
        	public void onClick(View v) {
        	    RadioButton rb = (RadioButton) v;
        	    bSingl.setChecked(false);
        	    bBeg.setChecked(false);
        	    bExp.setChecked(false);
        	    bProf.setChecked(false);      
        	    rb.setChecked(true);
        	}
        };
        
        bSingl.setOnClickListener(ai_radio_listener);
        bBeg.setOnClickListener(ai_radio_listener);       
        bExp.setOnClickListener(ai_radio_listener);
        bProf.setOnClickListener(ai_radio_listener);
        
        OnClickListener size_radio_listener = new OnClickListener (){ 
        	public void onClick(View v) {
        	    RadioButton rb = (RadioButton) v;
        	    b1.setChecked(false);
        	    b2.setChecked(false);
        	    b3.setChecked(false);
        	    b4.setChecked(false);  
        	    b5.setChecked(false);
        	    b6.setChecked(false);
        	    b7.setChecked(false);
        	    b8.setChecked(false);    
        	    b9.setChecked(false);           	    
        	    rb.setChecked(true);
        	}
        };  
        
        b1.setOnClickListener(size_radio_listener);
        b2.setOnClickListener(size_radio_listener);       
        b3.setOnClickListener(size_radio_listener);
        b4.setOnClickListener(size_radio_listener);
        b5.setOnClickListener(size_radio_listener);
        b6.setOnClickListener(size_radio_listener);       
        b7.setOnClickListener(size_radio_listener);
        b8.setOnClickListener(size_radio_listener);   
        b9.setOnClickListener(size_radio_listener);  
        
        if(TreasuryView.status == TreasuryView.Status.FREE){
        	TextView text = (TextView) currentDialog.findViewById(R.id.textView2); 
        	text.setVisibility(View.GONE);
    	    bSingl.setVisibility(View.GONE);
    	    bBeg.setVisibility(View.GONE);
    	    bExp.setVisibility(View.GONE);
    	    bProf.setVisibility(View.GONE);
         }
    }
    
    private void showScaleDialog(){
        currentDialog = new Dialog(this);
        currentDialog.setContentView(R.layout.scale_dialog);
        currentDialog.setTitle(R.string.title_scale_dialog);
        currentDialog.setCancelable(true);
        
 	    final TextView tscale = (TextView) currentDialog.findViewById(R.id.tscale);

        
        final SeekBar scaleSeekBar = 
     	         (SeekBar) currentDialog.findViewById(R.id.scaleSeekBar);    
     	      
 	    scaleSeekBar.setMax(TreasuryView.MaxSizeCell - TreasuryView.MinSizeCell);    	   
        scaleSeekBar.setProgress(treasuryview.getSizeCell() - TreasuryView.MinSizeCell);

        scaleSeekBar.setOnSeekBarChangeListener(scaleSeekBarChanged);
        
 	    tscale.setText(format(scaleSeekBar.getProgress() + TreasuryView.MinSizeCell));

        
        Button setScaleButton = (Button) currentDialog.findViewById(
     	         R.id.setScaleButton);
        setScaleButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                
                dialogIsVisible.set(false); 
                currentDialog.dismiss(); 
                currentDialog = null; 
            }
          });
        
        currentDialog.show(); // show the dialog
    }
    
    private OnSeekBarChangeListener scaleSeekBarChanged = new OnSeekBarChangeListener() {
    	@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    	     final SeekBar scaleSeekBar = (SeekBar) currentDialog.findViewById(R.id.scaleSeekBar);  
    	         
    	     final TextView tscale = (TextView) currentDialog.findViewById(R.id.tscale);   	   

    	     tscale.setText(format(scaleSeekBar.getProgress() + TreasuryView.MinSizeCell));
    	    	   
    	     treasuryview.setSizeCell(scaleSeekBar.getProgress() + TreasuryView.MinSizeCell);
    	} 
    	       
    	@Override public void onStartTrackingTouch(SeekBar seekBar) {} 

    	@Override public void onStopTrackingTouch(SeekBar seekBar) {} 
    }; 
    
    private String format(int number){
 	   if(number<10) return "00" + Integer.toString(number);
 	   if(number<100) 
 		   return "0" + Integer.toString(number);
 	   else
 		   return Integer.toString(number);
    }
}
