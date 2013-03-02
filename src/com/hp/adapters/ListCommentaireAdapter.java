package com.hp.adapters;

import java.util.List;
import com.hp.client.R;
import com.hp.client.resource.Commentaire;
import com.hp.client.resource.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("unused")
public class ListCommentaireAdapter extends ArrayAdapter<Commentaire> {

	User meeself;
	private final String TAG = "ListCommentaireAdapter";

	private TextView commentaireContent;

	// private List<Commentaire> listCommentaire;

	public ListCommentaireAdapter(Context context, int customRowView, List<Commentaire> list, User me) {
		super(context, customRowView, list);
		meeself = me;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		final int pos = position;
		if (row == null) {
			// ROW INFLATION

			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.frag_social_commentaire_rows, parent, false);

			// Like to implement here
			/*
			 * ((Button) row.findViewById(R.id.like_button)).setOnClickListener(new
			 * OnClickListener() { public void onClick(View v) { //todo } });
			 */

		}
		// Get item
		Commentaire item = this.getItem(pos);
		// Set comment content in TextView
		commentaireContent = (TextView) row.findViewById(R.id.commentContent);
		if (item != null) {
			Log.i(TAG,"ME:"+ meeself.getPrenom());
			Log.i(TAG, "AUTHOR:"+item.getAuthor());
			if (item.getAuthor().contentEquals(meeself.getPrenom()))
				commentaireContent.setText("Moi : " + item.getText());
			else
				commentaireContent.setText(item.getAuthor() + " : " + item.getText());
		}

		return row;

	}

}
