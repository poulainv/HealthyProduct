package com.hp.interfaces;

import java.util.ArrayList;

import com.hp.client.resource.Ingredient;
import com.hp.client.resource.User;

/**
 * 
 * Interface permettant la manipulation d'un User par les Fragments
 * Doit etre implémenté par l'Activity
 * 
 * @author Lisional
 * 
 */

public interface onUserCompleteListener {
	
	public User user = new User("",-1,null);
	
	public void registerUserAndGo();

	public boolean isNameSet();

	public boolean isTypeSet();

	public void setName(String n);

	public void setIngredientListForbidden(ArrayList<Ingredient> l);

	public void setTypeConso(int t);

}
