package com.github.aprestaux.locations.overlays;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.github.aprestaux.locations.activities.MyMapActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> myOverlays;

	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		myOverlays = new ArrayList<OverlayItem>();
		populate();
	}
	
	public void addOverlay(OverlayItem overlay) {
		myOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return myOverlays.get(i);
	}

	@Override
	public int size() {
		return myOverlays.size();
	}

	//Handle tap events on overlay icons
	protected boolean onTap(int i){
		GeoPoint geoPoint = myOverlays.get(i).getPoint();
		double lat = geoPoint.getLatitudeE6() / 1E6;
		double lon = geoPoint.getLongitudeE6() / 1E6;
		String toast = "Title: " + myOverlays.get(i).getTitle();
		toast += "\nText: " + myOverlays.get(i).getSnippet();
		toast += "\nSymbol coordinates: Lat=" + lat + " Lon=" + lon + " (microdegrees)";
		Toast.makeText(MyMapActivity.context, toast, Toast.LENGTH_LONG).show();
		return true;
	}
}
