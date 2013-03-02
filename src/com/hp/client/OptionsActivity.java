package com.hp.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hp.adapters.ListeIngredientAdapter;
import com.hp.client.dao.DAO;
import com.hp.client.resource.Ingredient;
import com.hp.client.resource.User;

public class OptionsActivity extends Activity {

	private final String TAG = "OptionsActivity";
	DAO dao;
	private Button name = null;
	private Button profil = null;
	private Button ingredients = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.options_list);

		name = (Button) findViewById(R.id.options_change_name);
		profil = (Button) findViewById(R.id.options_change_profil);
		ingredients = (Button) findViewById(R.id.options_change_ingredients);

		dao = DAO.getInstance(this);
		dao.open();
		final User u = dao.getUser(1);
		dao.close();

		initialiseButtons(u);

		name.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(OptionsActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
				adb.setTitle("Prénom");
				final EditText e = new EditText(OptionsActivity.this);
				e.setText(u.getPrenom());
				e.setPadding(10, 10, 10, 10);
				adb.setView(e);

				adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (e.getText().toString().contentEquals(""))
							return;
						else {
							name.setText(e.getText().toString());
							u.setPrenom(e.getText().toString());
							dao.open();
							dao.updateUser(1, u);
							dao.close();
						}
					}
				});
				adb.show();
			}
		});

		profil.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(new ContextThemeWrapper(OptionsActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
				adb1.setTitle("Profil");
				final RadioGroup r = new RadioGroup(OptionsActivity.this);
				final RadioButton[] rb = new RadioButton[4];
				rb[0] = new RadioButton(OptionsActivity.this);
				rb[0].setText("Econome");
				r.addView(rb[0]);
				rb[1] = new RadioButton(OptionsActivity.this);
				rb[1].setText("Eco-Friendly");
				r.addView(rb[1]);
				rb[2] = new RadioButton(OptionsActivity.this);
				rb[2].setText("Fat-Watcher");
				r.addView(rb[2]);
				rb[3] = new RadioButton(OptionsActivity.this);
				rb[3].setText("Qualité");
				r.addView(rb[3]);
				rb[u.getTypeconso()].setChecked(true);
				adb1.setView(r);
				adb1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (rb[0].isChecked()) {
							profil.setText(getResources().getString(R.string.frag_choixprofil_econome));
							u.setTypeconso(0);
						} else if (rb[1].isChecked()) {
							profil.setText(getResources().getString(R.string.frag_choixprofil_ecofriend));
							u.setTypeconso(1);
						} else if (rb[2].isChecked()) {
							profil.setText(getResources().getString(R.string.frag_choixprofil_fat));
							u.setTypeconso(2);
						} else {
							profil.setText(getResources().getString(R.string.frag_choixprofil_qualite));
							u.setTypeconso(3);
						}
						dao.open();
						dao.updateUser(1, u);
						dao.close();
					}
				});
				adb1.show();

			}
		});

		ingredients.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final Dialog d = new Dialog(OptionsActivity.this);
				d.setTitle("Ingrédients");
				d.setContentView(R.layout.dialog_list_ingredients);

				ListView list = (ListView) d.findViewById(R.id.lvListe);

				final ArrayList<Ingredient> listeIngredients = (ArrayList<Ingredient>) u.getIngredientForbiddenList();

				final ListeIngredientAdapter adapter = new ListeIngredientAdapter(OptionsActivity.this, R.layout.frag_ingredients_rows, listeIngredients);
				list.setEmptyView(d.findViewById(R.id.empty_list_view));
				list.setAdapter(adapter);

				final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) d.findViewById(R.id.input);

				dao.open();
				final ArrayList<Ingredient> allIngredients = (ArrayList<Ingredient>) dao.getListAllIngredient();
				ArrayList<String> dropdownList = createArrayOfIngredients(allIngredients);

				autoCompleteTextView.setAdapter(new ArrayAdapter<String>(OptionsActivity.this, android.R.layout.simple_dropdown_item_1line, dropdownList));
				autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						int position = -1;
						for (int i = 0; i < allIngredients.size(); ++i) {
							if (allIngredients.get(i).getNom().contentEquals(autoCompleteTextView.getText().toString()) && !listeIngredients.contains(allIngredients.get(i)))
								position = i;
						}

						if (position != -1)
							listeIngredients.add(allIngredients.get(position));

						adapter.notifyDataSetChanged();
						autoCompleteTextView.setText("");

					}
				});

				Button valid = (Button) d.findViewById(R.id.valid_button);

				valid.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						u.setIngredientForbiddenList(listeIngredients);
						dao.open();
						dao.updateUser(1, u);
						dao.close();
						if (u.getIngredientForbiddenList().isEmpty())
							ingredients.setText("Vide");
						else
							ingredients.setText(u.getIngredientForbiddenList().get(0).getNom() + "...");

						d.dismiss();
					}
				});
				d.show();

			}
		});
	}

	private void initialiseButtons(User u) {
		name.setText(u.getPrenom());

		switch (u.getTypeconso()) {
		case 0:
			profil.setText(this.getResources().getString(R.string.frag_choixprofil_econome));
			break;
		case 1:
			profil.setText(this.getResources().getString(R.string.frag_choixprofil_ecofriend));
			break;
		case 2:
			profil.setText(this.getResources().getString(R.string.frag_choixprofil_fat));
			break;
		case 3:
			profil.setText(this.getResources().getString(R.string.frag_choixprofil_qualite));
			break;
		}

		if (u.getIngredientForbiddenList().isEmpty())
			ingredients.setText("Vide");
		else
			ingredients.setText(u.getIngredientForbiddenList().get(0).getNom() + "...");

	}

	private ArrayList<String> createArrayOfIngredients(List<Ingredient> l) {
		ArrayList<String> s = new ArrayList<String>();
		for (int i = 0; i < l.size(); ++i) {
			s.add(l.get(i).getNom());
		}
		return s;
	}
}
