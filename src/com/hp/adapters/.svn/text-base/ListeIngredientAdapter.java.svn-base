package com.hp.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hp.client.R;
import com.hp.client.resource.Ingredient;

public class ListeIngredientAdapter extends ArrayAdapter<Ingredient> {

	// private static final String TAG = "ListeIngredientAdapter";

	private TextView ingredientName;
	private List<Ingredient> listIngredients;

	public ListeIngredientAdapter(Context c, int customRowView, List<Ingredient> list) {
		super(c, customRowView, list);
		listIngredients = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final int pos = position;
		if (row == null) {
			// ROW INFLATION

			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.frag_ingredients_rows, parent, false);

			((Button) row.findViewById(R.id.remove_button)).setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					listIngredients.remove(getItem(pos));
					notifyDataSetChanged();
				}
			});

		}
		// Get item
		Ingredient item = getItem(position);

		ingredientName = (TextView) row.findViewById(R.id.nom_ingredient);
		if (item != null)
			ingredientName.setText(item.getNom());

		return row;
	}
}