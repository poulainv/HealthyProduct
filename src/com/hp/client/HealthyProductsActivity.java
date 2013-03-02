package com.hp.client;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.hp.client.dao.DAO;
import com.hp.client.fragments.FragmentComplement;
import com.hp.client.fragments.FragmentMain;
import com.hp.client.fragments.FragmentSocial;
import com.hp.client.resource.Constants;
import com.hp.client.resource.Information;
import com.hp.client.resource.Ingredient;
import com.hp.client.resource.Label;
import com.hp.client.resource.Produit;
import com.hp.client.resource.User;

public class HealthyProductsActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, SensorEventListener {

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, HealthyProductsActivity.TabInfo>();
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private SensorManager mySensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private HashMap<Integer, Information> informationMap = new HashMap<Integer, Information>();
	private SharedPreferences mPrefs;
	private final String TAG = "HealthyProductsActivity";

	private class TabInfo {
		private String tag;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
		}
	}

	private User user;
	private Produit currentProduct;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "->onCreate");

		final Bundle b = this.getIntent().getBundleExtra("bundleProduit");
		currentProduct = b.getParcelable("produit");

		switch (b.getInt("nextContentView")) {
		case Constants.PRODUCT_OK:

			user = b.getParcelable("user");
			setContentView(R.layout.mainviewpager);
			this.initialiseTabHost(savedInstanceState);
			// set the tab as per the saved state
			if (savedInstanceState != null)
				mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));

			this.initialiseViewPager();

			// On affiche le Fragment du milieu au début
			this.mViewPager.setCurrentItem(1);
			this.mTabHost.setCurrentTab(1);
			break;
		case Constants.ERROR_PRODUCT_FORBIDDEN:
			setContentView(R.layout.error_ingredient_forbidden);
			ArrayList<Ingredient> list = b.getParcelableArrayList("listIngredientInterdits");
			TextView explanation = (TextView) findViewById(R.id.explanation);

			StringWriter message = new StringWriter();

			message.append("Ce produit contient:\n");

			for (int i = 0; i < list.size(); ++i) {
				message.append(list.get(i).getNom() + "\n");
			}
			explanation.setText(message.toString());

			Button back = (Button) findViewById(R.id.forbiddent_but_back);
			back.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					HealthyProductsActivity.this.finish();
				}
			});

			Button forward = (Button) findViewById(R.id.forbiddent_but_forward);
			forward.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.i(TAG, "Produit OK");
					b.putInt("nextContentView", Constants.PRODUCT_OK);
					b.putParcelable("produit", b.getParcelable("produit"));
					b.putParcelable("user", b.getParcelable("user"));
					Intent i2 = new Intent(HealthyProductsActivity.this, HealthyProductsActivity.class);
					i2.putExtra("bundleProduit", b);

					startActivity(i2);
					HealthyProductsActivity.this.finish();
				}
			});

			break;
		case Constants.ERROR_PRODUCT_NOT_FOUND:

			setContentView(R.layout.error_product_not_found);
			Button back2 = (Button) findViewById(R.id.forbiddent_but_back);
			back2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					HealthyProductsActivity.this.finish();
				}
			});
			break;

		}
		mPrefs = this.getSharedPreferences("healthyProductsPrefs", 0);
		if(getFirstRun()){
			AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(HealthyProductsActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
			adb.setTitle("Informations");
			TextView e = new TextView(this);
			e.setText(R.string.explanations_results_screen);
			adb.setView(e);

			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setRunned();
				}
			});
			adb.show();
		}
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
		mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		mySensorManager.registerListener(this, mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

		Log.i(TAG, "<-onCreate");
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (user != null) {
			// On le dira pas
			DAO dao = DAO.getInstance(this.getApplicationContext());
			dao.open();
			if (dao.getUser(1).getTypeconso() != user.getTypeconso()) {
				user = dao.getUser(1);
				this.initialiseViewPager();
			}
			dao.close();
		}

		mySensorManager.registerListener(this, mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mySensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		Log.d(TAG, "->onCreateOptionsMenu");

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		MenuItem profil = menu.findItem(R.id.options);
		profil.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				Intent OptionsIntent = new Intent(HealthyProductsActivity.this, OptionsActivity.class);
				startActivity(OptionsIntent);
				return true;
			}
		});

		MenuItem historic = menu.findItem(R.id.historique);
		historic.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				Intent OptionsIntent = new Intent(HealthyProductsActivity.this, HistoryActivity.class);
				startActivity(OptionsIntent);
				return true;
			}
		});

		MenuItem newscan = menu.findItem(R.id.newscan);
		newscan.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			// TODO trouver une solution pour ne pas relancer un scanner a chaque fois...
			public boolean onMenuItemClick(MenuItem item) {
				Intent OptionsIntent = new Intent(HealthyProductsActivity.this, ScannerActivity.class);
				startActivityIfNeeded(OptionsIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
				return true;
			}
		});
		Log.d(TAG, "<-onCreateOptionsMenu");
		return result;
	}

	/**
	 * <<<<<<< .mine Instancie les Fragments et les assemble dans le ViewPager Se charge de passer
	 * aux Fragments les infos/drawable ï¿½ afficher ======= Instancie les Fragments et les assemble
	 * dans le ViewPager Se charge de passer aux Fragments les infos/drawable ï¿½ afficher >>>>>>>
	 * .r21
	 */
	private void initialiseViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();

		Bundle b = new Bundle();
		Bundle bForComplement = new Bundle();
		Bundle bForSocial = new Bundle();
		ArrayList<Information> listInfosToMain = new ArrayList<Information>();
		ArrayList<Information> listInfosToDetails = new ArrayList<Information>();

		// on initialise l'informationMap avec toutes les infos
		this.initialiseInformationMap(currentProduct);

		switch (user.getTypeconso()) {
		case User.TYPE_ECOFRIEND:
			listInfosToMain.add(informationMap.remove(Information.INFO_KM_PARCOURU));
			listInfosToMain.add(informationMap.remove(Information.INFO_LABEL_BIO));
			listInfosToMain.add(informationMap.remove(Information.INFO_OGM));
			break;
		case User.TYPE_ECONOME:
			listInfosToMain.add(informationMap.remove(Information.INFO_PRIX_MOYEN));
			listInfosToMain.add(informationMap.remove(Information.INFO_KCAL));
			listInfosToMain.add(informationMap.remove(Information.INFO_LABEL_BIO));
			break;
		case User.TYPE_FATWATCHER:
			listInfosToMain.add(informationMap.remove(Information.INFO_KCAL));
			listInfosToMain.add(informationMap.remove(Information.INFO_LIPIDE));
			listInfosToMain.add(informationMap.remove(Information.INFO_GLUCIDE));
			break;
		case User.TYPE_QUALITE:
			listInfosToMain.add(informationMap.remove(Information.INFO_INGREDIENT_NON_RECOMMANDE));
			listInfosToMain.add(informationMap.remove(Information.INFO_LABEL_BIO));
			listInfosToMain.add(informationMap.remove(Information.INFO_OGM));
			break;
		default:
			Log.e(TAG, "Le type de l'utilisateur n'est pas reconnu");

		}

		b.putParcelableArrayList("infoToShowMain", listInfosToMain);
		bForSocial.putCharSequence("nameProduct", currentProduct.getNom());
		b.putCharSequence("nameProduct", currentProduct.getNom());
		bForSocial.putInt("idProduct", currentProduct.getGuid());

		// On recupere les information non affichees pour le main
		listInfosToDetails.addAll(informationMap.values());
		bForComplement.putParcelableArrayList("infoToShowComplement", listInfosToDetails);
		bForComplement.putParcelableArrayList("ingredientList", (ArrayList<Ingredient>) currentProduct.getIngredientList());
		bForComplement.putParcelableArrayList("labelList", (ArrayList<Label>) currentProduct.getLabelList());
		b.putParcelableArrayList("ingredientList", (ArrayList<Ingredient>) currentProduct.getIngredientList());
		b.putParcelableArrayList("labelList", (ArrayList<Label>) currentProduct.getLabelList());

		fragments.add(Fragment.instantiate(this, FragmentSocial.class.getName(), bForSocial));
		fragments.add(Fragment.instantiate(this, FragmentMain.class.getName(), b));
		fragments.add(Fragment.instantiate(this, FragmentComplement.class.getName(), bForComplement));
		this.mPagerAdapter = new com.hp.adapters.PagerAdapter(super.getSupportFragmentManager(), fragments);

		this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);

	}

	/**
	 * Ajoute les onglets dans la TabHost supï¿½rieure
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		HealthyProductsActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator(this.getText(R.string.frag_social_title)), (tabInfo = new TabInfo("Social",
				FragmentSocial.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		HealthyProductsActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator(this.getText(R.string.frag_main_title)), (tabInfo = new TabInfo("Profil", FragmentMain.class,
				args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		HealthyProductsActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(this.getText(R.string.frag_health_title)), (tabInfo = new TabInfo("Compléments",
				FragmentComplement.class, args)));
		// for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
		// mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 60;
		// // mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height =
		// ViewGroup.LayoutParams.WRAP_CONTENT;
		// }
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		mTabHost.setOnTabChangedListener(this);
	}

	private void initialiseInformationMap(final Produit prod) {
		informationMap.put(Information.INFO_GLUCIDE, new Information(prod.getgGlucide() + " g", R.drawable.glucides, Information.INFO_GLUCIDE));

		informationMap.put(Information.INFO_INGREDIENT_NON_RECOMMANDE, new Information(prod.getIngredientDeconseille().getNom(), R.drawable.button_ingredients,
				Information.INFO_INGREDIENT_NON_RECOMMANDE));
		informationMap.put(Information.INFO_KCAL, new Information(String.valueOf(prod.getkCal()), R.drawable.k_cal, Information.INFO_KCAL));
		informationMap.put(Information.INFO_KM_PARCOURU, new Information(prod.getKmParcouru() + "km", R.drawable.km_parcourus, Information.INFO_KM_PARCOURU));

		if (prod.getLabelBio() != null)
			informationMap.put(Information.INFO_LABEL_BIO, new Information(prod.getLabelBio().getNom(), R.drawable.button_label_bio, Information.INFO_LABEL_BIO));
		else
			informationMap.put(Information.INFO_LABEL_BIO, new Information("Aucun", R.drawable.button_label_bio, Information.INFO_LABEL_BIO));
		if (prod.getLabelFairTrade() != null)
			informationMap.put(Information.INFO_LABEL_FAIRTRADE, new Information(prod.getLabelFairTrade().getNom(), R.drawable.button_label_fairtrade, Information.INFO_LABEL_FAIRTRADE));
		else
			informationMap.put(Information.INFO_LABEL_FAIRTRADE, new Information("Aucun", R.drawable.button_label_fairtrade, Information.INFO_LABEL_FAIRTRADE));

		informationMap.put(Information.INFO_LIPIDE, new Information(prod.getgLipide() + " g", R.drawable.lipides, Information.INFO_LIPIDE));
		informationMap.put(Information.INFO_PRIX_MOYEN, new Information(prod.getPrixMoyen() + " €", R.drawable.prix_moy, Information.INFO_PRIX_MOYEN));
		informationMap.put(Information.INFO_OGM, new Information(prod.getOgm(), R.drawable.ogm, Information.INFO_OGM));
	}

	/**
	 * Add Tab content to the Tabhost
	 * 
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void AddTab(HealthyProductsActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	public void onPageScrollStateChanged(int arg0) {
		Log.i(TAG, "onPageScrollStateChanged");
		int pos = this.mViewPager.getCurrentItem();
		this.mTabHost.setCurrentTab(pos);

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.i(TAG, "onPageScrolled");

	}

	public void onPageSelected(int arg0) {
		Log.i(TAG, "onPageSelected");

	}

	protected void onSaveInstanceState(Bundle outState) {
		if (mTabHost != null)
			outState.putInt("tab", mTabHost.getCurrentTab()); // save the
																// tab
		// selected
		super.onSaveInstanceState(outState);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (mTabHost != null)
			mTabHost.setCurrentTab(savedInstanceState.getInt("tab"));

	}

	public void onTabChanged(String arg0) {
		// TabInfo newTab = this.mapTabInfo.get(tag);
		try {
			int pos = this.mTabHost.getCurrentTab();
			this.mViewPager.setCurrentItem(pos);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}

	}

	/**
	 * A simple factory that returns dummy views to the Tabhost
	 * 
	 * @author mwho
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	public void onAccuracyChanged(Sensor se, int accuracy) {
	}

	public void onSensorChanged(SensorEvent se) {
		float x = se.values[0];
		float y = se.values[1];
		float z = se.values[2];
		mAccelLast = mAccelCurrent;
		mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
		float delta = mAccelCurrent - mAccelLast;
		mAccel = mAccel * 0.9f + delta; // perform low-cut filter
		Log.i(TAG, "Accel:" + mAccel);
		if (mAccel > 4) {
			Log.i(TAG, "THIS IS A SHAKE");
			this.finish();
		}
	}

	/**
	 * store the first run
	 */
	public void setRunned() {
		SharedPreferences mPrefs = this.getSharedPreferences("healthyProductsPrefs", 0); // 0=mode
																							// privé.
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean("firstRunResults", false);
		edit.commit();
	}

	public boolean getFirstRun() {
		return mPrefs.getBoolean("firstRunResults", true);
	}

}