package com.hp.client.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hp.client.db.DBSQLite;
import com.hp.client.resource.Commentaire;
import com.hp.client.resource.Ingredient;
import com.hp.client.resource.Label;
import com.hp.client.resource.Produit;
import com.hp.client.resource.User;

public class DAO {

	private static volatile DAO instance = null;

	private final String TAG = "DAO";

	private static final int VERSION_BDD = 26;

	private static final String NOM_BDD = "healthyproducts.db";

	private final String TABLE_COMMENTAIRE = "COMMENTAIRE";
	private final int NUM_COL_ID = 0;
	private final String COL_ID = "ID";
	private final int NUM_COL_COMMENTAIRETEXT = 1;
	private final String COL_COMMENTAIRETEXT = "COMMENTAIRETEXT";
	private final int NUM_COL_NBLIKE = 2;
	private final String COL_NBLIKE = "NBLIKE";
	private final int NUM_COL_FKPRODUCT = 3;
	private final String COL_FKPRODUCT = "FKPRODUCT";
	private final int NUM_COL_PRENOM_COM = 4;

	private final String TABLE_INGREDIENT = "INGREDIENT";
	private final int NUM_COL_NOM = 1;
	private final String COL_NOM = "NOM";
	private final int NUM_COL_TYPEINGREDIENT = 4;
	private final String COL_TYPEINGREDIENT = "TYPEINGREDIENT";
	private final int NUM_COL_INFOS = 2;
	private final String COL_INFOS = "INFOS";
	private final int NUM_COL_QUALITE = 3;
	private final String COL_QUALITE = "QUALITE";

	private final String TABLE_LABEL = "LABEL";
	private final int NUM_COL_SIGLE = 2;
	private final String COL_SIGLE = "SIGLE";
	private final int NUM_COL_IMAGELABEL = 3;
	private final String COL_IMAGELABEL = "IMAGELABEL";

	private final String TABLE_PRODUCT = "PRODUCT";
	private final int NUM_COL_EAN = 3;
	private final String COL_EAN = "EAN";
	private final int NUM_COL_PRIXMOYEN = 2;
	private final String COL_PRIXMOYEN = "PRIXMOYEN";
	private final int NUM_COL_KMPARCOURU = 5;
	private final String COL_KMPARCOURU = "KMPARCOURU";
	private final int NUM_COL_KCAL = 4;
	private final String COL_KCAL = "KCAL";
	private final int NUM_COL_QTEGLUCIDE = 6;
	private final String COL_QTEGLUCIDE = "QTEGLUCIDE";
	private final int NUM_COL_QTELIPIDE = 7;
	private final String COL_QTELIPIDE = "QTELIPIDE";

	private final String TABLE_PRODUCTINGREDIENT = "PRODUCTINGREGIENT";
	private final int NUM_COL_FKPRODUCT_P_I = 1;
	private final int NUM_COL_QUANTITE = 2;
	private final String COL_QUANTITE = "QUANTITE";
	private final int NUM_COL_FKINGREDIENT = 3;
	private final String COL_FKINGREDIENT = "FKINGREDIENT";

	private final String TABLE_PRODUCTLABEL = "PRODUCTLABEL";
	private final int NUM_COL_FKLABEL = 2;
	private final String COL_FKLABEL = "FKLABEL";

	private final String TABLE_USER = "USER";
	private final int NUM_COL_PRENOM = 1;
	private final String COL_PRENOM = "PRENOM";
	private final int NUM_COL_TYPECONSO = 2;
	private final String COL_TYPECONSO = "TYPECONSO";

	private final String TABLE_USERINGREDIENT = "USERINGREDIENT";
	private final int NUM_COL_FKUSER = 2;
	private final String COL_FKUSER = "FKUSER";

	private final String TABLE_HISTORIQUE = "HISTORIQUE";

	private SQLiteDatabase bdd;

	private DBSQLite maBaseSQLite;

	private DAO(Context context) {
		super();
		// On cree la BDD et sa table
		maBaseSQLite = DBSQLite.getInstance(context, NOM_BDD, null, VERSION_BDD);
	}

