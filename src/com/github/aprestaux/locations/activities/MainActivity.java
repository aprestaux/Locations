package com.github.aprestaux.locations.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.adapters.LieuAdapter;
import com.github.aprestaux.locations.domain.BusinessLayer;
import com.github.aprestaux.locations.domain.Lieu;

public class MainActivity extends Activity {
	Lieu lieu;
	ArrayList<Lieu> lieuArray = new ArrayList<Lieu>();
	LieuAdapter adapter;
	BusinessLayer coucheMetier = new BusinessLayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().build());
	    
		 
			
		 	ListView myListView = (ListView) findViewById(R.id.listView);
		 	lieuArray = coucheMetier.fetchLieusFromWebservice();
		 	adapter = new LieuAdapter(this, lieuArray);
			myListView.setAdapter(adapter);
			
			myListView.setTextFilterEnabled(true);
	        myListView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		Intent monIntent = coucheMetier.getDetailIntent(MainActivity.this, ((Lieu)adapter.getItem(position)));
	        		startActivity(monIntent);
	        	}
			});
	        
	        EditText myEditText = (EditText) findViewById(R.id.editText);
	        myEditText.addTextChangedListener(searchTextWatcher);
	        
	}
	
	private TextWatcher searchTextWatcher = new TextWatcher() {
	    @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	    		//ignore
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	            // ignore
	        }

	        @Override
	        public void afterTextChanged(Editable s) {
	            adapter.getFilter().filter(s.toString());
	        }
	    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.menu_carte) {
	        startActivity(new Intent(this, MyMapActivity.class));
	    }
	    if (item.getItemId() == R.id.menu_favoris) {
	        startActivity(new Intent(this, FavorisActivity.class));
	    }
	    return super.onOptionsItemSelected(item);
	}

}
