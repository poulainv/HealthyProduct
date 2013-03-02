package com.hp.client.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.adapters.ListCommentaireAdapter;
import com.hp.client.R;
import com.hp.client.dao.DAO;
import com.hp.client.resource.Commentaire;
import com.hp.client.resource.User;

/**
 * 
 * Fragment social, affichage des avis/notes
 * 
 * @author kofronpi
 * 
 */

public class FragmentSocial extends Fragment {
	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */

	// Is it a problem to declare some stuff such as button?
	private static final String TAG = "FragmentSocial";
	int idProduct; // the current product
	String nameProduct;
	Button shareButton;
	DAO instanceDao; // instance to get access to the database
	List<Commentaire> listComments; // List of comments
	ListView commentListView; // the list view XML element
	TextView productNameView; // the product name displayed at top
	Commentaire userIsSharing; // the Commentaire the user on the fragment
								// wants, perhaps, to share
	User me;
	EditText textInput;

	private ListCommentaireAdapter commentsAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View myFragmentView = inflater.inflate(R.layout.frag_social, container, false);

		// Initialisation des attributs et de la liste des commentaires
		Bundle arg = this.getArguments();
		nameProduct = arg.getString("nameProduct");
		idProduct = arg.getInt("idProduct");
		instanceDao = DAO.getInstance(getActivity());
		instanceDao.open();
		listComments = instanceDao.getListCommentaireForProduct(idProduct);
		me = instanceDao.getUser(1);

		// User input management
		textInput = (EditText) myFragmentView.findViewById(R.id.socialTextToShare);

		// set user ID here?

		// Button action
		shareButton = (Button) myFragmentView.findViewById(R.id.socialShareButton);
		shareButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "comment added with share button :" + textInput.getText().toString());

				// no comment to add to the database
				String strValueString = textInput.getText().toString();
				if (strValueString == null || strValueString.equals(""))
					return;
				userIsSharing = new Commentaire(strValueString);
				instanceDao.open();
				userIsSharing.setAuthor(me.getPrenom());
				Log.i(TAG,"COMMENT SET AUTHOR"+userIsSharing.getAuthor());
				instanceDao.insertCommentaireForProduct(userIsSharing, idProduct);
				listComments.add(userIsSharing);
				
				instanceDao.close();
				textInput.setText("");
				commentsAdapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "Commentaire ajouté, merci !", Toast.LENGTH_LONG).show();
				Log.i(TAG, "comment added with share button");

			}
		});

		// Get comments existing for given product
		commentListView = (ListView) myFragmentView.findViewById(R.id.commentList);
		commentsAdapter = new ListCommentaireAdapter(this.getActivity().getApplicationContext(), R.layout.frag_social_commentaire_rows, listComments, me);
		commentListView.setAdapter(commentsAdapter);

		// return (LinearLayout) inflater.inflate(R.layout.frag_social,
		// container, false);

		instanceDao.close();
		return myFragmentView;

	}
}