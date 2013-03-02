package com.hp.client.resource;

import android.os.Parcel;
import android.os.Parcelable;

public class Label implements Parcelable{

	private int guid ;
	
	private String nom ;
	
	private String sigle ;
	
	private String imagePath ;

	/**
	 * @return the guid
	 */
	public int getGuid() {
		return guid;
	}

	/**
	 * @param guid the guid to set
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
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the sigle
	 */
	public String getSigle() {
		return sigle;
	}

	/**
	 * @param sigle the sigle to set
	 */
	public void setSigle(String sigle) {
		this.sigle = sigle;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Label> CREATOR = new Parcelable.Creator<Label>() {

		public Label createFromParcel(Parcel source) {
			return new Label(source);
		}

		public Label[] newArray(int size) {
			return new Label[size];
		}
	};
	
	public Label(Parcel in) {
		this.guid = in.readInt();
		this.nom = in.readString();
		this.sigle = in.readString();
		this.imagePath = in.readString();
	}

	public Label() {
		super();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(guid);
		dest.writeString(nom);
		dest.writeString(sigle);
		dest.writeString(imagePath);
		
	}
	
	
	
	
}
