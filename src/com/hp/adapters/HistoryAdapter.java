package com.hp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.client.R;
import com.hp.client.resource.Produit;

public class HistoryAdapter extends BaseAdapter{

	private List<Produit> liste;
	private Activity myActivity;
	private final String TAG = "HistoryAdapter";
	public HistoryAdapter(List<Produit> l, Activity act){
		liste = l;
		myActivity = act;
	}
	public int getCount() {
		return liste.size();
	}

	public String getItem(int position) {
		if(position >= liste.size())
			return null;
		else
			return liste.get(position).getNom();
	}

	public long getItemId(int position) {
		return position;
	}
	public String getItemEan(long position){
		return liste.get((int) position).getEan();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout result;

        if (convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = (LinearLayout) inflater.inflate(R.layout.history_row, parent, false);
        } else {
            result = (LinearLayout) convertView;
        }
        
        final String mot = getItem(position);
        Log.i(TAG, mot);
        ((TextView)result.findViewById(R.id.nom_produit)).setText(mot);
		return result;
	}

}
