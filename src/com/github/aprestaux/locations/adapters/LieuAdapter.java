package com.github.aprestaux.locations.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.domain.Lieu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LieuAdapter extends BaseAdapter {
	
	List<Lieu> lieus;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	
	public LieuAdapter(Context context, List<Lieu> objects) {
		super();
		inflater = LayoutInflater.from(context);
		lieus = objects;
		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		return lieus.size();
	}

	@Override
	public Object getItem(int i) {
		return lieus.get(i);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item, null);
			holder.tvTitre = (TextView) convertView.findViewById(R.id.title);
			holder.tvDescription = (TextView) convertView.findViewById(R.id.description);
			holder.imgView = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Lieu lieu = lieus.get(position);
		holder.tvTitre.setText(lieu.getNom());
		holder.tvDescription.setText(lieu.getQuartier() + " - " + lieu.getSecteur());
		// Load and display image asynchronously
		imageLoader.displayImage(lieu.getImage(), holder.imgView);
		
		return convertView;
	}

	private class ViewHolder {
		TextView tvTitre;
		TextView tvDescription;
		ImageView imgView;
	}

}
