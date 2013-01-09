package com.github.aprestaux.locations.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.adapters.LieuAdapter;
import com.github.aprestaux.locations.domain.Item;
import com.github.aprestaux.locations.domain.Lieu;

public class MainActivity extends Activity {
	HttpClient httpClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet("http://cci.corellis.eu/pois.php");
	Lieu lieu;
	ArrayList<Item> lieuArray = new ArrayList<Item>();
	LieuAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().build());
		
		 try {
	        	HttpResponse response = httpClient.execute(httpGet);
	        	if (response != null) {
	        		String line = "";
	        		InputStream inputStream = response.getEntity().getContent();
	        		line = convertStreamToString(inputStream);
	        		JSONObject jsonObject1 = new JSONObject(line);
	    			JSONArray jsonArray = jsonObject1.getJSONArray("results");
	        		for (int i=0; i<jsonArray.length(); i++) {
	        			JSONObject jsonObject = jsonArray.getJSONObject(i);
	        			lieu = new Lieu(jsonObject.getString("nom"),
	        					jsonObject.getLong("lat"), 
	        					jsonObject.getLong("lon"), 
	        					jsonObject.getString("secteur"),
	        					jsonObject.getString("quartier"),
	        					jsonObject.getInt("categorie_id"),
	        					jsonObject.getString("image"),
	        					jsonObject.getString("informations"));
	        			lieuArray.add(lieu);
	        		}
	        	}
	        }catch(Exception e) {}
			
		 	ListView myListView = (ListView) findViewById(R.id.listView);
		 	adapter = new LieuAdapter(this, lieuArray);
			myListView.setAdapter(adapter);
			
			myListView.setTextFilterEnabled(true);
	        myListView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		Intent monIntent = new Intent(MainActivity.this, DetailActivity.class);
	        		monIntent.putExtra("nom", ((Lieu)adapter.getItem(position)).getNom());
	        		monIntent.putExtra("quartier", ((Lieu)adapter.getItem(position)).getQuartier());
	        		monIntent.putExtra("secteur", ((Lieu)adapter.getItem(position)).getSecteur());
	        		monIntent.putExtra("info", ((Lieu)adapter.getItem(position)).getInformations());
	        		monIntent.putExtra("image", ((Lieu)adapter.getItem(position)).getImage());
	        		startActivity(monIntent);
	        	}
			});
	        
	        EditText myEditText = (EditText) findViewById(R.id.editText);
	        myEditText.addTextChangedListener(searchTextWatcher);
	        
	}
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	private TextWatcher searchTextWatcher = new TextWatcher() {
	    @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	            // ignore
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
	    	Log.d("MENU", "Favoris");
	        //startActivity(new Intent(this, HandicapActivity.class));
	    }
	    return super.onOptionsItemSelected(item);
	}

}
