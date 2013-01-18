package com.github.aprestaux.locations.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.domain.BusinessLayer;
import com.github.aprestaux.locations.domain.Lieu;
import com.github.aprestaux.locations.overlays.MyItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity {
	
	private List<Overlay> mapOverlays;
	public static Context context;
	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
	private MyItemizedOverlay itemizedOverlay1;
	private MapView myMapView;
	private BusinessLayer coucheMetier = new BusinessLayer();
	
	protected boolean isRouteDisplayed() {
		return false;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		context = getApplicationContext();
		
		ArrayList<Lieu> lieus = coucheMetier.fetchLieusFromWebservice();
		for (int i=0; i<lieus.size(); i++) {
			double latitude = ((Lieu)lieus.get(i)).getLat()*1E6;
			double longitude = ((Lieu)lieus.get(i)).getLon()*1E6;
			items.add(new OverlayItem(new GeoPoint((int)Math.round(latitude), (int)Math.round(longitude)), ((Lieu)lieus.get(i)).getNom(), ((Lieu)lieus.get(i)).getInformations()));
		}
		
		myMapView = (MapView) findViewById(R.id.mapView);
		setOverlay();
		
		myMapView.setSatellite(true);
		myMapView.setBuiltInZoomControls(true);
		
		final MapController mControl = myMapView.getController();
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	mControl.setCenter(new GeoPoint((int)Math.round(extras.getDouble("lat")), (int)Math.round(extras.getDouble("lon"))));
        	mControl.setZoom(20);
        }else{
        	mControl.setCenter(new GeoPoint((int)Math.round(4.888763328727749E7), (int)Math.round(2247787.7140045166)));
        	mControl.setZoom(15);
        }
		
		
		final MyLocationOverlay myLocation = new MyLocationOverlay(getApplicationContext(), myMapView);
		myMapView.getOverlays().add(myLocation);
		myLocation.enableMyLocation();
	
	}
	
	public void setOverlay() {
		mapOverlays = myMapView.getOverlays();
		Drawable drawable1 = this.getResources().getDrawable(R.drawable.ic_pinpoint);
		itemizedOverlay1 = new MyItemizedOverlay(drawable1);
		for (int i=0; i<items.size(); i++) {
			itemizedOverlay1.addOverlay(items.get(i));
		}
		mapOverlays.add(itemizedOverlay1);
		myMapView.postInvalidate();
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
	    if (item.getItemId() == R.id.menu_favoris) {
	        startActivity(new Intent(this, FavorisActivity.class));
	    }
	    return super.onOptionsItemSelected(item);
	}
}
