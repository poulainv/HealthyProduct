package com.hp.client.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.hp.client.R;
import com.hp.interfaces.onUserCompleteListener;

/**
 * 
 * Fragment demandant le nom de l'utilisateur Fait apparaitre une fleche pour
 * indiquer le mouvement a suivre
 * 
 * @author Lisional
 * 
 */

public class FragmentUser extends Fragment {

	private final String TAG = "FragmentUser";

	onUserCompleteListener Activitylistener = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		final View myFragmentView = inflater.inflate(R.layout.frag_name_user, container, false);
		final ImageView fleche = (ImageView) myFragmentView.findViewById(R.id.fleche_next);
		
		final EditText nom = (EditText) myFragmentView.findViewById(R.id.champNom);
		nom.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Log.v("TAG", "afterTextChanged");
				String name = nom.getText().toString();
				Log.i(TAG, name);
				
				Activitylistener.setName(name);
				if (!name.contentEquals("")) {
					fleche.setVisibility(View.VISIBLE);
				} else {
					fleche.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				Log.v(TAG, "beforeTextChanged");
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.v(TAG, "onTextChanged");
			}
		});

		return myFragmentView;
	}

	/**
	 * Une fois que l'Activity est créée, on vérifie que le Fragment est bien
	 * attaché à une Activity permettant de manipuler un User
	 * 
	 */
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
