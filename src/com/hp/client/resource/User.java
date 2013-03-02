package com.hp.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
	
	public static final int TYPE_ECONOME = 0;
	public static final int TYPE_ECOFRIEND = 1;
	public static final int TYPE_FATWATCHER= 2;
	public static final int TYPE_QUALITE = 3;
	

	private int guid;

	private String prenom;

	/**
	 * DŽsigne le profil de consommateur de l'utilisateur
	 * 0 : Žconome
	 * 1 : Žcofrient
	 * 2 : fatwatcher
	 * 3 : qualite
	 * (Utiliser les attributs static de la classe User)
	 */
	private int typeconso;

	private List<Ingredient> ingredientForbiddenList;

	private final String TAG = "User";

	public User(String n, int type, List<Ingredient> ingredients) {
		prenom = n;
		typeconso = type;
		if (ingredients == null)
			ingredientForbiddenList = new ArrayList<Ingredient>();
		else
			ingredientForbiddenList = ingredients;
	}

	/**
	 * @return the ingredientForbiddenList
	 */
	public List<Ingredient> getIngredientForbiddenList() {
		return ingredientForbiddenList;
	}

	/**
	 * @param ingredientForbiddenList
	 *            the ingredientForbiddenList to set
	 */
	public void setIngredientForbiddenList(List<Ingredient> ingredientForbiddenList) {
		this.ingredientForbiddenList = ingredientForbiddenList;
	}

	/**
	 * @return the guid
	 */
	public int getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public void setGuid(int guid) {
		this.guid = guid;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom
	 *            the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the typeconso
	 */
	public int getTypeconso() {
		return typeconso;
	}

	/**
	 * @param typeconso
	 *            the typeconso to set
	 */
	public void setTypeconso(int typeconso) {
		this.typeconso = typeconso;
	}

	/**
	 * Description d'un User
	 */
	@Override
	public String toString() {
		StringWriter w = new StringWriter();
		w.append("nom: " + prenom + "\n");
		w.append("type conso: " + typeconso + "\n");
		for (int i = 0; i < ingredientForbiddenList.size(); ++i) {
			w.append("ingredient interdit " + i + " : " + ingredientForbiddenList.get(i) + "\n");
		}
		try {
			w.close();
		} catch (IOException e) {
			Log.i(TAG, e.toString());
		}
		return w.toString();
	}

	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public User(Parcel in) {
		this.guid = in.readInt();
		this.prenom = in.readString();
		this.typeconso = in.readInt();
		ingredientForbiddenList = new ArrayList<Ingredient>();
		in.readTypedList(ingredientForbiddenList, Ingredient.CREATOR);
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(guid);
		dest.writeString(prenom);
		dest.writeInt(typeconso);
		dest.writeTypedList(ingredientForbiddenList);

	}
}
