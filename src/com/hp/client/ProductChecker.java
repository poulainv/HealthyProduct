package com.hp.client;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.hp.client.dao.DAO;
import com.hp.client.resource.Constants;
import com.hp.client.resource.Ingredient;
import com.hp.client.resource.Produit;
import com.hp.client.resource.User;

/**
 * 
 * AsyncTask vï¿½rifiant la base de donnï¿½es, et les ingrï¿½dients C'est ici que l'on ferait l'accï¿½s au
 * serveur par exemple.
 * 
 * @author Lisional
 * 
 */
public class ProductChecker extends AsyncTask<Uri, Void, Void> {

//	private ProgressDialog progressDialog;
	private Activity m_activity;
	private final String TAG = "ProductChecker";
	private String codeEan;
	private Bundle b;

	public void setActivity(Activity activity) {
		m_activity = activity;

//		progressDialog = new ProgressDialog(new ContextThemeWrapper(m_activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
//		progressDialog.setMessage("Rï¿½cupï¿½ration des informations");
//		progressDialog.setCancelable(false);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressDialog.show();
	}

	public ProductChecker(Activity c, String ean) {
		setActivity(c);
		codeEan = ean;
	}

	/**
	 * This is executed in the UI thread. The only place where we can show the dialog.
	 */
	@Override
	protected void onPreExecute() {

	}

	/**
	 * Premier accï¿½s en base de donnï¿½es, pour vï¿½rifier si le produit existe Si c'est le cas, on
	 * rï¿½cupï¿½re les listes d'ingrï¿½dients: ingrï¿½dients interdits / tous les ingrï¿½dients, pour les
	 * comparer
	 */
	@Override
	protected Void doInBackground(Uri... params) {
		Log.i(TAG, "doInBackground");
//		SystemClock.sleep(1000);
		b = new Bundle();
		DAO dao = DAO.getInstance(m_activity);
		dao.open();
		Produit p = dao.getProduitByEan(codeEan);
		dao.close();
		if (p == null){
			Log.i(TAG, "Produit introuvable!");
			b.putInt("nextContentView", Constants.ERROR_PRODUCT_NOT_FOUND);
		}else{
			dao.open();
			dao.insertProduitIntoHistorique(p.getGuid());
			ArrayList<Ingredient> list = (ArrayList<Ingredient>) dao.getListIngredientForProduct(p.getGuid());
			User u = dao.getUser(1);
			dao.close();
			
			ArrayList<Ingredient> listeInterdits = new ArrayList<Ingredient>();
			// On ne conserve que la liste des ingrï¿½dients communs

			for(int i = 0 ; i  < u.getIngredientForbiddenList().size() ; ++i){
				for(int j = 0 ; j < list.size() ; ++j){
					if(list.get(j).getNom().contentEquals(u.getIngredientForbiddenList().get(i).getNom()))
						listeInterdits.add(u.getIngredientForbiddenList().get(i));
				}
			}
			
			// si on trouve au moins un ingrï¿½dient commun
			if(!listeInterdits.isEmpty()){
				Log.i(TAG, "Ingrédient interdit trouvé");
				b.putInt("nextContentView", Constants.ERROR_PRODUCT_FORBIDDEN);
				b.putParcelable("produit", p); // On rajoute le produit au cas ou le mec veut quand meme le voir
				b.putParcelable("user", u);
				b.putParcelableArrayList("listIngredientInterdits", listeInterdits);
			}else{
				Log.i(TAG, "Produit OK");
				b.putInt("nextContentView", Constants.PRODUCT_OK);
				b.putParcelable("produit", p);
				b.putParcelable("user", u);
			}
		}
		return null;
	}

	/**
	 * This is executed in the UI thread. The only place where we can show the dialog.
	 */
	@Override
	protected void onPostExecute(Void unused) {
//		if (progressDialog.isShowing())
//			progressDialog.dismiss();
		
		Intent i2 = new Intent(m_activity,HealthyProductsActivity.class);
		i2.putExtra("bundleProduit", b);
		
		i2.putExtra("code", codeEan); // Code barre nutella
		
		m_activity.startActivity(i2);

	}

}
