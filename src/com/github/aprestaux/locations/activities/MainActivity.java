package com.github.aprestaux.locations.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.adapters.LieuAdapter;
import com.github.aprestaux.locations.domain.BusinessLayer;
import com.github.aprestaux.locations.domain.Lieu;


public class MainActivity extends Activity {
	Lieu lieu;
	ArrayList<Lieu> lieuArray;
	LieuAdapter adapter;
	BusinessLayer coucheMetier = BusinessLayer.getInstance();
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView myListView = (ListView) findViewById(R.id.listView);
		lieuArray = coucheMetier.getLieuArray();
		adapter = new LieuAdapter(this, lieuArray);
		myListView.setAdapter(adapter);

		myListView.setTextFilterEnabled(true);
		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent monIntent = coucheMetier.getDetailIntent(
						MainActivity.this, ((Lieu) adapter.getItem(position)));
				startActivity(monIntent);
			}
		});

		EditText myEditText = (EditText) findViewById(R.id.editText);
		myEditText.addTextChangedListener(searchTextWatcher);
		
		addItemsOnSpinner();
		addListenerOnSpinnerItemSelection();

	}

	private TextWatcher searchTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// ignore
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
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

	public void addItemsOnSpinner() {
		String cat;
		spinner = (Spinner) findViewById(R.id.spinner);
		List<String> listCat = new ArrayList<String>();
		listCat.add("Catégories");
		List<Character> listCatId = new ArrayList<Character>();
		for (int i=0;i<lieuArray.size();i++) {
			cat = lieuArray.get(i).getCategorie();
		    for (int j = 0; j < cat.length(); j++) {
		        Character character = cat.charAt(j);
		        if (Character.isDigit(character)) {
		            if (!listCatId.contains(character)) {
		            	listCatId.add(character);
		            	listCat.add("Catégorie " + character);
		            }
		        }
		    }
		}
		Collections.sort(listCat);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listCat);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection() {
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				adapter.getFilter().filter("FILTERCATEGORY" + String.valueOf(pos));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
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
