package com.github.aprestaux.locations.overlays;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.github.aprestaux.locations.activities.MyMapActivity;
import com.github.aprestaux.locations.domain.BusinessLayer;
import com.github.aprestaux.locations.domain.Lieu;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> myOverlays;
	private BusinessLayer coucheMetier = BusinessLayer.getInstance();
	private ArrayList<Lieu> lieus = coucheMetier.getLieuArray();
	
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
		int index = Integer.parseInt(myOverlays.get(i).getSnippet());
		Lieu lieu = lieus.get(index);
		String toast = lieu.getNom();
		toast += "\n" + lieu.getInformations();
		Toast.makeText(MyMapActivity.context, toast, Toast.LENGTH_LONG).show();
		return true;
	}
}
