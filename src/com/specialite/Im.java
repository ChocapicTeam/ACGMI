package com.specialite;

import com.gestion.UE;

public class Im extends Specialite {
	public Im(String nom) {
		super(nom);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		lstUE.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUE.add(new UE(UE.types.COMMUN, "TER"));
		lstUE.add(new UE(UE.types.IMPOSEE, "3DIS"));
		lstUE.add(new UE(UE.types.IMPOSEE, "IG3D"));
		lstUE.add(new UE(UE.types.IMPOSEE, "IATI"));
		lstUE.add(new UE(UE.types.IMPOSEE, "AS"));
		
		lstUE.add(new UE(UE.types.CHOIX, "IAA"));
		lstUE.add(new UE(UE.types.CHOIX, "MCPOOA"));
		lstUE.add(new UE(UE.types.CHOIX, "ASDSI"));
		lstUE.add(new UE(UE.types.CHOIX, "IAN"));
		lstUE.add(new UE(UE.types.CHOIX, "IVO"));
		lstUE.add(new UE(UE.types.CHOIX, "OPP"));
		lstUE.add(new UE(UE.types.CHOIX, "AHP"));
		lstUE.add(new UE(UE.types.CHOIX, "RO"));
		lstUE.add(new UE(UE.types.CHOIX, "RC"));
		lstUE.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUE.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUE.add(new UE(UE.types.CHOIX, "IR"));
		lstUE.add(new UE(UE.types.CHOIX, "SPR"));
		lstUE.add(new UE(UE.types.CHOIX, "MPI"));
		lstUE.add(new UE(UE.types.CHOIX, "IHMUL"));
		lstUE.add(new UE(UE.types.CHOIX, "CSR"));
		lstUE.add(new UE(UE.types.CHOIX, "STAGE"));


		map.put("im", lstUE);
	}
}
