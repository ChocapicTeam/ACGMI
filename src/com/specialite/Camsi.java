package com.specialite;

import com.gestion.UE;

public class Camsi extends Specialite {

	public Camsi(String nom) {
		super(nom);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		lstUE.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUE.add(new UE(UE.types.COMMUN, "TER"));
		lstUE.add(new UE(UE.types.IMPOSEE, "AHP"));
		lstUE.add(new UE(UE.types.IMPOSEE, "AS"));
		lstUE.add(new UE(UE.types.IMPOSEE, "CSR"));
		lstUE.add(new UE(UE.types.IMPOSEE, "IATI"));
		
		lstUE.add(new UE(UE.types.CHOIX, "RO"));
		lstUE.add(new UE(UE.types.CHOIX, "MCPOOA"));
		lstUE.add(new UE(UE.types.CHOIX, "3DIS"));
		lstUE.add(new UE(UE.types.CHOIX, "NSTR"));
		lstUE.add(new UE(UE.types.CHOIX, "TAS"));
		lstUE.add(new UE(UE.types.CHOIX, "OPP"));
		lstUE.add(new UE(UE.types.CHOIX, "MPI"));
		lstUE.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUE.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUE.add(new UE(UE.types.CHOIX, "IR"));
		lstUE.add(new UE(UE.types.CHOIX, "SPR"));
		lstUE.add(new UE(UE.types.CHOIX, "ATI"));
		lstUE.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUE.add(new UE(UE.types.CHOIX, "RC"));
		lstUE.add(new UE(UE.types.CHOIX, "STAGE"));
		lstUE.add(new UE(UE.types.CHOIX, "AIRC2"));


		map.put("camsi", lstUE);
	}
	
	

}
