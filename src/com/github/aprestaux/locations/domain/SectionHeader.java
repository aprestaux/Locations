package com.github.aprestaux.locations.domain;

public class SectionHeader implements Item{
	private String nom;

	@Override
	public boolean isSection() {
		return true;
	}

	public String getNom() {
		return nom;
	}

	public SectionHeader(String nom) {
		super();
		this.nom = nom;
	}
	
	

}
