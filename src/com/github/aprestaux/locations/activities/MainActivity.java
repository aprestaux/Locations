package com.github.aprestaux.locations.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aprestaux.domain.Lieu;
import com.github.aprestaux.locations.R;

public class MainActivity extends Activity {
	HttpClient httpClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet("http://cci.corellis.eu/pois.php");
	List<Lieu> lieuArray = new ArrayList<Lieu>();
	Lieu lieu;
	List<String> titleArray = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		
		 try {
	        	HttpResponse response = httpClient.execute(httpGet);
	        	if (response != null) {
	        		String line = "";
	        		InputStream inputStream = response.getEntity().getContent();
	        		line = convertStreamToString(inputStream);
	        		JSONObject jsonObject1 = new JSONObject(line);
	        		Log.d("JSON", line);
	    			JSONArray jsonArray = jsonObject1.getJSONArray("results");
	        		for (int i=0; i<jsonArray.length(); i++) {
	        			JSONObject jsonObject = jsonArray.getJSONObject(i);
	        			lieu = new Lieu(jsonObject.getString("nom"), jsonObject.getLong("lat"), jsonObject.getLong("lon"), jsonObject.getString("secteur"), jsonObject.getString("quartier"));
	        			lieuArray.add(lieu);
	        			titleArray.add(jsonObject.getString("nom"));
	        		}
	        	}
	        }catch(Exception e) {}
	        
	        ListView myListView = (ListView) findViewById(R.id.listView);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, titleArray);
	        myListView.setAdapter(adapter);
	        
	        myListView.setTextFilterEnabled(true);
	        myListView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
	        	}
			});
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.menu_liste) {
	    	Log.d("MENU", "Liste");
	        //startActivity(new Intent(this, CoursesActivity.class));
	    }
	    if (item.getItemId() == R.id.menu_carte) {
	    	Log.d("MENU", "Carte");
	        //startActivity(new Intent(this, ScoresActivity.class));
	    }
	    if (item.getItemId() == R.id.menu_favoris) {
	    	Log.d("MENU", "Favoris");
	        //startActivity(new Intent(this, HandicapActivity.class));
	    }
	    return super.onOptionsItemSelected(item);
	}

}
