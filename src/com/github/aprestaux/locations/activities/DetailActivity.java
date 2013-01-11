package com.github.aprestaux.locations.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aprestaux.locations.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DetailActivity extends Activity {
	
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
        ImageView image = (ImageView) findViewById(R.id.image);
        
        Bundle extras = getIntent().getExtras();
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
        		// Load and display image asynchronously
        		imageLoader.displayImage(extraImage, image);
        	}
        }
	}

}
