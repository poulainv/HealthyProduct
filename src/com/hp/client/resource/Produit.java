package com.hp.client.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Produit implements Parcelable{
	
	private final String TAG = "Produit";

	private int guid ;
	
	private String nom ;
	
	private String ean ;
	
	private float prixMoyen ;
	
	private int kmParcouru ;
	
	private int kCal ;
	
	private float gGlucide ;
	
	private float gLipide ;
	
	private String ogm = "Pas d'OGM";
	
	private List<Commentaire> commentaireList;
	
	private List<Label> labelList ;
	
	private List <Ingredient> ingredientList ;

	/** retourne un label bio s'il existe
	 * null sinon
	 * @return
	 */
	public Label getLabelBio(){
		for(int i=0;i<labelList.size();i++){
			if(labelList.get(i).getSigle().equals("AB"))
				return labelList.get(i);
		}
		return null;
	}

	/** retourne un label fair trade s'il existe
	 * null sinon
	 * @return
	 */
	public Label getLabelFairTrade(){
		for(int i=0;i<labelList.size();i++){
			if(labelList.get(i).getSigle().equals("FT"))
				return labelList.get(i);
		}
		return null;
	}


	/** retourne l'ingredient le plus mauvais du produit
	 * null sinon
	 * @return
	 */
	public Ingredient getIngredientDeconseille(){
		int min = 3;
		int res = 0;
		boolean produitPasBon = false ; 
		for(int i=0;i<ingredientList.size();i++){
			if(ingredientList.get(i).getQualite()<min){
				produitPasBon = true;
				min = ingredientList.get(i).getQualite();
				res = i;
			}
		}
		if(produitPasBon)
			return getIngredientList().get(res);
		else{
			return new Ingredient("Tout est bon !");
		}
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
	 * @return the ean
	 */
	public String getEan() {
		return ean;
	}

	/**
	 * @param ean the ean to set
	 */
	public void setEan(String ean) {
		this.ean = ean;
	}

	/**
	 * @return the prixMoyen
	 */
	public float getPrixMoyen() {
		return prixMoyen;
	}

	/**
	 * @param prixMoyen the prixMoyen to set
	 */
	public void setPrixMoyen(float prixMoyen) {
		this.prixMoyen = prixMoyen;
	}

	/**
	 * @return the kmParcouru
	 */
	public int getKmParcouru() {
		return kmParcouru;
	}

	/**
	 * @param kmParcouru the kmParcouru to set
	 */
	public void setKmParcouru(int kmParcouru) {
		this.kmParcouru = kmParcouru;
	}

	/**
	 * @return the kCal
	 */
	public int getkCal() {
		return kCal;
	}

	/**
	 * @param kCal the kCal to set
	 */
	public void setkCal(int kCal) {
		this.kCal = kCal;
	}

	/**
	 * @return the gGlucide
	 */
	public float getgGlucide() {
		return gGlucide;
	}

	/**
	 * @param gGlucide the gGlucide to set
	 */
	public void setgGlucide(float gGlucide) {
		this.gGlucide = gGlucide;
	}

	/**
	 * @return the gLipide
	 */
	public float getgLipide() {
		return gLipide;
	}

	/**
	 * @param gLipide the gLipide to set
	 */
	public void setgLipide(float gLipide) {
		this.gLipide = gLipide;
	}

	/**
	 * @return the commentaireList
	 */
	public List<Commentaire> getCommentaireList() {
		return commentaireList;
	}

	/**
	 * @param commentaireList the commentaireList to set
	 */
	public void setCommentaireList(List<Commentaire> commentaireList) {
		this.commentaireList = commentaireList;
	}

	/**
	 * @return the labelList
	 */
	public List<Label> getLabelList() {
		return labelList;
	}

	/**
	 * @param labelList the labelList to set
	 */
	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	/**
	 * @return the ingredientList
	 */
	public List<Ingredient> getIngredientList() {
		return ingredientList;
	}

	/**
	 * @param ingredientList the ingredientList to set
	 */
	public void setIngredientList(List<Ingredient> ingredientList) {
		this.ingredientList = ingredientList;
	}
	
	
	
	/**
	 * @return the ogm
	 */
	public String getOgm() {
		return ogm;
	}

	/**
	 * @param ogm the ogm to set
	 */
	public void setOgm(String ogm) {
		this.ogm = ogm;
	}

	/**
	 * 
	 * Description d'un produit (sauf les deux listes comments/ingredients)
	 * @return 
	 */

	@Override
	public String toString(){
		StringWriter w = new StringWriter();
		w.append("nom: "+nom+"\n");
		w.append("prix moyen: "+prixMoyen+"\n");
		w.append("km parcourus: "+kmParcouru+"\n");
		w.append("kCal: "+kCal+"\n");
		w.append("gGlucide: "+gGlucide+"\n");
		w.append("gLipide: "+gLipide+"\n");
		try {
			w.close();
		} catch (IOException e) {
			Log.w(TAG, e.toString());
		}
		return w.toString();
		
	}

	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Produit> CREATOR = new Parcelable.Creator<Produit>() {

		public Produit createFromParcel(Parcel source) {
			return new Produit(source);
		}

		public Produit[] newArray(int size) {
			return new Produit[size];
		}
	};

	public Produit(Parcel in) {
		this.guid = in.readInt();
		this.nom = in.readString();
		this.ean = in.readString();
		this.prixMoyen = in.readFloat();
		this.kmParcouru = in.readInt();
		this.kCal = in.readInt();
		this.gGlucide = in.readFloat();
		this.gLipide = in.readFloat();
		commentaireList = new ArrayList<Commentaire>();
		in.readTypedList(commentaireList, Commentaire.CREATOR);
		labelList = new ArrayList<Label>();
		in.readTypedList(labelList, Label.CREATOR);
		ingredientList = new ArrayList<Ingredient>();
		in.readTypedList(ingredientList, Ingredient.CREATOR);

	}

	public Produit() {
		
	}

	public void writeToParcel(Parcel dest, int flags) {		
		
		dest.writeInt(guid);
		dest.writeString(nom);
		dest.writeString(ean);
		dest.writeFloat(prixMoyen);
		dest.writeInt(kmParcouru);
		dest.writeInt(kCal);
		dest.writeFloat(gGlucide);
		dest.writeFloat(gLipide);
		dest.writeTypedList(commentaireList);
		dest.writeTypedList(labelList);
		dest.writeTypedList(ingredientList);
		
		
	}
	
}
