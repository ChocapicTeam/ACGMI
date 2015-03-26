package com.specialite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.gestion.UE;

public abstract class Specialite {
	String nom;
	ArrayList<UE> lstUE;
	static HashMap<String, ArrayList<UE>> map = new HashMap<String, ArrayList<UE>>();


	public Specialite(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public static UE.types getTypeUE(String spec, UE ue) {

		if (map.isEmpty())
			initMap();

		Set<Entry<String, ArrayList<UE>>> entry = map.entrySet();
		for (Entry<String, ArrayList<UE>> ent : entry) {
			if (ent.getKey().toLowerCase().equals(spec.toLowerCase())) {
				for (int i = 0; i < ent.getValue().size(); i++) {
					if (ent.getValue().get(i).getNom().toLowerCase().equals(ue.getNom().toLowerCase()))
						return ent.getValue().get(i).getType();
				}
			}
		}
		return UE.types.CHOIX;
	}

	private static void initMap()
	{
		initMapCamsi();
		initMapDl();
		initMapIarf();
		initMapIhm();
		initMapIm();

	}


	private static void initMapCamsi() {
		ArrayList <UE> lstUECamsi = new ArrayList<UE>(); 
		lstUECamsi.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUECamsi.add(new UE(UE.types.COMMUN, "TER"));
		lstUECamsi.add(new UE(UE.types.IMPOSEE, "AHP"));
		lstUECamsi.add(new UE(UE.types.IMPOSEE, "AS"));
		lstUECamsi.add(new UE(UE.types.IMPOSEE, "CSR"));
		lstUECamsi.add(new UE(UE.types.IMPOSEE, "IATI"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "RO"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "MCPOOA"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "3DIS"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "NSTR"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "TAS"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "OPP"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "MPI"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "IR"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "SPR"));
		//lstUECamsi.add(new UE(UE.types.CHOIX, "IATI")); probleme
		lstUECamsi.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "RC"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "STAGECAMSI"));
		lstUECamsi.add(new UE(UE.types.CHOIX, "AIRC2"));
		map.put("camsi", lstUECamsi);	
	}

	private static void initMapDl() {

		ArrayList <UE> lstUEDl = new ArrayList<UE>(); 
		lstUEDl.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUEDl.add(new UE(UE.types.COMMUN, "TER"));
		lstUEDl.add(new UE(UE.types.IMPOSEE, "AL"));
		lstUEDl.add(new UE(UE.types.IMPOSEE, "MCPOOA"));
		lstUEDl.add(new UE(UE.types.IMPOSEE,"JEE"));
		lstUEDl.add(new UE(UE.types.IMPOSEE, "MPI"));
		lstUEDl.add(new UE(UE.types.CHOIX, "DCLL"));
		lstUEDl.add(new UE(UE.types.CHOIX, "IAWS"));
		lstUEDl.add(new UE(UE.types.CHOIX, "MA"));
		lstUEDl.add(new UE(UE.types.CHOIX, "IATI"));
		lstUEDl.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUEDl.add(new UE(UE.types.CHOIX, "RC"));
		lstUEDl.add(new UE(UE.types.CHOIX, "RO"));
		lstUEDl.add(new UE(UE.types.CHOIX, "AHP"));
		lstUEDl.add(new UE(UE.types.CHOIX, "CSR"));
		lstUEDl.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUEDl.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUEDl.add(new UE(UE.types.CHOIX, "IR"));
		lstUEDl.add(new UE(UE.types.CHOIX, "SPR"));
		lstUEDl.add(new UE(UE.types.CHOIX, "STAGEDL"));
		map.put("dl", lstUEDl);
	}


	private static void initMapIhm() {

		ArrayList <UE> lstUEIhm = new ArrayList<UE>(); 
		lstUEIhm.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUEIhm.add(new UE(UE.types.COMMUN, "TER"));
		lstUEIhm.add(new UE(UE.types.IMPOSEE, "AL"));
		lstUEIhm.add(new UE(UE.types.IMPOSEE, "MCPOOA"));
		lstUEIhm.add(new UE(UE.types.IMPOSEE, "IHMUL"));
		lstUEIhm.add(new UE(UE.types.IMPOSEE, "MPI"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "MA"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "IAWS"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "DCLL"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "IATI"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "RC"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "RO"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "AHP"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "CSR"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "IR"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "SPR"));
		lstUEIhm.add(new UE(UE.types.CHOIX, "STAGEIHM"));
		map.put("ihm", lstUEIhm);
	}

	private static void initMapIarf() {

		ArrayList <UE> lstUEIarf = new ArrayList<UE>(); 
		lstUEIarf.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUEIarf.add(new UE(UE.types.COMMUN, "TER"));
		lstUEIarf.add(new UE(UE.types.IMPOSEE, "3DIS"));
		lstUEIarf.add(new UE(UE.types.IMPOSEE, "IAA"));
		lstUEIarf.add(new UE(UE.types.IMPOSEE, "RC"));
		lstUEIarf.add(new UE(UE.types.IMPOSEE, "AIRC2"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "RO"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "IAN"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "MCPOOA"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "IR"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "SPR"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "IATI"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "AHP"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "CSR"));
		lstUEIarf.add(new UE(UE.types.CHOIX, "STAGEIARF"));
		map.put("iarf", lstUEIarf);

	}

	private static void initMapIm() {

		ArrayList <UE> lstUEIm = new ArrayList<UE>(); 
		lstUEIm.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUEIm.add(new UE(UE.types.COMMUN, "TER"));
		lstUEIm.add(new UE(UE.types.IMPOSEE, "3DIS"));
		lstUEIm.add(new UE(UE.types.IMPOSEE, "IG3D"));
		lstUEIm.add(new UE(UE.types.IMPOSEE, "IATI"));
		lstUEIm.add(new UE(UE.types.IMPOSEE, "AS"));
		lstUEIm.add(new UE(UE.types.CHOIX, "IAA"));
		lstUEIm.add(new UE(UE.types.CHOIX, "MCPOOA"));
		lstUEIm.add(new UE(UE.types.CHOIX, "ASDSI"));
		lstUEIm.add(new UE(UE.types.CHOIX, "IAN"));
		lstUEIm.add(new UE(UE.types.CHOIX, "IVO"));
		lstUEIm.add(new UE(UE.types.CHOIX, "OPP"));
		lstUEIm.add(new UE(UE.types.CHOIX, "AHP"));
		lstUEIm.add(new UE(UE.types.CHOIX, "RO"));
		lstUEIm.add(new UE(UE.types.CHOIX, "RC"));
		lstUEIm.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUEIm.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUEIm.add(new UE(UE.types.CHOIX, "IR"));
		lstUEIm.add(new UE(UE.types.CHOIX, "SPR"));
		lstUEIm.add(new UE(UE.types.CHOIX, "MPI"));
		lstUEIm.add(new UE(UE.types.CHOIX, "IHMUL"));
		lstUEIm.add(new UE(UE.types.CHOIX, "CSR"));
		lstUEIm.add(new UE(UE.types.CHOIX, "STAGEIM"));	
		map.put("im", lstUEIm);
	}

	

	abstract void init();
}
