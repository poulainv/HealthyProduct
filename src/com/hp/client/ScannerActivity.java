package com.hp.client;

import java.io.IOException;
import java.util.Collection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.DecodeFormatManager;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.IntentSource;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.hp.interfaces.SimpleGestureFilter;

public class ScannerActivity extends Activity implements SurfaceHolder.Callback, com.hp.interfaces.SimpleGestureFilter.SimpleGestureListener {

	private final String TAG = "ScannerActivity";
	private ProductChecker prod;
	private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
	
	private SimpleGestureFilter detector; 

	private static final String PACKAGE_NAME = "com.google.zxing.client.android";
	private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
	private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
	private static final String[] ZXING_URLS = {
			"http://zxing.appspot.com/scan", "zxing://scan/" };

	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private View resultView;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private String sourceUrl;
	private Collection<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private ImageView mImage = null;
	private SharedPreferences mPrefs;
	Context mContext;

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.barcodecapture);

		
		mContext = this.getBaseContext();
		firstRunPreferences();
		if(getFirstRun()){
			mImage = (ImageView) this.findViewById(R.id.help_scan);
			mImage.setVisibility(View.VISIBLE);
			
			SharedPreferences.Editor edit = mPrefs.edit();
			edit.putBoolean("firstRunScan", false);
			edit.commit();
		}
		
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		detector = new SimpleGestureFilter(this,this);
		// setContentView(R.layout.scanner);
		//
		// Button b = (Button) this.findViewById(R.id.skip_button);
		//
		// // bouton � remplacer par le scanner.
		// // Quand le scan r�ussit, il faut lancer l'AsyncTask comme ci
		// dessous, en lui passant le code EAN
		// b.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		//
		// prod = new ProductChecker(ScannerActivity.this, "3017620424403");
		// prod.execute();
		//
		// }
		// });
		//
		// // Ancienne m�thode, normalement il faut utiliser les Fragments, mais
		// pas utile dans notre cas
		// @SuppressWarnings("deprecation")
		// ProductChecker lastConfiguration = (ProductChecker)
		// this.getLastNonConfigurationInstance();
		// if (lastConfiguration != null) {
		// prod = lastConfiguration;
		// prod.setActivity(this);
		// }
	}
	
	public boolean getFirstRun() {
        return mPrefs.getBoolean("firstRunScan", true);
     }

	 public void firstRunPreferences() {

	        mPrefs = mContext.getSharedPreferences("healthyProductsPrefs", 0); //0 = mode priv�.
	     }
	@Override
	protected void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		resultView = findViewById(R.id.result_view);
		statusView = (TextView) findViewById(R.id.status_view);

		handler = null;
		lastResult = null;

		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		beepManager.updatePrefs();

		inactivityTimer.onResume();

		Intent intent = getIntent();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// copyToClipboard =
		// prefs.getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true)
		// && (intent == null ||
		// intent.getBooleanExtra(Intents.Scan.SAVE_HISTORY, true));

		source = IntentSource.NONE;
		decodeFormats = null;
		characterSet = null;

		if (intent != null) {

			String action = intent.getAction();
			String dataString = intent.getDataString();

			if (Intents.Scan.ACTION.equals(action)) {

				// Scan the formats the intent requested, and return the result
				// to the calling activity.
				source = IntentSource.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);

				if (intent.hasExtra(Intents.Scan.WIDTH)
						&& intent.hasExtra(Intents.Scan.HEIGHT)) {
					int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
					int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
					if (width > 0 && height > 0) {
						cameraManager.setManualFramingRect(width, height);
					}
				}

				String customPromptMessage = intent
						.getStringExtra(Intents.Scan.PROMPT_MESSAGE);
				if (customPromptMessage != null) {
					statusView.setText(customPromptMessage);
				}

			} else if (dataString != null
					&& dataString.contains(PRODUCT_SEARCH_URL_PREFIX)
					&& dataString.contains(PRODUCT_SEARCH_URL_SUFFIX)) {

				// Scan only products and send the result to mobile Product
				// Search.
				source = IntentSource.PRODUCT_SEARCH_LINK;
				sourceUrl = dataString;
				decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;

			} else if (isZXingURL(dataString)) {

				// Scan formats requested in query string (all formats if none
				// specified).
				// If a return URL is specified, send the results there.
				// Otherwise, handle it ourselves.
				source = IntentSource.ZXING_LINK;
				sourceUrl = dataString;
				Uri inputUri = Uri.parse(sourceUrl);
				decodeFormats = DecodeFormatManager
						.parseDecodeFormats(inputUri);

			}

			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);

		}
	}

	@Override
	  protected void onPause() {
	    if (handler != null) {
	      handler.quitSynchronously();
	      handler = null;
	    }
	    inactivityTimer.onPause();
	    cameraManager.closeDriver();
	    if (!hasSurface) {
	      SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
	      SurfaceHolder surfaceHolder = surfaceView.getHolder();
	      surfaceHolder.removeCallback(this);
	    }
	    super.onPause();
	  }
	
	@Override
	  protected void onDestroy() {
	    inactivityTimer.shutdown();
	    super.onDestroy();
	  }
	
	
	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		// ResultHandler resultHandler =
		// ResultHandlerFactory.makeResultHandler(this, rawResult);
		// historyManager.addHistoryItem(rawResult, resultHandler);

		if (barcode == null) {
			// This is from history -- no saved barcode
			// handleDecodeInternally(rawResult, resultHandler, null);
		} else {
			beepManager.playBeepSoundAndVibrate();
			// drawResultPoints(barcode, rawResult);
			// Nouvel intent ici
			System.out.println(lastResult.getText());
			prod = new ProductChecker(ScannerActivity.this, rawResult.getText());//"3017620424403");
			prod.execute();

			// handleDecodeExternally(rawResult, resultHandler, barcode);
		}
	}

	// Remember the information when the screen is just about to be rotated.
	// This information can be retrieved by using
	// getLastNonConfigurationInstance()
	public Object onRetainNonConfigurationInstance() {

		return prod;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}


	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	private void resetStatusView() {
		//resultView.setVisibility(View.GONE);

		statusView.setText(R.string.msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		RotateAnimation ranim_text = (RotateAnimation) AnimationUtils
				.loadAnimation(this, R.anim.animtext);
		ranim_text.setFillAfter(true);
		statusView.setAnimation(ranim_text);
			
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						characterSet, cameraManager);
			}
			// decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private static boolean isZXingURL(String dataString) {
		if (dataString == null) {
			return false;
		}
		for (String url : ZXING_URLS) {
			if (dataString.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	 @Override 
	 public boolean dispatchTouchEvent(MotionEvent me){ 
	   this.detector.onTouchEvent(me);
	  return super.dispatchTouchEvent(me); 
	 }
	 
	public void onSwipe(int direction) {
		  
		  switch (direction) {
		  
		  case SimpleGestureFilter.SWIPE_DOWN :  
			  Intent OptionsIntent = new Intent(ScannerActivity.this,OptionsActivity.class);
			startActivityForResult(OptionsIntent, 123);
		                                                 break;
		  case SimpleGestureFilter.SWIPE_UP :   
		  Intent i2 = new Intent(ScannerActivity.this,HistoryActivity.class);
			ScannerActivity.this.startActivity(i2);
		                                                 break;  
		  case SimpleGestureFilter.SINGLE_TAP :  
			 if(mImage != null)
				 mImage.setVisibility(View.INVISIBLE);
		                                                 break;
		  } 
	}

	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}

}
