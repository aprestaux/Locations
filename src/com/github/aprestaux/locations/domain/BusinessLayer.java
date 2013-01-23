package com.github.aprestaux.locations.domain;

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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.github.aprestaux.locations.activities.DetailActivity;

public class BusinessLayer {
	HttpClient httpClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet("http://cci.corellis.eu/pois.php");
	Lieu lieu;
	ArrayList<Lieu> lieuArray = new ArrayList<Lieu>();

	public ArrayList<Lieu> fetchLieusFromWebservice() {
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				String line = "";
				InputStream inputStream = response.getEntity().getContent();
				line = convertStreamToString(inputStream);
				JSONObject jsonObject1 = new JSONObject(line);
				JSONArray jsonArray = jsonObject1.getJSONArray("results");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					lieu = new Lieu(jsonObject.getInt("id"),
							jsonObject.getString("nom"),
							jsonObject.getString("categorie_id"),
							jsonObject.getDouble("lat"),
							jsonObject.getDouble("lon"),
							jsonObject.getString("secteur"),
							jsonObject.getString("quartier"),
							jsonObject.getString("image"),
							jsonObject.getString("informations"));
					lieuArray.add(lieu);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return lieuArray;
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
	
	public Intent getDetailIntent(Activity fromActivity, Lieu lieu) {
		GPSTracker mGPS = new GPSTracker(fromActivity);
		final double mLat;
		final double mLong;
		if(mGPS.canGetLocation() ){

			mLat=mGPS.getLatitude();
			mLong=mGPS.getLongitude();

		}else{
			mLat=0;
			mLong=0;
		}
		
		Intent monIntent = new Intent(fromActivity, DetailActivity.class);
		monIntent.putExtra("nom", lieu.getNom());
		monIntent.putExtra("cat", "Cat. " + lieu.getCategorie());
		monIntent.putExtra("quartier", lieu.getQuartier());
		monIntent.putExtra("secteur", lieu.getSecteur());
		monIntent.putExtra("info", lieu.getInformations());
		monIntent.putExtra("image", lieu.getImage());
		monIntent.putExtra("id", String.valueOf(lieu.getId()));
		monIntent.putExtra("lat", lieu.getLat()*1E6);
		monIntent.putExtra("lon", lieu.getLon()*1E6);
		String locationUrl = "saddr=" + mLat + "," + mLong + "&daddr=" + lieu.getLat() + "," + lieu.getLon();
		monIntent.putExtra("url", locationUrl);
		return monIntent;
	}
	
	public String getFavoris(Context context) {
        SharedPreferences settings = context.getSharedPreferences("FAVORIS", 0);
		String favoris = settings.getString("favoris", "");
		return favoris;
	}
	
	public void editFavoris(Context context, String newString) {
		SharedPreferences settings = context.getSharedPreferences("FAVORIS", 0);
        SharedPreferences.Editor editor = settings.edit();
		String favoris = settings.getString("favoris", "");
		favoris = newString;
	    editor.putString("favoris", favoris);
	    editor.commit();
	}
	
	public void confirmCancelFavorisDialog(final int lieuId, final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		DialogInterface.OnClickListener onPositiveClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				BusinessLayer coucheMetier = new BusinessLayer();
				String favoris = coucheMetier.getFavoris(context);
				favoris = favoris.replace("," + String.valueOf(lieuId) + ",", "");
				coucheMetier.editFavoris(context, favoris);
				Toast.makeText(context, "Ce lieu a bien ŽtŽ supprimŽ des favoris.", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		};
		builder.setMessage("Supprimer des favoris ?");
		builder.setPositiveButton("Oui", onPositiveClickListener);
		builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		builder.create();
		builder.show();
	}
}
