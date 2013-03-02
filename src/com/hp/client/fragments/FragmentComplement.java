package com.hp.client.fragments;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hp.adapters.ListeIngredientCompleteAdapter;
import com.hp.adapters.ListeLabelCompleteAdapter;
import com.hp.client.R;
import com.hp.client.resource.Information;
import com.hp.client.resource.Ingredient;
import com.hp.client.resource.Label;

/**
 * 
 * 
 * Fragment contenant les infos complémentaires du produit
 * 
 * @author Lisional
 * 
 */

public class FragmentComplement extends Fragment {

	
	FragmentComplement inst = this ;
	TextView t1;
	TextView t2;
	TextView t3;
	TextView t4;
	TextView t5;
	TextView t6;
	TextView allIngredientTextView;
	TextView allLabelTextView;
	Bundle arg;
	
	private final String TAG = "FragmentComplement";

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View myFragmentView = inflater.inflate(R.layout.frag_health, container, false);

		try{
			
		
		t1 = (TextView) myFragmentView.findViewById(R.id.suppTextView1);
		t2 = (TextView) myFragmentView.findViewById(R.id.suppTextView2);
		t3 = (TextView) myFragmentView.findViewById(R.id.suppTextView3);
		t4 = (TextView) myFragmentView.findViewById(R.id.suppTextView4);
		t5 = (TextView) myFragmentView.findViewById(R.id.suppTextView5);
		t6 = (TextView) myFragmentView.findViewById(R.id.suppTextView6);

		arg = getArguments();
		ArrayList<Information> infos = arg.getParcelableArrayList("infoToShowComplement");

		Log.i(TAG, infos.get(0).getInfo());
		Log.i(TAG, infos.get(1).getInfo());
		Log.i(TAG, infos.get(2).getInfo());
		Log.i(TAG, infos.get(3).getInfo());

		t1.setBackgroundDrawable(getResources().getDrawable(infos.get(0).getDrawable()));
		t1.setText(infos.get(0).getInfo());
		if(infos.get(0).getTypeInfo()==Information.INFO_INGREDIENT_NON_RECOMMANDE)
			addClickableTewtViewForListIngredient(t1);
		if(infos.get(0).getTypeInfo()==Information.INFO_LABEL_BIO || infos.get(0).getTypeInfo()==Information.INFO_LABEL_FAIRTRADE)
			addClickableTewtViewForListLabel(t1);
		
		
		
		t2.setBackgroundDrawable(getResources().getDrawable(infos.get(1).getDrawable()));
		t2.setText(infos.get(1).getInfo());
		if(infos.get(1).getTypeInfo()==Information.INFO_INGREDIENT_NON_RECOMMANDE)
			addClickableTewtViewForListIngredient(t2);
		if(infos.get(1).getTypeInfo()==Information.INFO_LABEL_BIO || infos.get(1).getTypeInfo()==Information.INFO_LABEL_FAIRTRADE)
			addClickableTewtViewForListLabel(t2);
		
		
		t3.setBackgroundDrawable(getResources().getDrawable(infos.get(2).getDrawable()));
		t3.setText(infos.get(2).getInfo());
		if(infos.get(2).getTypeInfo()==Information.INFO_INGREDIENT_NON_RECOMMANDE)
			addClickableTewtViewForListIngredient(t3);
		if(infos.get(2).getTypeInfo()==Information.INFO_LABEL_BIO || infos.get(2).getTypeInfo()==Information.INFO_LABEL_FAIRTRADE)
			addClickableTewtViewForListLabel(t3);
		

		t4.setBackgroundDrawable(getResources().getDrawable(infos.get(3).getDrawable()));
		t4.setText(infos.get(3).getInfo());
		if(infos.get(3).getTypeInfo()==Information.INFO_INGREDIENT_NON_RECOMMANDE)
			addClickableTewtViewForListIngredient(t4);
		if(infos.get(3).getTypeInfo()==Information.INFO_LABEL_BIO || infos.get(3).getTypeInfo()==Information.INFO_LABEL_FAIRTRADE)
			addClickableTewtViewForListLabel(t4);
		

		t5.setBackgroundDrawable(getResources().getDrawable(infos.get(4).getDrawable()));
		t5.setText(infos.get(4).getInfo());
		if(infos.get(4).getTypeInfo()==Information.INFO_INGREDIENT_NON_RECOMMANDE)
			addClickableTewtViewForListIngredient(t5);
		if(infos.get(4).getTypeInfo()==Information.INFO_LABEL_BIO || infos.get(4).getTypeInfo()==Information.INFO_LABEL_FAIRTRADE)
			addClickableTewtViewForListLabel(t5);
		
		t6.setBackgroundDrawable(getResources().getDrawable(infos.get(5).getDrawable()));
		t6.setText(infos.get(5).getInfo());
		if(infos.get(5).getTypeInfo()==Information.INFO_INGREDIENT_NON_RECOMMANDE)
			addClickableTewtViewForListIngredient(t6);
		if(infos.get(5).getTypeInfo()==Information.INFO_LABEL_BIO || infos.get(5).getTypeInfo()==Information.INFO_LABEL_FAIRTRADE)
			addClickableTewtViewForListLabel(t6);

	
		}catch(Exception e){
			Log.i(TAG,e.toString());
		}

		return myFragmentView;

	}

	private void addClickableTewtViewForListIngredient(TextView t){
		t.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				final Dialog d = new Dialog(inst.getActivity());
				d.setTitle("Liste des ingredients");
				d.setContentView(R.layout.dialog_ingredient_list_complete);

				ListView list = (ListView) d.findViewById(R.id.lvListe);

				final ArrayList<Ingredient> listeIngredients =  arg.getParcelableArrayList("ingredientList");

				final ListeIngredientCompleteAdapter adapter = new ListeIngredientCompleteAdapter(inst.getActivity().getApplicationContext(), R.layout.ingredient_list_row,
						listeIngredients);
				list.setEmptyView(d.findViewById(R.id.empty_list_view));
				list.setAdapter(adapter);
				d.show();
			}
			
		});
	}
	private void addClickableTewtViewForListLabel(TextView t){
		t.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {
				final Dialog d = new Dialog(inst.getActivity());
				d.setTitle("Liste des labels");
				d.setContentView(R.layout.dialog_label_list);
				
				ListView list = (ListView) d.findViewById(R.id.lvListe);
				
				final ArrayList<Label> listeLabels =  arg.getParcelableArrayList("labelList");
				
				final ListeLabelCompleteAdapter adapter = new ListeLabelCompleteAdapter(inst.getActivity().getApplicationContext(), R.layout.ingredient_list_row,
						listeLabels);
				list.setEmptyView(d.findViewById(R.id.empty_list_view));
				list.setAdapter(adapter);
				d.show();
				
			}
			
		});
	}
}