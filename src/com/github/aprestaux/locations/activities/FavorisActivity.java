package com.github.aprestaux.locations.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.adapters.LieuFavorisAdapter;
import com.github.aprestaux.locations.domain.BusinessLayer;
import com.github.aprestaux.locations.domain.Lieu;

public class FavorisActivity extends Activity {
	Lieu lieu;
	ArrayList<Lieu> lieuArray = new ArrayList<Lieu>();
	ArrayList<Lieu> lieuFavorisArray = new ArrayList<Lieu>();
	LieuFavorisAdapter adapter;
	BusinessLayer coucheMetier = new BusinessLayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favoris);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().build());
	    
		String favoris = coucheMetier.getFavoris(getApplicationContext());
		lieuArray = coucheMetier.fetchLieusFromWebservice();
		for (int i=0;i<lieuArray.size(); i++) {
			lieu = lieuArray.get(i);
			if (favoris.indexOf("," + String.valueOf(lieu.getId()) + ",") >= 0) {
				lieuFavorisArray.add(lieu);
			}
		}
		
		
		ListView myListView = (ListView) findViewById(R.id.listView);
	 	adapter = new LieuFavorisAdapter(this, lieuFavorisArray);
		myListView.setAdapter(adapter);
		
		myListView.setTextFilterEnabled(true);
        myListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Intent monIntent = coucheMetier.getDetailIntent(FavorisActivity.this, ((Lieu)adapter.getItem(position)));
        		startActivity(monIntent);
        	}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.menu_liste) {
	        startActivity(new Intent(this, MainActivity.class));
	    }
	    if (item.getItemId() == R.id.menu_carte) {
	        startActivity(new Intent(this, MyMapActivity.class));
	    }
	    return super.onOptionsItemSelected(item);
	}

}
