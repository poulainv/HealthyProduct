package com.hp.client.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hp.adapters.ListeIngredientAdapter;
import com.hp.client.R;
import com.hp.client.resource.Ingredient;
import com.hp.interfaces.onUserCompleteListener;

public class FragmentAffinage extends Fragment {

	private static final String TAG = "FragmentAffinage";

	AutoCompleteTextView autoCompleteTextView;
	Button plus;
	ListView list;
	ListeIngredientAdapter adapter;
	ArrayList<Ingredient> listeIngredients;
	ArrayList<Ingredient> allIngredients;

	onUserCompleteListener Activitylistener = null;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "onSaveInstanceState");
		outState.putSerializable("ingredients", listeIngredients);

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null)
			return null;

		if (savedInstanceState != null) {
			listeIngredients = (ArrayList<Ingredient>) savedInstanceState.getSerializable("ingredients");
			super.onCreateView(inflater, container, savedInstanceState);
		}

		if (listeIngredients == null)
			listeIngredients = new ArrayList<Ingredient>();

		View myFragmentView = inflater.inflate(R.layout.frag_affinage, container, false);

		list = (ListView) myFragmentView.findViewById(R.id.lvListe);

		adapter = new ListeIngredientAdapter(this.getActivity(), R.layout.frag_ingredients_rows, listeIngredients);
		list.setEmptyView(myFragmentView.findViewById(R.id.empty_list_view));
		list.setAdapter(adapter);

		autoCompleteTextView = (AutoCompleteTextView) myFragmentView.findViewById(R.id.input);

		Bundle b = this.getArguments();
		allIngredients = b.getParcelableArrayList("listeAllIngredients");
		if (allIngredients == null) {
			Log.i(TAG, "allIngredient null");
		}

		ArrayList<String> dropdownList = createArrayOfIngredients(allIngredients);

		autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, dropdownList));
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int position = -1;
				for (int i = 0; i < allIngredients.size(); ++i) {
					if (allIngredients.get(i).getNom().contentEquals(autoCompleteTextView.getText().toString()) && !listeIngredients.contains(allIngredients.get(i)))
						position = i;
				}

				if (position != -1)
					listeIngredients.add(allIngredients.get(position));

				adapter.notifyDataSetChanged();
				autoCompleteTextView.setText("");
				
			}
		});
		
		Button valid = (Button) myFragmentView.findViewById(R.id.valid_button);

		valid.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				if (!Activitylistener.isNameSet()) {
					Toast.makeText(getActivity(), "Renseigner votre prénom", Toast.LENGTH_LONG).show();
				} else if (!Activitylistener.isTypeSet()) {
					Toast.makeText(getActivity(), "Choisissez un profil", Toast.LENGTH_LONG).show();
				} else {
					Activitylistener.setIngredientListForbidden(listeIngredients);
					Activitylistener.registerUserAndGo();
				}
			}
		});

		return myFragmentView;
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

	private ArrayList<String> createArrayOfIngredients(List<Ingredient> l) {
		ArrayList<String> s = new ArrayList<String>();
		for (int i = 0; i < l.size(); ++i) {
			s.add(l.get(i).getNom());
		}
		return s;
	}



	

}