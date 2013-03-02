package com.hp.client;

import java.util.List;

import com.hp.adapters.HistoryAdapter;
import com.hp.client.dao.DAO;
import com.hp.client.resource.Produit;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

public class HistoryActivity extends ListActivity{
	
	 private HistoryAdapter mHistoryAdapter;
	 private DAO dao ;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        dao = DAO.getInstance(this);
		dao.open();
		List<Produit> liste = dao.getProductHistorique();
		setContentView(R.layout.history_list);
		// Si ma liste est non vide
		if(liste.size() != 0){
			
	        mHistoryAdapter = new HistoryAdapter(liste,this);
	        setListAdapter(mHistoryAdapter);
	        
	        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					ProductChecker prod = new ProductChecker(HistoryActivity.this, mHistoryAdapter.getItemEan(arg3));//"3017620424403");
					prod.execute();
				}
			});
		}else{
			this.getListView().setEmptyView(findViewById(R.id.empty_list_view));
		}
		Button back2 = (Button) findViewById(R.id.history_button_back);
		back2.setVisibility(View.VISIBLE);
		
		back2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HistoryActivity.this.finish();

			}
		});
    }
}
