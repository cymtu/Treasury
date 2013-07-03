package com.example.treasury;

import android.content.*;
import android.graphics.*;
import android.view.*;

public class TreasuryThread extends Thread{
    
    private SurfaceHolder mSurfaceHolder;
    private boolean mRunning;
    private TreasuryView viewtreasury;
    

    public TreasuryThread(SurfaceHolder surfaceHolder, Context context, TreasuryView viewtreasury){
        mSurfaceHolder = surfaceHolder;
        this.viewtreasury = viewtreasury;
        mRunning = false; 
    }

    public void setRunning(boolean running){
        mRunning = running;
    }

    @Override
    public void run(){
        Canvas canvas = null;
        while (mRunning){
            try{
                canvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder){
                	viewtreasury.onDraw(canvas);
                }
            }
            catch (Exception e) { }
            finally{
                if (canvas != null){
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    
    public void initPositions(int screenHeight, int screenWidth){
            
    }
   
}
