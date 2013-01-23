package com.github.aprestaux.locations.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aprestaux.locations.R;
import com.github.aprestaux.locations.domain.Lieu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LieuAdapter extends BaseAdapter {
	
	List<Lieu> lieus;
	List<Lieu> publishedLieus;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	SharedPreferences.Editor editor;
	String favoris;
	
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
		TextView txtCat;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SharedPreferences settings = parent.getContext().getSharedPreferences("FAVORIS", 0);
	    editor = settings.edit();
		favoris = settings.getString("favoris", "");
		Lieu i = publishedLieus.get(position);
		ViewHolderLieu holderLieu;
        if (i != null) {
        	if (convertView == null) {
    			holderLieu = new ViewHolderLieu();
    			convertView = inflater.inflate(R.layout.list_item, null);
    			holderLieu.tvTitre = (TextView) convertView.findViewById(R.id.title);
    			holderLieu.txtCat = (TextView) convertView.findViewById(R.id.txtCat);
    			holderLieu.tvDescription = (TextView) convertView.findViewById(R.id.description);
    			holderLieu.imgView = (ImageView) convertView.findViewById(R.id.image);
    			convertView.setTag(holderLieu);
    		}else{
    			holderLieu = (ViewHolderLieu) convertView.getTag();
    		}
        	final Context currentContext = convertView.getContext();
        	final Lieu lieu = (Lieu) i;
    		holderLieu.tvTitre.setText(lieu.getNom());
    		holderLieu.tvDescription.setText(lieu.getQuartier() + " - " + lieu.getSecteur());
    		holderLieu.txtCat.setText("Cat. " + lieu.getCategorie().replace(" et ", ", "));
    		ImageView imgFavorisAdd = (ImageView) convertView.findViewById(R.id.imgVwFavorisAdd);
    		if (favoris.indexOf("," + String.valueOf(lieu.getId()) + ",") >= 0) {
    			imgFavorisAdd.setImageResource(R.drawable.ic_favorited);
    		}else{
    			imgFavorisAdd.setImageResource(R.drawable.ic_favoris_add);
    			imgFavorisAdd.setOnClickListener(new OnClickListener() {
        		    public void onClick(View v) {
        		    	favoris += "," + String.valueOf(lieu.getId()) + ",";
        			    editor.putString("favoris", favoris);
        			    editor.commit();
        			    ((ImageView) v).setImageResource(R.drawable.ic_favorited);
        			    Toast.makeText(currentContext, lieu.getNom() + " a bien été ajouté aux favoris.", Toast.LENGTH_SHORT).show();
        		    }
        		});
    		}
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
	
	private List<Lieu> getCategoryFilteredResults(CharSequence constraint) {
        List<Lieu> results = new ArrayList<Lieu>();
        String cat = constraint.toString().replace("FILTERCATEGORY", "");
        if (cat.contains("0")) {
        	results = lieus;
        }else{
	    	for (int i=0; i<lieus.size(); i++) {
				Lieu lieu = (Lieu)lieus.get(i);
				if (lieu.getCategorie().contains(cat)) {
	                results.add(lieu);
				}
			}
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
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	List<Lieu> filteredResults;
            	if (constraint.toString().contains("FILTERCATEGORY")) {
            		filteredResults = getCategoryFilteredResults(constraint);
            	}else{
            		filteredResults = getFilteredResults(constraint);
            	}

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

}
