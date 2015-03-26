package com.gestion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jxl.read.biff.BiffException;

public class Main {

	public static void main(String[] args) throws BiffException, InvalidFormatException, ClassNotFoundException, SQLException, IOException {
		Date d = new Date();
		try {
			GestionFichierExcelWithPoi.dataBaseCreation();
			GestionFichierExcelWithPoi.ouvertureFichier("results.xlsx");
		} catch (IOException | ClassNotFoundException
				| SQLException e) {
			e.printStackTrace();
		} catch (OldExcelFormatException e) {
			GestionFichierExcelWithJxl.dataBaseCreation();
			GestionFichierExcelWithJxl.ouvertureFichier("results.xlsx");
		}
		Date d2 = new Date();
		System.out.println((d2.getTime() - d.getTime())/1000 + " secondes.");
	}
}