	public final static DAO getInstance(Context context) {
		if (DAO.instance == null) {
			synchronized (DAO.class) {
				if (DAO.instance == null) {
					DAO.instance = new DAO(context);
				}
			}
		}
		return DAO.instance;
	}

	/**
	 * Methode pour ouvrir en ecriture la base de donnees
	 */
	public void open() {
		Log.i(TAG, "open db");
		bdd = maBaseSQLite.getWritableDatabase();
	}

	/**
	 * Methode pour ferme en lecture et en ecrite l'acces a la bdd
	 */
	public void close() {
		bdd.close();
	}

	public SQLiteDatabase getBDD() {
		return bdd;
	}

	public Ingredient getIngredientById(final int id) {
		Cursor c = bdd.query(TABLE_INGREDIENT, new String[] { COL_ID, COL_NOM, COL_INFOS, COL_QUALITE, COL_TYPEINGREDIENT }, COL_ID + " = " + id,
				null, null, null, null);
		return cursorToIngredient(c);
	}

	public List<Ingredient> getListIngredientForProduct(final int productId) {

		String sql = "SELECT " + TABLE_INGREDIENT + "." + COL_ID + "," + TABLE_INGREDIENT + "." + COL_NOM + "," + TABLE_INGREDIENT + "." + COL_INFOS
				+ "," + TABLE_INGREDIENT + "." + COL_QUALITE + "," + TABLE_INGREDIENT + "." + COL_TYPEINGREDIENT + "," + TABLE_PRODUCTINGREDIENT
				+ "." + COL_QUANTITE + " FROM " + TABLE_INGREDIENT + " INNER JOIN " + TABLE_PRODUCTINGREDIENT + " ON " + TABLE_INGREDIENT + "."
				+ COL_ID + " = " + TABLE_PRODUCTINGREDIENT + "." + COL_FKINGREDIENT + " WHERE " + TABLE_PRODUCTINGREDIENT + "." + COL_FKPRODUCT
				+ " = " + productId + ";";
		return cursorToListIngredient(bdd.rawQuery(sql, null));
	}

	public List<Ingredient> getListAllIngredient() {
		Cursor c = bdd.query(TABLE_INGREDIENT, new String[] { COL_ID, COL_NOM, COL_INFOS, COL_QUALITE, COL_TYPEINGREDIENT }, null, null, null, null,
				null);
		return cursorToListIngredient(c);
	}

	public List<Ingredient> getIngredientForbiddenListForUser(final int idUser) {
		String sql = "SELECT " + TABLE_INGREDIENT + "." + COL_ID + "," + TABLE_INGREDIENT + "." + COL_NOM + "," + TABLE_INGREDIENT + "." + COL_INFOS
				+ "," + TABLE_INGREDIENT + "." + COL_QUALITE + "," + TABLE_INGREDIENT + "." + COL_TYPEINGREDIENT + " FROM " + TABLE_INGREDIENT
				+ " INNER JOIN " + TABLE_USERINGREDIENT + " ON " + TABLE_INGREDIENT + "." + COL_ID + " = " + TABLE_USERINGREDIENT + "."
				+ COL_FKINGREDIENT + " WHERE " + TABLE_USERINGREDIENT + "." + COL_FKUSER + " = " + idUser + ";";
		return cursorToListIngredient(bdd.rawQuery(sql, null));
	}

	public List<Commentaire> getListCommentaireForProduct(final int id) {
		Cursor c = bdd.query(TABLE_COMMENTAIRE, new String[] { COL_ID, COL_COMMENTAIRETEXT, COL_NBLIKE, COL_FKPRODUCT, COL_PRENOM }, COL_FKPRODUCT
				+ " = " + id, null, null, null, null);
		return cursorToListCommentaire(c);
	}

	public long insertCommentaireForProduct(final Commentaire com, final int productId) {
		ContentValues content = new ContentValues();
		content.put(COL_COMMENTAIRETEXT, com.getText());
		content.put(COL_FKPRODUCT, productId);
		content.put(COL_PRENOM, com.getAuthor());
		return bdd.insert(TABLE_COMMENTAIRE, null, content);
	}

