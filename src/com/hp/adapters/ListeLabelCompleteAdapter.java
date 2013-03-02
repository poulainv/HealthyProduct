package com.hp.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hp.client.R;
import com.hp.client.resource.Label;

public class ListeLabelCompleteAdapter extends ArrayAdapter<Label> {

	// private static final String TAG = "ListeIngredientAdapter";

	private TextView labelName;
	private List<Label> listLabels;

	public ListeLabelCompleteAdapter(Context c, int customRowView, List<Label> list) {
		super(c, customRowView, list);
		listLabels = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final int pos = position;
		if (row == null) {
			// ROW INFLATION

			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.ingredient_list_row, parent, false);


		}
		// Get item
		Label item = getItem(position);

		labelName = (TextView) row.findViewById(R.id.nom_ingredient);
		if (item != null)
			labelName.setText(item.getNom());

		return row;
	}
}