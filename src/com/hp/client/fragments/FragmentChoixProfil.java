package com.hp.client.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.hp.client.R;
import com.hp.client.resource.User;
import com.hp.interfaces.onUserCompleteListener;

/**
 * 
 * Fragment principal, contenant les infos primordiales du profil.
 * 
 * @author Lisional
 * 
 */

public class FragmentChoixProfil extends Fragment {

	ArrayList<ToggleButton> buttons;
	private static final String TAG = "FragmentChoixProfil";
	private int currentlyChecked = 0;

	onUserCompleteListener Activitylistener = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View v = inflater.inflate(R.layout.frag_choixprofil, container, false);

		buttons = new ArrayList<ToggleButton>();

		buttons.add((ToggleButton) v.findViewById(R.id.radioEconome));
		buttons.add((ToggleButton) v.findViewById(R.id.radioEcofriend));
		buttons.add((ToggleButton) v.findViewById(R.id.radioFatWatcher));
		buttons.add((ToggleButton) v.findViewById(R.id.radioQualite));
		
		buttons.get(currentlyChecked).setChecked(true);
		buttons.get(currentlyChecked).setClickable(true);
		Activitylistener.setTypeConso(User.TYPE_ECONOME);

		buttons.get(0).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.d(TAG, "click button 0");
				Activitylistener.setTypeConso(User.TYPE_ECONOME);
				buttons.get(currentlyChecked).setChecked(false);
				buttons.get(currentlyChecked).setClickable(true);
				buttons.get(0).setClickable(false);
				currentlyChecked = User.TYPE_ECONOME;
			}
		});

		buttons.get(1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "click button 1");
				Activitylistener.setTypeConso(User.TYPE_ECOFRIEND);
				buttons.get(currentlyChecked).setChecked(false);
				buttons.get(currentlyChecked).setClickable(true);
				buttons.get(1).setClickable(false);
				currentlyChecked = User.TYPE_ECOFRIEND;
			}
		});

		buttons.get(2).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "click button 2");
				Activitylistener.setTypeConso(User.TYPE_FATWATCHER);
				buttons.get(currentlyChecked).setChecked(false);
				buttons.get(currentlyChecked).setClickable(true);
				buttons.get(2).setClickable(false);
				currentlyChecked = User.TYPE_FATWATCHER;
			}
		});

		buttons.get(3).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.d(TAG, "click button 3");
				Activitylistener.setTypeConso(User.TYPE_QUALITE);
				buttons.get(currentlyChecked).setChecked(false);
				buttons.get(currentlyChecked).setClickable(true);
				buttons.get(3).setClickable(false);
				currentlyChecked = User.TYPE_QUALITE;
			}
		});

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "->onAttach");
		try {
			Activitylistener = (onUserCompleteListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement onUserCompleteListener");
		}
		Log.d(TAG, "<-onAttach");
	}
}