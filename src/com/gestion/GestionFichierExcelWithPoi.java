package com.gestion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.specialite.Specialite;
import com.sqlite.GestionBD;
import com.sqlite.SQLiteJDBC;

public class GestionFichierExcelWithPoi {

	private static String parseSpe(String chaine) {
		if (chaine.substring(0, 3).compareTo("dl") == 1)
			return "dl";
		else if (chaine.substring(0, 4).compareTo("ihm") == 1)
			return "ihm";
		else if (chaine.substring(0, 6).compareTo("camsi") == 1)
			return "camsi";
		else if (chaine.substring(0, 3).compareTo("im") == 1)
			return "im";
		else if (chaine.substring(0, 5).compareTo("iarf") == 1)
			return "iarf";
		return chaine;
	}

	public static void ouvertureFichier(String fileName)
			throws InvalidFormatException, IOException, ClassNotFoundException, SQLException {
		ArrayList<UE> lstUE = new ArrayList<UE>();
		ArrayList<Etudiant> lstEtudiant = new ArrayList<Etudiant>();
		int indDebutUE = 8;

		HashMap<String, Integer> speDebut = new HashMap<>();

		// Ouverture du fichier Excel
		File file = new File(fileName);
		Workbook wb = WorkbookFactory.create(file);
		// On recupère la feuille 1 du fichier Excel
		Sheet sh = wb.getSheet(wb.getSheetName(0));
		int lastRowNum = sh.getLastRowNum() + 1;

		Row row = sh.getRow(0);

		// Récupération de l'entete des UEs et parsing de la chaine ( type / nom
		// de l'UE )
		for (int i = indDebutUE; i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			lstUE.add(new UE(UE.getType(cell.getStringCellValue()), UE
					.parseNomUE(cell.getStringCellValue())));
			if (speDebut.containsKey(parseSpe(cell.getStringCellValue())))
				speDebut.put(parseSpe(cell.getStringCellValue()), i);
			//speDebut.putIfAbsent(parseSpe(cell.getStringCellValue()), i);
		}

		// Récupération des attributs de chaque étudiants à partir du fichier
		// Excel
		for (int i = 1; i < lastRowNum; i++) {
			row = sh.getRow(i);
			Etudiant e = new Etudiant();
			e.setNom(row.getCell(0).getStringCellValue());
			e.setPrenom(row.getCell(1).getStringCellValue());
			e.setNumero(String.valueOf((int) row.getCell(2)
					.getNumericCellValue()));
			e.setMailPerso(row.getCell(3).getStringCellValue());
			e.setSpecialite(row.getCell(4).getStringCellValue().trim());

			if (row.getCell(5).getStringCellValue().trim().toLowerCase()
					.equals("y")) {
				e.setRedoublant(true);
			} else {
				e.setRedoublant(false);
			}

			try {
				GestionBD.ajouterEtudiant(e);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			lstEtudiant.add(e);
		}
		// Initialiosation des liste des liste d'UEs pour chaque etudiants
		for (int i = 1; i < lastRowNum; i++) {
			row = sh.getRow(i);
			Etudiant pers = lstEtudiant.get(i - 1);
			for (int j = indDebutUE; j < row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				if (cell == null)
					continue;

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					UE ue = lstUE.get(j - indDebutUE);
					switch (cell.getStringCellValue().trim().toLowerCase()) {
					case "y":
						if (!pers.isRedoublant()) {
							pers.getListeUE().add(ue);
						}
						break;
					case "ad":
						ue.setValide(true);
						ue.setType(Specialite.getTypeUE(pers.getSpecialite(),
								ue));
						pers.getListeUE().add(ue);
						break;
					case "noad":
						ue.setValide(false);
						ue.setType(Specialite.getTypeUE(pers.getSpecialite(),
								ue));
						pers.getListeUE().add(ue);
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
			GestionBD.ajouterLien(pers);
		}
		// Affichage de la liste d'étudiants
		//		for (int i = 0; i < lstEtudiant.size(); i++) {
		//			 //System.out.println(lstEtudiant.get(i).toString());
		//			try {
		//			} catch (ClassNotFoundException | SQLException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
	}

	public static void dataBaseCreation() throws ClassNotFoundException,
	SQLException {
		SQLiteJDBC.getConnexion();
		GestionBD.creationTable();
		GestionBD.initTableUE();
	}



/*	public static void createDoc(List<Etudiant> lstEtudiant) throws ClassNotFoundException, SQLException, IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("feuille 1");
		HSSFSheet sheet2 = workbook.createSheet("feuille 2");

		int rownum = 0;
		for (Etudiant etu : lstEtudiant) {
			Row row = sheet.createRow(rownum++);
			int cellnum = 0;
			Cell cell = row.createCell(cellnum++);
			cell.setCellValue(etu.getSpecialite());
			cell = row.createCell(cellnum++);
			cell.setCellValue(etu.getNom());
			cell = row.createCell(cellnum++);
			cell.setCellValue(etu.getPrenom());
			cell = row.createCell(cellnum++);
			cell.setCellValue(etu.getNumero());
			cell = row.createCell(cellnum++);
			cell.setCellValue(etu.getMailPerso());
			cell = row.createCell(cellnum++);
		}

		TreeMap<String, ArrayList<Etudiant>> mapUeAndEtu = new TreeMap<>();
		//mapUeAndEtu = GestionBD.getEtudiantsPerUE();
		rownum = 0;
		for(Entry<String, ArrayList<Etudiant>> entry : mapUeAndEtu.entrySet()) {
			String ue = entry.getKey();
			ArrayList<Etudiant> lstEtu = entry.getValue();

			int cellnum = 0;
			Row row = sheet2.createRow(rownum++);

			Cell cell = row.createCell(cellnum++);
			cell.setCellValue(ue + " : ");

			for (Etudiant etu : lstEtu) {
				row = sheet2.createRow(rownum++);
				cell = row.createCell(cellnum++);
				cell.setCellValue(etu.getNom());
				cell = row.createCell(cellnum++);
				cell.setCellValue(etu.getPrenom());
				cell = row.createCell(cellnum++);
				cell.setCellValue(etu.getNumero());
				cell = row.createCell(cellnum++);
				cell.setCellValue(etu.getMailPerso());
				cell = row.createCell(cellnum++);
			}
		}


		try {
			FileOutputStream out = 
					new FileOutputStream(new File("new.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		workbook.close();
	}
/*


/*
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		List<Etudiant> etus = new ArrayList<>();
		try {
			dataBaseCreation();
			ouvertureFichier("results.xlsx");
		} catch (InvalidFormatException | IOException | ClassNotFoundException
				| SQLException e) {
			e.printStackTrace();
		}

		try {
			etus = GestionBD.recupererEtudiants();
		}
		catch ( ClassNotFoundException
				| SQLException e) {
			e.printStackTrace();
		}

		createDoc(etus);

	}*/

}
