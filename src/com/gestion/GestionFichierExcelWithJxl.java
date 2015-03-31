package com.gestion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.specialite.Specialite;
import com.sqlite.GestionBD;
import com.sqlite.SQLiteJDBC;



public class GestionFichierExcelWithJxl {
	
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
			throws IOException, BiffException {
		ArrayList<UE> lstUE = new ArrayList<UE>();
		ArrayList<Etudiant> lstEtudiant = new ArrayList<Etudiant>();
		int indDebutUE = 8 + 6;
		
		HashMap<String, Integer> speDebut = new HashMap<>();
		

		// Ouverture du fichier Excel
		File file = new File(fileName);
		Workbook wb =  Workbook.getWorkbook(file);
		// On recupère la feuille 1 du fichier Excel
		Sheet sh = wb.getSheet(0);
		int lastRowNum = sh.getRows();
		//Row row = sh.getRow(0);

		// Récupération de l'entete des UEs et parsing de la chaine ( type / nom
		// de l'UE )
		for (int i = indDebutUE; i < sh.getColumns()-1; i++) {
			Cell cell = sh.getCell(i,0);
			lstUE.add(new UE(UE.getType(cell.getContents()), UE
					.parseNomUE(cell.getContents())));
			if (!speDebut.containsKey(parseSpe(cell.getContents())))
				speDebut.put(parseSpe(cell.getContents()), i);
			//speDebut.putIfAbsent(parseSpe(cell.getContents()), i);
		}

		// Récupération des attributs de chaque étudiants à partir du fichier
		// Excel
		for (int i = 1; i < lastRowNum; i++) {
			//row = sh.getRow(i);
			Etudiant e = new Etudiant();
			
			e.setNom(sh.getCell(0 +6,i).getContents());
			e.setPrenom((sh.getCell(1 +6,i).getContents()));
			e.setNumero(sh.getCell(2 +6,i).getContents());
			e.setMailPerso(sh.getCell(3 +6,i).getContents());
			e.setSpecialite(sh.getCell(4 +6,i).getContents().trim());

			if (sh.getCell(5 +6,i).getContents().trim().toLowerCase()
					.equals("y")) {
				e.setRedoublant(true);
			} else {
				e.setRedoublant(false);
			}
			lstEtudiant.add(e);
		}

        try {
            GestionBD.ajouterEtudiant(lstEtudiant);
        } catch (ClassNotFoundException | SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		//System.out.println(lstEtudiant.toString());

		// Initialiosation des liste des liste d'UEs pour chaque etudiants
		for (int i = 1; i < lastRowNum; i++) {
			int debutUE = speDebut.get(lstEtudiant.get(i-1).getSpecialite().toLowerCase());
			int nbUELus = 0;
			int nbUERedouLus = 0;
			
			for (int j = debutUE; j < sh.getColumns(); j++) {
				
				if ( (nbUELus == 10 && !lstEtudiant.get(i-1).isRedoublant()) 
						|| (nbUERedouLus == 10 && lstEtudiant.get(i-1).isRedoublant()) )
					break;
				
				Cell cell = sh.getCell(j,i);
				
				if (cell.getType() == CellType.EMPTY)
					continue;

				if (cell.getType() == CellType.LABEL) {
					switch (cell.getContents().trim().toLowerCase()) {
					case "y":
						nbUELus++;
						if (!lstEtudiant.get(i - 1).isRedoublant()) {
							lstEtudiant.get(i - 1).getListeUE()
									.add(lstUE.get(j - indDebutUE));
						}
						break;
					case "ad":
						nbUERedouLus++;
						(lstUE.get(j - indDebutUE)).setValide(true);
						(lstUE.get(j - indDebutUE)).setType(Specialite
								.getTypeUE(lstEtudiant.get(i - 1)
										.getSpecialite(), lstUE.get(j
										- indDebutUE)));

						lstEtudiant.get(i - 1).getListeUE()
								.add(lstUE.get(j - indDebutUE));
						break;
					case "noad":
						nbUERedouLus++;
						(lstUE.get(j - indDebutUE)).setValide(false);
						(lstUE.get(j - indDebutUE)).setType(Specialite
								.getTypeUE(lstEtudiant.get(i - 1)
										.getSpecialite(), lstUE.get(j
										- indDebutUE)));
						lstEtudiant.get(i - 1).getListeUE()
								.add(lstUE.get(j - indDebutUE));
						break;
					default:
						break;
					}
				}
			}

		}

        try {
            GestionBD.ajouterLien(lstEtudiant);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Affichage de la liste d'étudiants
		/*for (int i = 0; i < lstEtudiant.size(); i++) {
			try {
				GestionBD.ajouterLien(lstEtudiant.get(i));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}

	public static void dataBaseCreation() throws ClassNotFoundException,
			SQLException {
		SQLiteJDBC.getConnexion();
		GestionBD.creationTable();
		GestionBD.initTableUE();
	}
	
	
	public static void toExcel(JTable table, File file){
	    try{
	        TableModel model = table.getModel();
	        FileWriter excel = new FileWriter(file);

	        for(int i = 0; i < model.getColumnCount(); i++){
	            excel.write(model.getColumnName(i) + "\t");
	        }
	        excel.write("\n");

	        for(int i=0; i< model.getRowCount(); i++) {
	            for(int j=0; j < model.getColumnCount(); j++) {
	                excel.write(model.getValueAt(i,j).toString()+"\t");
	            }
	            excel.write("\n");
	        }
	        excel.close();

	    }catch(IOException e){ System.out.println(e); }
	}
	
	
	

	public static void main(String[] args) throws BiffException, InvalidFormatException, WriteException {
		Date d = new Date();

		try {

			//GestionFichierExcelWithPoi.dataBaseCreation();
			//GestionFichierExcelWithPoi.ouvertureFichier("results2.xls");
			
			dataBaseCreation();
			ouvertureFichier("results2.xls");
			/*final ArrayList<Etudiant> listeEtu  = GestionBD.recupererEtudiants();

			TreeMap<String, ArrayList<Etudiant>> listEtuByUE = GestionBD.getEtudiantsPerUE(listeEtu);



            try {
                listEtuByUE = GestionBD.getEtudiantsPerUE(listeEtu);
                File fileToSave = null;
                fileToSave = new File("newouttest.xls");
                WritableWorkbook workbook;
                WorkbookSettings wbSettings = new WorkbookSettings();

                wbSettings.setLocale(new Locale("fr", "FR"));

                workbook = Workbook.createWorkbook(fileToSave, wbSettings);
                String s = fileToSave.toString();
                WriteExcel test = new WriteExcel();
                //test.setOutputFile(s+".xls");
                test.write(workbook, listeEtu, listEtuByUE);
            } catch (WriteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }*/
		
				
		} catch (IOException | ClassNotFoundException | SQLException | OldExcelFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Date d2 = new Date();
		System.out.println((d2.getTime() - d.getTime())/1000 + " secondes.");
	}
}
