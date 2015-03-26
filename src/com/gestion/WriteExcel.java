package com.gestion;


import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.sqlite.GestionBD;


public class WriteExcel {

  private WritableCellFormat timesBoldUnderline;
  private WritableCellFormat times;
  private String inputFile;
  
public void setOutputFile(String inputFile) {
  this.inputFile = inputFile;
  }

  public void write(WritableWorkbook workbook, ArrayList<Etudiant> listeEtu, TreeMap<String, ArrayList<Etudiant>> listEtuByUE) throws IOException, WriteException, ClassNotFoundException, SQLException {
    
    workbook.createSheet("Etudiants", 0);
    WritableSheet sheet1 = workbook.getSheet(0);
    createLabelSheet1(sheet1);
    createContentSheet1(sheet1, listeEtu);
    
    int i = 1;
    for (Entry<String, ArrayList<Etudiant>> entry : listEtuByUE.entrySet()) {
    	workbook.createSheet(entry.getKey(), i);
    	WritableSheet sheetUE = workbook.getSheet(i);
        createLabelSheet2(sheetUE);
        createContentSheet2(sheetUE, entry);
        sheetAutoFitColumns(sheetUE);
        i++;
    }
    sheetAutoFitColumns(sheet1);

    System.out.println("Exportation finie!");
    

    workbook.write();
    workbook.close();
  }
  


private void sheetAutoFitColumns(WritableSheet sheet) {
	    for (int i = 0; i < sheet.getColumns(); i++) {
	        Cell[] cells = sheet.getColumn(i);
	        int longestStrLen = -1;

	        if (cells.length == 0)
	            continue;

	        /* Find the widest cell in the column. */
	        for (int j = 0; j < cells.length; j++) {
	            if ( cells[j].getContents().length() > longestStrLen ) {
	                String str = cells[j].getContents();
	                if (str == null || str.isEmpty())
	                    continue;
	                longestStrLen = str.trim().length();
	            }
	        }

	        /* If not found, skip the column. */
	        if (longestStrLen == -1) 
	            continue;

	        /* If wider than the max width, crop width */
	        if (longestStrLen > 280)
	            longestStrLen = 280;

	        CellView cv = sheet.getColumnView(i);
	        cv.setSize(longestStrLen * 300 + 100); /* Every character is 256 units wide, so scale it. */
	        sheet.setColumnView(i, cv);
	    }
	}

  private void createLabelSheet1(WritableSheet sheet)
      throws WriteException {
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    timesBoldUnderline = new WritableCellFormat();
    // Lets automatically wrap the cells
    timesBoldUnderline.setWrap(true);

    CellView cv = new CellView();
    cv.setSize(100);
    cv.setFormat(times);
    cv.setFormat(timesBoldUnderline);

    // Write a few headers
    addCaption(sheet, 0, 0, "Nom");
    addCaption(sheet, 1, 0, "Prenom");
    addCaption(sheet, 2, 0, "Mail");
    addCaption(sheet, 3, 0, "Specialite");
    addCaption(sheet, 4, 0, "Redoublant");
    addCaption(sheet, 5, 0, "Num Etudiant");
    addCaption(sheet, 6, 0, "UE Projet");
    addCaption(sheet, 7, 0, "UE ter");
    addCaption(sheet, 8, 0, "UE Imposée 1");
    addCaption(sheet, 9, 0, "UE Imposée 2");
    addCaption(sheet, 10, 0, "UE Imposée 3");
    addCaption(sheet, 11, 0, "UE Imposée 4");
    addCaption(sheet, 12, 0, "UE à choix 1");
    addCaption(sheet, 13, 0, "UE à choix 2");
    addCaption(sheet, 14, 0, "UE à choix 3");
    addCaption(sheet, 15, 0, "UE à choix 4");
  }

