package com.hp.client.fragments;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Fragment principal, contenant les infos primordiales du profil.
 * 
 * @author Lisional
 * 
 */

public class FragmentMain extends Fragment {

	FragmentMain inst = this;
	TextView t1;
	TextView t2;
	TextView t3;
	TextView nameProduct;
	Bundle arg ;

	private final String TAG = "FragmentMain";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View myFragmentView = inflater.inflate(R.layout.frag_main, container, false);

		t1 = (TextView) myFragmentView.findViewById(R.id.mainTextView);
		t2 = (TextView) myFragmentView.findViewById(R.id.mainTextView2);
		t3 = (TextView) myFragmentView.findViewById(R.id.mainTextView3);
		nameProduct = (TextView) myFragmentView.findViewById(R.id.nameProductTextView);

		arg = getArguments();
		ArrayList<Information> infos = arg.getParcelableArrayList("infoToShowMain");

		nameProduct.setText(arg.getCharSequence("nameProduct"));

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