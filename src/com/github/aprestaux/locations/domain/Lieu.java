package com.github.aprestaux.locations.domain;

public class Lieu {
	private String nom;
	private long lat;
	private long lon;
	private int categorie_id;
	private String image;
	private String secteur;
	private String quartier;
	private String informations;
	
	
	public String getNom() {
		return nom;
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

	
	public Lieu(String nom, long lat, long lon, String secteur, String quartier, int categorie_id, String image, String info) {
		super();
		this.nom = nom;
		this.lat = lat;
		this.lon = lon;
		this.secteur = secteur;
		this.quartier = quartier;
		this.categorie_id = categorie_id;
		this.image = image;
		this.informations = info;
	}

}