  private void createContentSheet1(WritableSheet sheet, ArrayList<Etudiant> listeEtu) throws WriteException,
      RowsExceededException, ClassNotFoundException, SQLException {
    
	ArrayList<UE> listeChoix = new ArrayList<UE>();
	ArrayList<UE> listeImpo = new ArrayList<UE>();
	ArrayList<UE> listeCommun = new ArrayList<UE>();
	ArrayList<UE> listeUE ; 
	  
	int i = 1;
    for (Etudiant etu : listeEtu) {
    	String numero = etu.getNumero();
		listeUE = GestionBD.getUeEtudiant(numero);
    	for(UE ue : listeUE) {
			if (ue.getType().equals(UE.types.IMPOSEE))
				listeImpo.add(ue);
			else if(ue.getType().equals(UE.types.COMMUN))
				listeCommun.add(ue);
			else 
				listeChoix.add(ue);
    	}
    	
    	addLabel(sheet, 0, i, etu.getNom());
    	addLabel(sheet, 1, i, etu.getPrenom());
    	addLabel(sheet, 2, i, etu.getMailPerso());
    	addLabel(sheet, 3, i, etu.getSpecialite());
    	addLabel(sheet, 4, i, etu.isRedoublant()? "Oui" : "Non");
    	addLabel(sheet, 5, i, etu.getNumero());
    	
    	addLabel(sheet,  6, i, listeCommun.get(0).isValide()? listeCommun.get(0).getNom() + " admis" : listeCommun.get(0).getNom());
    	addLabel(sheet,  7, i, listeCommun.get(1).isValide()? listeCommun.get(1).getNom() + " admis" : listeCommun.get(1).getNom());
    	addLabel(sheet,  8, i, listeImpo.get(0).isValide()? listeImpo.get(0).getNom() + " admis" : listeImpo.get(0).getNom());
    	addLabel(sheet,  9, i, listeImpo.get(1).isValide()? listeImpo.get(1).getNom() + " admis" : listeImpo.get(1).getNom());
    	addLabel(sheet, 10, i, listeImpo.get(2).isValide()? listeImpo.get(2).getNom() + " admis" : listeImpo.get(2).getNom());
    	addLabel(sheet, 11, i, listeImpo.get(3).isValide()? listeImpo.get(3).getNom() + " admis" : listeImpo.get(3).getNom());
    	addLabel(sheet, 12, i, listeChoix.get(0).isValide()? listeChoix.get(0).getNom() + " admis" : listeChoix.get(0).getNom());
    	addLabel(sheet, 13, i, listeChoix.get(1).isValide()? listeChoix.get(1).getNom() + " admis" : listeChoix.get(1).getNom());
    	addLabel(sheet, 14, i, listeChoix.get(2).isValide()? listeChoix.get(2).getNom() + " admis" : listeChoix.get(2).getNom());
    	addLabel(sheet, 15, i, listeChoix.get(3).isValide()? listeChoix.get(3).getNom() + " admis" : listeChoix.get(3).getNom());
    	
    	listeImpo.clear();
		listeChoix.clear();
		listeCommun.clear();
		i++;
    }
    
  }

  private void addCaption(WritableSheet sheet, int column, int row, String s)
      throws RowsExceededException, WriteException {
    Label label;
    label = new Label(column, row, s, timesBoldUnderline);
    sheet.addCell(label);
  }

  private void addLabel(WritableSheet sheet, int column, int row, String s)
      throws WriteException, RowsExceededException {
    Label label;
    label = new Label(column, row, s, times);
    sheet.addCell(label);
    CellView cv = sheet.getColumnView(column);
    cv.setSize(s.length() * 256 + 100); /* Every character is 256 units wide, so scale it. */
    sheet.setColumnView(column, cv);
  }
  
  
  
  
  private void createContentSheet2(WritableSheet sheet,
		  Entry<String, ArrayList<Etudiant>> entry) throws RowsExceededException, WriteException {
	// TODO Auto-generated method stub

	//String ue = entry.getKey();
	//addLabel(sheet, 0, i, entry.getKey());
	int i = 1;
	for (Etudiant etu : entry.getValue()) {		  
		addLabel(sheet, 1, i, etu.getNom());
		addLabel(sheet, 2, i, etu.getPrenom());
		addLabel(sheet, 3, i, etu.getNumero());
		addLabel(sheet, 4, i, etu.getSpecialite());
		i++;
	}
	
}

  
  
  
private void createLabelSheet2(WritableSheet sheet) throws RowsExceededException, WriteException {
	// Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    timesBoldUnderline = new WritableCellFormat();
    // Lets automatically wrap the cells
    timesBoldUnderline.setWrap(true);

    CellView cv = new CellView();
    cv.setSize(100);
    cv.setFormat(times);
    cv.setFormat(timesBoldUnderline);

	
    addCaption(sheet, 0, 0, "UE");
	addCaption(sheet, 1, 0, "Nom");
    addCaption(sheet, 2, 0, "Prenom");
    addCaption(sheet, 4, 0, "Specialite");
    addCaption(sheet, 3, 0, "Num Etudiant");
	
}
  
/*
  public static void main(String[] args) throws WriteException, IOException, InvalidFormatException, ClassNotFoundException, SQLException {
	  GestionFichierExcelWithPoi.dataBaseCreation();
		GestionFichierExcelWithPoi.ouvertureFichier("results.xlsx");
		ArrayList<Etudiant> listeEtu  = GestionBD.recupererEtudiants();
	  
	  
    WriteExcel test = new WriteExcel();
    test.setOutputFile("newfichierout.xls");
    test.write(listeEtu);
    
    System.out
        .println("Please check the result file ");
  }*/
} 