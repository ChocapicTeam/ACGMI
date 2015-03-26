package com.specialite;

import com.gestion.UE;

public class Dl extends Specialite {
	public Dl(String nom) {
		super(nom);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		lstUE.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUE.add(new UE(UE.types.COMMUN, "TER"));
		lstUE.add(new UE(UE.types.IMPOSEE, "AL"));
		lstUE.add(new UE(UE.types.IMPOSEE, "MCPOOA"));
		lstUE.add(new UE(UE.types.IMPOSEE,"JEE"));
		lstUE.add(new UE(UE.types.IMPOSEE, "MPI"));
		
		lstUE.add(new UE(UE.types.CHOIX, "DCLL"));
		lstUE.add(new UE(UE.types.CHOIX, "IAWS"));
		lstUE.add(new UE(UE.types.CHOIX, "MA"));
		lstUE.add(new UE(UE.types.CHOIX, "ATI"));
		lstUE.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUE.add(new UE(UE.types.CHOIX, "RC"));
		lstUE.add(new UE(UE.types.CHOIX, "RO"));
		lstUE.add(new UE(UE.types.CHOIX, "AHP"));
		lstUE.add(new UE(UE.types.CHOIX, "CSR"));
		lstUE.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUE.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUE.add(new UE(UE.types.CHOIX, "IR"));
		lstUE.add(new UE(UE.types.CHOIX, "SPR"));
		lstUE.add(new UE(UE.types.CHOIX, "STAGEDL"));

		
		map.put("dl", lstUE);
	}
}
