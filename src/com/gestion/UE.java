package com.gestion;
public class UE {
	private String nom;
	
	private boolean estValide;

	public static enum types {
		IMPOSEE, CHOIX, COMMUN
	};

	private types type;
	
	public UE(String nom) {
		this.nom = nom;
		estValide = false;
	}

	public UE(types type, String nom) {
		this.type = type;
		this.nom = nom;
		this.estValide = false;
	}
	
	public UE(types type, String nom, boolean estValide) {
		this.nom = nom;
		this.estValide = estValide;
		this.type = type ; 
	}

	public types getType() {
		return type;
	}

	public void setType(types type) {
		this.type = type;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	
	//Retourne le nom de l'UE à partir d'une chaine de caractère spécifique 
	public static String parseNomUE(String chaine) {
		String result = chaine;
		result = result.substring(result.indexOf("[") + 1);
		result = result.substring(0, result.indexOf("]"));
		return result;
	}
	
	
	//Retourne le type de l'UE à partir d'une chaine de caractère spécifique 
	public static types getType(String chaine) {
		if (chaine.toLowerCase().contains("choix"))
			return types.CHOIX;
		else if (chaine.toLowerCase().contains("impo"))
			return types.IMPOSEE;
		else if (chaine.toLowerCase().contains("commun"))
			return types.COMMUN;
		return types.CHOIX; // ne devrait jamais arriver
	}

	public boolean isValide() {
		return estValide;
	}

	public void setValide(boolean estValide) {
		this.estValide = estValide;
	}

	//Methode d'affichage de l'UE 
	public String toString() {
		String var = "oui" ; 
		if (!estValide)
			var = "non"; 
		return (" UE : " + nom + " type : " + type + " Valid�e : "+ var + "\n");
	}
}
