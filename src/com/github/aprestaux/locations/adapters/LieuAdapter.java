package com.github.aprestaux.locations.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.domain.Item;
import com.github.aprestaux.locations.domain.Lieu;
import com.github.aprestaux.locations.domain.SectionHeader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LieuAdapter extends BaseAdapter {
	
	List<Item> items;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	
	public LieuAdapter(Context context, List<Item> objects) {
		super();
		inflater = LayoutInflater.from(context);
		items = objects;
		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int i) {
		return items.get(i);
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
	
	private class ViewHolderSection {
		TextView tvTitre;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item i = items.get(position);
		ViewHolderLieu holderLieu;
		ViewHolderSection holderSection;
        if (i != null) {
            if(i.isSection()){
            	if (convertView == null) {
        			holderSection = new ViewHolderSection();
        			convertView = inflater.inflate(R.layout.list_item_section, null);
        			convertView.setTag(holderSection);
        		}else{
        			holderSection = (ViewHolderSection) convertView.getTag();
        		}
            	SectionHeader section = (SectionHeader) i;
        		holderSection.tvTitre.setText(section.getNom());       	
            }else{
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
        }
		
		return convertView;
	}
	
	private List<Item> getFilteredResults(CharSequence constraint) {
		return items.subList(0, 5);
	}
	
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (List<Item>) results.values;
                LieuAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Item> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

}
