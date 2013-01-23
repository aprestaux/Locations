package com.github.aprestaux.locations.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.domain.BusinessLayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DetailActivity extends Activity {
	BusinessLayer coucheMetier = new BusinessLayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        ActionBar bar = getActionBar();
        bar.hide();
        
        // Get singletone instance of ImageLoader
 		ImageLoader imageLoader = ImageLoader.getInstance();
 		// Initialize ImageLoader with configuration. Do it once.
 		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        
        TextView nom = (TextView) findViewById(R.id.textNom);
        TextView quartier = (TextView) findViewById(R.id.textQuartier);
        TextView secteur = (TextView) findViewById(R.id.textSecteur);
        TextView info = (TextView) findViewById(R.id.textInfo);
        TextView cat = (TextView) findViewById(R.id.txtCat);
        ImageView image = (ImageView) findViewById(R.id.image);
        
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	String extraNom = extras.getString("nom");
        	if (extraNom != null){
        		nom.setText(extraNom);
        	}
        	String extraQuartier = extras.getString("quartier");
        	if (extraQuartier != null){
        		quartier.setText(extraQuartier);
        	}
        	String extraSecteur = extras.getString("secteur");
        	if (extraSecteur != null){
        		secteur.setText(extraSecteur);
        	}
        	String extraInfo = extras.getString("info");
        	if (extraInfo != null){
        		info.setText(extraInfo.replace("</br>", ". "));
        	}
        	String extraImage = extras.getString("image");
        	if (extraImage != null){
        		imageLoader.displayImage(extraImage, image);
        	}
        	String extraCat = extras.getString("cat");
        	if (extraCat != null){
        		cat.setText(extraCat);
        	}
        }
        
        final Button buttonFavoris = (Button) findViewById(R.id.buttonFavoris);
        String favoris = coucheMetier.getFavoris(getApplicationContext());
        if (favoris.indexOf("," + extras.getString("id") + ",") >= 0) {
        	buttonFavoris.setEnabled(false);
        }
        buttonFavoris.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		String favoris = coucheMetier.getFavoris(getApplicationContext());
        		favoris += "," + extras.getString("id") + ",";
			    coucheMetier.editFavoris(getApplicationContext(), favoris);
			    buttonFavoris.setEnabled(false);
			    Toast.makeText(v.getContext(), extras.getString("nom") + " a bien été ajouté aux favoris.", Toast.LENGTH_SHORT).show();
        	}
        });
        
        Button buttonYAller = (Button) findViewById(R.id.buttonYAller);
        buttonYAller.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		String url = "http://maps.google.com/maps?" + extras.getString("url");
        		Intent monIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        		startActivity(monIntent);
        	}
        });
        
        Button buttonCarte = (Button) findViewById(R.id.buttonCarte);
        buttonCarte.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent monIntent = new Intent(DetailActivity.this, MyMapActivity.class);
        		monIntent.putExtra("lat", extras.getDouble("lat"));
        		monIntent.putExtra("lon", extras.getDouble("lon"));
        		startActivity(monIntent);
        	}
        });
	}

}