	public int updateCommentaire(final Commentaire com) {
		ContentValues content = new ContentValues();
		content.put(COL_COMMENTAIRETEXT, com.getText());
		content.put(COL_NBLIKE, com.getNbLike());
		return bdd.update(TABLE_COMMENTAIRE, content, COL_ID + " = " + com.getGuid(), null);
	}

	public Produit getProduitById(final int id) {

		Cursor c = bdd.query(TABLE_PRODUCT, new String[] { COL_ID, COL_NOM, COL_PRIXMOYEN, COL_EAN, COL_KCAL, COL_KMPARCOURU, COL_QTEGLUCIDE,
				COL_QTELIPIDE }, COL_ID + " = " + id, null, null, null, null);
		return cursorToProduct(c);
	}

	/**
	 * Enregistre le produit dans l'historique de l'utilisateur
	 * 
	 * @param produitId
	 *            le guid du produit a enregisrter
	 * @return l'id de la ligne historique ajoutée
	 */
	public long insertProduitIntoHistorique(final int produitId) {

		List<Produit> lp = getProductHistorique();
		for (int i = 0; i < lp.size(); i++) {
			if (lp.get(i).getGuid() == produitId) {
				removeProductIntoHistorique(produitId);
				break;
			}
		}

		ContentValues content = new ContentValues();
		content.put(COL_FKPRODUCT, produitId);
		return bdd.insert(TABLE_HISTORIQUE, null, content);
	}

	public List<Produit> getProductHistorique() {
		Cursor c = bdd.query(TABLE_HISTORIQUE, new String[] { COL_ID, COL_FKPRODUCT }, null, null, null, null, null);
		if (c.getCount() == 0)
			return new ArrayList<Produit>();
		c.moveToFirst();
		List<Produit> listProduit = new ArrayList<Produit>();
		List<Integer> listProduitId = new ArrayList<Integer>();

		do {
			listProduitId.add(c.getInt(NUM_COL_FKPRODUCT_P_I));
		} while (c.moveToNext());
		c.close();
		for (int i = 0; i < listProduitId.size(); i++) {
			listProduit.add(this.getProduitById(listProduitId.get(i)));
		}
		return listProduit;
	}

	/**
	 * Supprimeun produit de l'historique
	 * 
	 * @param l
	 *            'id d'un produit
	 * @param userId
	 * @return
	 */
	public long removeProductIntoHistorique(final int productId) {
		return bdd.delete(TABLE_HISTORIQUE, "" + COL_FKPRODUCT + " = " + productId, null);
	}

	/**
	 * Renvoit un utilisateur
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(final int id) {
		Cursor c = bdd.query(TABLE_USER, new String[] { COL_ID, COL_PRENOM, COL_TYPECONSO }, COL_ID + " = " + id, null, null, null, null);
		return cursorToUser(c);
	}

	/**
	 * Ajoute un utilisateur en base de données (sa liste d'ingrdient interdit
	 * compris, s'il elle existe)
	 * 
	 * @param user
	 * @return
	 */
	public long insertUser(final User user) {
		ContentValues content = new ContentValues();
		content.put(COL_PRENOM, user.getPrenom());
		content.put(COL_TYPECONSO, user.getTypeconso());
		long userId = bdd.insert(TABLE_USER, null, content);
		if (user.getIngredientForbiddenList() != null) {
			for (int i = 0; i < user.getIngredientForbiddenList().size(); i++) {
				this.insertForbiddenIngredientForUser(user.getIngredientForbiddenList().get(i), (int) userId);
			}
		}
		return userId;
	}

	public int updateUser(final int userId, final User user) {
		removeForbiddenIngredientListForUser(userId);
		ContentValues content = new ContentValues();
		content.put(COL_PRENOM, user.getPrenom());
		content.put(COL_TYPECONSO, user.getTypeconso());
		if (user.getIngredientForbiddenList() != null) {
			for (int i = 0; i < user.getIngredientForbiddenList().size(); i++) {
				this.insertForbiddenIngredientForUser(user.getIngredientForbiddenList().get(i), userId);
			}
		}
		return bdd.update(TABLE_USER, content, COL_ID + " = " + userId, null);
	}

