package com.hp.client.resource;

import java.io.IOException;
import java.io.StringWriter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Ingredient implements Parcelable {

	private int guid;

	private String nom;

	private String quantite;

	private String type;

	private String infos;

	private int qualite;

	private final String TAG = "Ingredient";

	public Ingredient() {
		super();
	}
	
	public Ingredient(final String nom) {
		super();
		this.nom = nom;
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
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom
	 *            the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the quantite
	 */
	public String getQuantite() {
		return quantite;
	}

	/**
	 * @param quantite
	 *            the quantite to set
	 */
	public void setQuantite(String quantite) {
		this.quantite = quantite;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the qualite
	 */
	public int getQualite() {
		return qualite;
	}

	/**
	 * @param qualite
	 *            the qualite to set
	 */
	public void setQualite(int qualite) {
		this.qualite = qualite;
	}

	/**
	 * @return the infos
	 */
	public String getInfos() {
		return infos;
	}

	/**
	 * @param infos
	 *            the infos to set
	 */
	public void setInfos(String infos) {
		this.infos = infos;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(guid);
		dest.writeString(nom);
		dest.writeString(quantite);
		dest.writeString(type);
		dest.writeString(infos);
		dest.writeInt(qualite);

	}

	public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

		public Ingredient createFromParcel(Parcel source) {
			return new Ingredient(source);
		}

		public Ingredient[] newArray(int size) {
			return new Ingredient[size];
		}
	};

	public Ingredient(Parcel in) {
		this.guid = in.readInt();
		this.nom = in.readString();
		this.quantite = in.readString();
		this.type = in.readString();
		this.infos = in.readString();
		this.qualite = in.readInt();
	}

	/**
	 * Description d'un ingrédient
	 */
	@Override
	public String toString() {
		StringWriter w = new StringWriter();

		w.append("nom: " + nom + "\n");
		w.append("quantite: " + quantite + "\n");
		w.append("type: " + type + "\n");
		w.append("infos: " + infos + "\n");
		w.append("qualite: " + qualite + "\n");
		try {
			w.close();
		} catch (IOException e) {
			Log.i(TAG, e.toString());
		}
		return w.toString();
	}

}
