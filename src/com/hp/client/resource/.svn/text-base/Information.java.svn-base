package com.hp.client.resource;

import java.io.IOException;
import java.io.StringWriter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 
 * Conteneur du champ à afficher (km, gLipides, nb de labels etc...)
 * Et permet de lier un drawable à cette information
 * @author Lisional
 *
 */
public class Information implements Parcelable{

	public static final int INFO_KCAL = 0 ;
	public static final int INFO_GLUCIDE = 1 ;
	public static final int INFO_LIPIDE = 2 ;
	public static final int INFO_KM_PARCOURU = 3 ;
	public static final int INFO_PRIX_MOYEN = 4 ;
	public static final int INFO_LABEL_BIO = 5 ;
	public static final int INFO_LABEL_FAIRTRADE = 6 ;
	public static final int INFO_INGREDIENT_NON_RECOMMANDE = 7 ;
	public static final int INFO_OGM = 8 ;
	
	
	
	
	private String info;
	private int drawable;
	private final String TAG = "Information";
	private double typeInfo ;
	
	public Information(String i, int d, int typeInfo){
		info = i;
		drawable = d;
		this.typeInfo = typeInfo;
	}
	
	public void setInfo(String i){
		info = i;
	}
	
	public String getInfo(){
		return info;
	}
	
	public void setDrawable(int d){
		drawable = d;
	}
	
	public int getDrawable(){
		return drawable;
	}
	
	@Override
	public String toString(){
		StringWriter w = new StringWriter();
		w.append("info: " + info + "\n");

		try {
			w.close();
		} catch (IOException e) {
			Log.i(TAG  ,e.toString());
		}
		return w.toString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(info);
		dest.writeInt(drawable);
		dest.writeDouble(typeInfo);
	}

	public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

		public Ingredient createFromParcel(Parcel source) {
			return new Ingredient(source);
		}

		public Ingredient[] newArray(int size) {
			return new Ingredient[size];
		}
	};

	public Information(Parcel in) {
		this.info = in.readString();
		this.drawable = in.readInt();
		this.typeInfo = in.readDouble();
	}

	public int getTypeInfo() {
		return (int)typeInfo;
	}

	public void setTypeInfo(int typeInfo) {
		this.typeInfo = typeInfo;
	}
}