	/**
	 * Ajout un ingredient interdit à la liste des ingredient interdit d'un
	 * utilisateur en base de donnees
	 * 
	 * @param ing
	 *            : un ingredient
	 * @param userId
	 * @return
	 */
	public long insertForbiddenIngredientForUser(final Ingredient ing, final int userId) {
		ContentValues content = new ContentValues();
		content.put(COL_FKINGREDIENT, ing.getGuid());
		content.put(COL_FKUSER, userId);
		return bdd.insert(TABLE_USERINGREDIENT, null, content);
	}

	/**
	 * Ajout un ingredient interdit à la liste des ingredient interdit d'un
	 * utilisateur en base de donnees
	 * 
	 * @param l
	 *            'id d'un ingrdient
	 * @param userId
	 * @return
	 */
	public long insertForbiddenIngredientForUser(final int ingredientId, final int userId) {
		ContentValues content = new ContentValues();
		content.put(COL_FKINGREDIENT, ingredientId);
		content.put(COL_FKUSER, userId);
		return bdd.insert(TABLE_USERINGREDIENT, null, content);
	}

	/**
	 * Supprime la liste des ingredients interdit d'un utilisateur en base de
	 * donnees
	 * 
	 * @param l
	 *            'id d'un ingrdient
	 * @param userId
	 * @return
	 */
	public long removeForbiddenIngredientListForUser(final int userId) {
		return bdd.delete(TABLE_USERINGREDIENT, "" + COL_FKUSER + " = " + userId, null);
	}

	/**
	 * Supprime un ingredient de la liste des ingredients interdit d'un
	 * utilisateur en base de donnees
	 * 
	 * @param l
	 *            'id d'un ingrdient
	 * @param userId
	 * @return
	 */
	public long removeForbiddenIngredientForUser(final int ingredientId, final int userId) {
		return bdd.delete(TABLE_USERINGREDIENT, "" + COL_FKUSER + " = " + userId + " AND " + COL_FKINGREDIENT + " = " + ingredientId, null);
	}

	/**
	 * Retourne un produit par son ean Retourne null si aucun produit n'est
	 * trouv�
	 */
	public Produit getProduitByEan(final String ean) {
		Cursor c = bdd.query(TABLE_PRODUCT, new String[] { COL_ID, COL_NOM, COL_PRIXMOYEN, COL_EAN, COL_KCAL, COL_KMPARCOURU, COL_QTEGLUCIDE,
				COL_QTELIPIDE }, COL_EAN + " = '" + ean + "'", null, null, null, null);
		return cursorToProduct(c);
	}

	public List<Label> getListLabelForProduct(final int productId) {
		String sql = "SELECT " + TABLE_LABEL + "." + COL_ID + "," + TABLE_LABEL + "." + COL_NOM + "," + TABLE_LABEL + "." + COL_SIGLE + ","
				+ TABLE_LABEL + "." + COL_IMAGELABEL + " FROM " + TABLE_LABEL + " INNER JOIN " + TABLE_PRODUCTLABEL + " ON " + TABLE_LABEL + "."
				+ COL_ID + " = " + TABLE_PRODUCTLABEL + "." + COL_FKLABEL + " WHERE " + TABLE_PRODUCTLABEL + "." + COL_FKPRODUCT + " = " + productId
				+ ";";
		return cursorToListLabel(bdd.rawQuery(sql, null));
	}

	/**
	 * Cette methode permet de convertir un cursor en un Ingredient
	 * 
	 * @param c
	 * @return
	 */
	private Ingredient cursorToIngredient(Cursor c) {
		if (c.getCount() == 0)
			return new Ingredient();
		c.moveToFirst();
		Ingredient ing = new Ingredient();
		ing.setGuid(c.getInt(NUM_COL_ID));
		ing.setNom(c.getString(NUM_COL_NOM));
		ing.setInfos(c.getString(NUM_COL_INFOS));
		ing.setQualite(c.getInt(NUM_COL_QUALITE));
		ing.setType(c.getString(NUM_COL_TYPEINGREDIENT));
		c.close();
		return ing;
	}

