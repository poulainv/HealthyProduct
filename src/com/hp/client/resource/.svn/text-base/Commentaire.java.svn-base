package com.hp.client.resource;

import android.os.Parcel;
import android.os.Parcelable;

public class Commentaire implements Parcelable{

	
	private int guid ;
	
	private String text ;
	
	private int nbLike ;
	
	private String author ;
	
	

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	public Commentaire(){
		super();
	}
	
	public Commentaire(final String com){
		super();
		text = com;
	}
	
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the nbLike
	 */
	public int getNbLike() {
		return nbLike;
	}

	/**
	 * @param nbLike the nbLike to set
	 */
	public void setNbLike(int nbLike) {
		this.nbLike = nbLike;
	}

	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Commentaire> CREATOR = new Parcelable.Creator<Commentaire>() {

		public Commentaire createFromParcel(Parcel source) {
			return new Commentaire(source);
		}

		public Commentaire[] newArray(int size) {
			return new Commentaire[size];
		}
	};
	
	public Commentaire(Parcel in) {
		this.guid = in.readInt();
		this.text = in.readString();
		this.nbLike = in.readInt();
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(guid);
		dest.writeString(text);
		dest.writeInt(nbLike);
		
	}

	
	
}
