package com.hp.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hp.adapters.CirclePageIndicator;
import com.hp.adapters.PageIndicator;
import com.hp.client.dao.DAO;
import com.hp.client.fragments.FragmentAffinage;
import com.hp.client.fragments.FragmentChoixProfil;
import com.hp.client.fragments.FragmentUser;
import com.hp.client.resource.Ingredient;
import com.hp.interfaces.onUserCompleteListener;

/**
 * 
 * Activity de création du profil utilisateur
 * 
 * @author Lisional
 * 
 */

public class FirstUseActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, onUserCompleteListener {

	SharedPreferences mPrefs;
	Button valid;
	private final String TAG = "FirstUseActivity";
	private List<Fragment> fragments;

	private DAO dao;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	PageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, ">> onCreate");

		mPrefs = this.getSharedPreferences("healthyProductsPrefs", 0); // 0 =
																		// mode
																		// privé.

		setContentView(R.layout.first_use);

		dao = DAO.getInstance(this);
		dao.open();
		ArrayList<Ingredient> l = (ArrayList<Ingredient>) dao.getListAllIngredient();
		dao.close();

		initialiseViewPager(l);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mViewPager);

		Log.i(TAG, "<< onCreate");

	}

	/**
	 * Instancie les Fragments et les assemble dans le ViewPager
	 * 
	 * @param ingredients
	 */
	private void initialiseViewPager(ArrayList<Ingredient> ingredients) {
		fragments = new Vector<Fragment>();

		Bundle b = new Bundle();
		b.putParcelableArrayList("listeAllIngredients", ingredients);

		fragments.add(Fragment.instantiate(this, FragmentUser.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentChoixProfil.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentAffinage.class.getName(), b));

		mPagerAdapter = new com.hp.adapters.PagerAdapter(super.getSupportFragmentManager(), fragments);
		mViewPager = (ViewPager) super.findViewById(R.id.pager);
		mViewPager.setAdapter(this.mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		
//		View f = fragments.get(1).getView().findViewById(R.id.fleche_next);
//		f.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				mViewPager.setCurrentItem(1, true);
//				
//			}
//		});
//		View f1 = fragments.get(1).getView().findViewById(R.id.fleche_next);
//		f1.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				mViewPager.setCurrentItem(2, true);
//				
//			}
//		});

	}

	public void onPageScrollStateChanged(int arg0) {
		Log.i(TAG, "onPageScrollStateChanged");

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.i(TAG, "onPageScrolled");

	}

	public void onPageSelected(int arg0) {
		Log.i(TAG, "onPageSelected");

	}

	/**
	 * Méthode de l'interface onUserCompleteListener permettant de sauvegarder
	 * le user et de passer à l'Activity suivante
	 * 
	 */
	public void registerUserAndGo() {

		dao.open();
		Log.i(TAG, "ID : " + dao.insertUser(user));
		dao.close();
		Intent i2 = new Intent(this, ScannerActivity.class);
		startActivity(i2);
		setRunned();
		this.finish();

	}

	/**
	 * store the first run
	 */
	public void setRunned() {
		SharedPreferences mPrefs = this.getSharedPreferences("healthyProductsPrefs", 0); // 0=mode
																							// privé.
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean("firstRun", false);
		edit.commit();
	}

	public boolean isNameSet() {
		if (user.getPrenom().contentEquals(""))
			return false;
		return true;
	}

	public boolean isTypeSet() {
		if (user.getTypeconso() == -1)
			return false;
		return true;
	}

	public void setName(String n) {
		user.setPrenom(n);

	}

	public void setIngredientListForbidden(ArrayList<Ingredient> l) {
		user.setIngredientForbiddenList(l);
	}

	public void setTypeConso(int t) {
		user.setTypeconso(t);

	}

}