	/**
	 * Cette methode permet de convertir un cursor en un Produit
	 * 
	 * @param c
	 * @return
	 */
	private Produit cursorToProduct(Cursor c) {
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		Produit prdt = new Produit();
		prdt.setGuid(c.getInt(NUM_COL_ID));
		prdt.setNom(c.getString(NUM_COL_NOM));
		prdt.setEan(c.getString(NUM_COL_EAN));
		prdt.setgGlucide(c.getFloat(NUM_COL_QTEGLUCIDE));
		prdt.setgLipide(c.getFloat(NUM_COL_QTELIPIDE));
		prdt.setkCal(c.getInt(NUM_COL_KCAL));
		prdt.setKmParcouru(c.getInt(NUM_COL_KMPARCOURU));
		prdt.setPrixMoyen(c.getFloat(NUM_COL_PRIXMOYEN));
		c.close();

		prdt.setCommentaireList(this.getListCommentaireForProduct(prdt.getGuid()));
		prdt.setLabelList(this.getListLabelForProduct(prdt.getGuid()));
		prdt.setIngredientList(this.getListIngredientForProduct(prdt.getGuid()));

		return prdt;
	}

	private User cursorToUser(Cursor c) {
		if (c.getCount() == 0)
			return new User("", 0, null);
		c.moveToFirst();
		User user = new User("", 0, null);

		user.setGuid(c.getInt(NUM_COL_ID));
		user.setPrenom(c.getString(NUM_COL_PRENOM));
		user.setTypeconso(c.getInt(NUM_COL_TYPECONSO));
		c.close();

		user.setIngredientForbiddenList(this.getIngredientForbiddenListForUser(user.getGuid()));

		return user;
	}

	/**
	 * Cette methode permet de convertir un cursor en une liste d' Ingredient
	 * 
	 * @param c
	 * @return
	 */
	private List<Ingredient> cursorToListIngredient(Cursor c) {
		if (c.getCount() == 0)
			return new ArrayList<Ingredient>();
		List<Ingredient> listIngredient = new ArrayList<Ingredient>();
		c.moveToFirst();
		do {
			Ingredient in = new Ingredient();
			in.setGuid(c.getInt(NUM_COL_ID));
			in.setNom(c.getString(NUM_COL_NOM));
			in.setInfos(c.getString(NUM_COL_INFOS));
			in.setQualite(c.getInt(NUM_COL_QUALITE));
			in.setType(c.getString(NUM_COL_TYPEINGREDIENT));

			// TODO faire un truc propre ici
			if (c.getColumnCount() == 6) {
				in.setQuantite(c.getString(5)); // pour recuperer la quantite de
												// l'ingredient
			}
			listIngredient.add(in);
		} while (c.moveToNext());
		c.close();
		return listIngredient;
	}

	/**
	 * Cette methode permet de convertir un cursor en une liste de label
	 * 
	 * @param c
	 * @return
	 */
	private List<Label> cursorToListLabel(Cursor c) {
		if (c.getCount() == 0)
			return new ArrayList<Label>();
		List<Label> listLabel = new ArrayList<Label>();
		c.moveToFirst();
		do {
			Label la = new Label();
			la.setGuid(c.getInt(NUM_COL_ID));
			la.setNom(c.getString(NUM_COL_NOM));
			la.setSigle(c.getString(NUM_COL_SIGLE));
			la.setImagePath(c.getString(NUM_COL_IMAGELABEL));
			listLabel.add(la);
		} while (c.moveToNext());
		c.close();
		return listLabel;
	}

	/**
	 * Cette methode permet de convertir un cursor en une liste de label
	 * 
	 * @param c
	 * @return
	 */
	private List<Commentaire> cursorToListCommentaire(Cursor c) {
		if (c.getCount() == 0)
			return new ArrayList<Commentaire>();
		List<Commentaire> listCommentaire = new ArrayList<Commentaire>();
		c.moveToFirst();
		do {
			Commentaire co = new Commentaire();
			co.setGuid(c.getInt(NUM_COL_ID));
			co.setNbLike(c.getInt(NUM_COL_NBLIKE));
			co.setText(c.getString(NUM_COL_COMMENTAIRETEXT));
			co.setAuthor(c.getString(NUM_COL_PRENOM_COM));
			listCommentaire.add(co);
		} while (c.moveToNext());
		c.close();
		return listCommentaire;
	}

}
