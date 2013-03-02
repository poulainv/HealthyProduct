package com.hp.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 
 * Point d'entrée de l'application.
 * Vérifie si l'application a déjà été lancée, et démarre soit l'aide, soit le 
 * 
 * @author Lisional
 *
 */

public class SplashScreenActivity extends Activity {

	private static final int FIRST_USE = 1;
	private static final int NORMAL_START = 2;
	private SharedPreferences mPrefs;
	private static final String TAG = "SplashScreen";
	Context mContext;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"->onCreate");
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        // La on lancera le thread de chargement de la liste des param enregistrés
        setContentView(R.layout.splashscreen);

        mContext = this.getBaseContext();
        firstRunPreferences();
        
		Initializer m = new Initializer();
		m.start();
		Log.d(TAG,"<-onCreate");
    }

	// Refresh handler, necessary for updating the UI in a/the thread
	Handler hRefresh = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case FIRST_USE:
				Intent i1 = new Intent(SplashScreenActivity.this, FirstUseActivity.class);					
                startActivity(i1);
                SplashScreenActivity.this.finish();
				break;
			case NORMAL_START:
				Intent i2 = new Intent(SplashScreenActivity.this, ScannerActivity.class);
                startActivity(i2);
                SplashScreenActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};
	
	public class Initializer extends Thread {

	    public Initializer() {}

	    public void run() {				
			try{
				Thread.sleep(1000);
				Log.d(TAG,"->run Initializer");
				if(!getFirstRun())
					hRefresh.sendEmptyMessage(NORMAL_START);
				else
					hRefresh.sendEmptyMessage(FIRST_USE);

			}catch(Exception e){
				Log.e(TAG,"Error:"+e.toString());
			}
	    }   
	}
	
	/**
     * get if this is the first run
     * @return returns true, if this is the first run
     */
        public boolean getFirstRun() {
        return mPrefs.getBoolean("firstRun", true);
     }
     
     
     /**
     * setting up preferences storage
     */
     public void firstRunPreferences() {

        mPrefs = mContext.getSharedPreferences("healthyProductsPrefs", 0); //0 = mode privé.
     }
}
