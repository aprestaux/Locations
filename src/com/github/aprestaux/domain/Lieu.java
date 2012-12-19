package com.github.aprestaux.domain;

public class Lieu {
	private String nom;
	private long lat;
	private long lon;
	private String secteur;
	private String quartier;
	private String informations;
	
	public Lieu(String nom, long lat, long lon, String secteur, String quartier) {
		super();
		this.nom = nom;
		this.lat = lat;
		this.lon = lon;
		this.secteur = secteur;
		this.quartier = quartier;
	}

	public String getInformations() {
		return informations;
	}

	public void setInformations(String informations) {
		this.informations = informations;
	}

}
