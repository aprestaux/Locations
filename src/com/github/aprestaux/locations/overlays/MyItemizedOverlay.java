package com.github.aprestaux.locations.overlays;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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
	private Context context;
	
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		myOverlays = new ArrayList<OverlayItem>();
		this.context = context;
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

	protected boolean onTap(int i){
		int index = Integer.parseInt(myOverlays.get(i).getSnippet());
		Lieu lieu = lieus.get(index);
		Intent monIntent = coucheMetier.getDetailIntent(context, lieu);
		monIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(monIntent);
		return true;
	}
}
