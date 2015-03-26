package com.specialite;

import com.gestion.UE;

public class Iarf extends Specialite {

	public Iarf(String nom) {
		super(nom);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		lstUE.add(new UE(UE.types.COMMUN, "PROJET"));
		lstUE.add(new UE(UE.types.COMMUN, "TER"));
		lstUE.add(new UE(UE.types.IMPOSEE, "3DIS"));
		lstUE.add(new UE(UE.types.IMPOSEE, "IAA"));
		lstUE.add(new UE(UE.types.IMPOSEE, "RC"));
		lstUE.add(new UE(UE.types.IMPOSEE, "AIRC2"));
		
		lstUE.add(new UE(UE.types.CHOIX, "RO"));
		lstUE.add(new UE(UE.types.CHOIX, "IAN"));
		lstUE.add(new UE(UE.types.CHOIX, "MCPOOA"));
		lstUE.add(new UE(UE.types.CHOIX, "BDOO"));
		lstUE.add(new UE(UE.types.CHOIX, "BDPR"));
		lstUE.add(new UE(UE.types.CHOIX, "IR"));
		lstUE.add(new UE(UE.types.CHOIX, "SPR"));
		lstUE.add(new UE(UE.types.CHOIX, "ATI"));
		lstUE.add(new UE(UE.types.CHOIX, "IG3D"));
		lstUE.add(new UE(UE.types.CHOIX, "AHP"));
		lstUE.add(new UE(UE.types.CHOIX, "CSR"));
		lstUE.add(new UE(UE.types.CHOIX, "STAGE"));


		
		
		map.put("iarf", lstUE);
	}

}
