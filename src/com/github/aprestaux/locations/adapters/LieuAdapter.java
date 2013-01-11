package com.github.aprestaux.locations.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.domain.Lieu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LieuAdapter extends BaseAdapter {
	
	List<Lieu> lieus;
	List<Lieu> publishedLieus;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	
	public LieuAdapter(Context context, List<Lieu> objects) {
		super();
		inflater = LayoutInflater.from(context);
		lieus = objects;
		publishedLieus = lieus;
		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		return publishedLieus.size();
	}

	@Override
	public Object getItem(int i) {
		return publishedLieus.get(i);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolderLieu {
		TextView tvTitre;
		TextView tvDescription;
		ImageView imgView;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Lieu i = publishedLieus.get(position);
		ViewHolderLieu holderLieu;
        if (i != null) {
        	if (convertView == null) {
    			holderLieu = new ViewHolderLieu();
    			convertView = inflater.inflate(R.layout.list_item, null);
    			holderLieu.tvTitre = (TextView) convertView.findViewById(R.id.title);
    			holderLieu.tvDescription = (TextView) convertView.findViewById(R.id.description);
    			holderLieu.imgView = (ImageView) convertView.findViewById(R.id.image);
    			convertView.setTag(holderLieu);
    		}else{
    			holderLieu = (ViewHolderLieu) convertView.getTag();
    		}
        	Lieu lieu = (Lieu) i;
    		holderLieu.tvTitre.setText(lieu.getNom());
    		holderLieu.tvDescription.setText(lieu.getQuartier() + " - " + lieu.getSecteur());
    		// Load and display image asynchronously
    		imageLoader.displayImage(lieu.getImage(), holderLieu.imgView);
        }
		return convertView;
	}
	
	private List<Lieu> getFilteredResults(CharSequence constraint) {
        List<Lieu> results = new ArrayList<Lieu>();
        if (constraint != null || constraint == "") {
        	for (int i=0; i<lieus.size(); i++) {
    			Lieu lieu = (Lieu)lieus.get(i);
                if (lieu.getNom().toLowerCase()
                        .contains(constraint.toString()) || lieu.getSecteur().toLowerCase().contains(constraint.toString())
                        || lieu.getQuartier().toLowerCase().contains(constraint.toString()) )
                    results.add(lieu);
    		}
        }else{
        	return lieus;
        }
		return results;
	}
	
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                publishedLieus = (List<Lieu>) results.values;
                LieuAdapter.this.notifyDataSetChanged();
                //publishedLieus = lieus;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Lieu> filteredResults = getFilteredResults(constraint);
                
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

}
