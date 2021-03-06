package com.github.aprestaux.locations.domain;

public class Lieu {
	private int id;
	private String nom;
	private String categorie;
	private double lat;
	private double lon;
	private String image;
	private String secteur;
	private String quartier;
	private String informations;
	
	public int getId() {
		return id;
	}
	
	public String getNom() {
		return nom;
	}

	public String getCategorie() {
		return categorie;
	}

	public String getSecteur() {
		return secteur;
	}

	public String getQuartier() {
		return quartier;
	}
	
	public String getImage() {
		return image;
	}

	public String getInformations() {
		return informations;
	}
	
	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public Lieu(int id, String nom, String categorie, double lat, double lon, String secteur, String quartier, String image, String info) {
		super();
		this.id = id;
		this.nom = nom;
		this.categorie = categorie;
		this.lat = lat;
		this.lon = lon;
		this.secteur = secteur;
		this.quartier = quartier;
		this.image = image;
		this.informations = info;
	}

}
