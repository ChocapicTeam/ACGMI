package com.gestion;


import com.sqlite.GestionBD;
import jxl.Cell;
import jxl.CellView;
import jxl.format.*;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.Label;
import jxl.write.Number;

import java.io.IOException;
import java.lang.Boolean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import static jxl.format.UnderlineStyle.*;


public class WriteExcel {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    //private String inputFile;

    /*public void setOutputFile(String inputFile) {
      this.inputFile = inputFile;
      }
    */
    public void write(WritableWorkbook workbook, ArrayList<Etudiant> listeEtu, TreeMap<String, ArrayList<Etudiant>> listEtuByUE) throws IOException, WriteException, ClassNotFoundException, SQLException {

        workbook.createSheet("Etudiants", 0);
        workbook.createSheet("nb", 1);
        WritableSheet sheetInfo = workbook.getSheet(0);
        createLabelSheetUE(sheetInfo);
        createContentSheetInfo(sheetInfo, listeEtu);

        WritableSheet sheetNumber = workbook.getSheet(1);
        //createLabelSheet3(sheet3, listEtuByUE);
        createContentSheetNumber(sheetNumber);


        int i = 2;
        for (Entry<String, ArrayList<Etudiant>> entry : listEtuByUE.entrySet()) {
            workbook.createSheet(entry.getKey(), i);
            WritableSheet sheetUE = workbook.getSheet(i);
            createLabelSheetUE(sheetUE);
            createContentSheetUE(entry.getKey() ,sheetUE, entry);
            sheetAutoFitColumns(sheetUE, 300);
            i++;
        }


        sheetAutoFitColumns(sheetInfo, 300);
        sheetAutoFitColumns(sheetNumber, 400);

        System.out.println("Exportation finie!");


        workbook.write();
        workbook.close();
    }



    private void sheetAutoFitColumns(WritableSheet sheet, int size) {
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
            cv.setSize(longestStrLen * size + 100); /* Every character is 256 units wide, so scale it. */
            sheet.setColumnView(i, cv);
        }
    }

    private void createLabelSheetUE

            (WritableSheet sheet)
            throws WriteException {
        addCaptionLabel(sheet, 0, 0, "Nom");
        addCaptionLabel(sheet, 1, 0, "Prenom");
        addCaptionLabel(sheet, 2, 0, "Mail");
        addCaptionLabel(sheet, 3, 0, "Specialite");
        addCaptionLabel(sheet, 4, 0, "Redoublant");
        addCaptionLabel(sheet, 5, 0, "Num Etudiant");
        addCaptionLabel(sheet, 6, 0, "UE Projet");
        addCaptionLabel(sheet, 7, 0, "UE ter");
        addCaptionLabel(sheet, 8, 0, "UE Imposée 1");
        addCaptionLabel(sheet, 9, 0, "UE Imposée 2");
        addCaptionLabel(sheet, 10, 0, "UE Imposée 3");
        addCaptionLabel(sheet, 11, 0, "UE Imposée 4");
        addCaptionLabel(sheet, 12, 0, "UE à choix 1");
        addCaptionLabel(sheet, 13, 0, "UE à choix 2");
        addCaptionLabel(sheet, 14, 0, "UE à choix 3");
        addCaptionLabel(sheet, 15, 0, "UE à choix 4");
    }

    private void createContentSheetInfo(WritableSheet sheet, ArrayList<Etudiant> listeEtu) throws WriteException,
            ClassNotFoundException, SQLException {

        ArrayList<UE> listeChoix = new ArrayList<>();
        ArrayList<UE> listeImpo = new ArrayList<>();
        ArrayList<UE> listeCommun = new ArrayList<>();
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

            addCaptionLabel(sheet, 0, i, etu.getNom());
            addCaptionLabel(sheet, 1, i, etu.getPrenom());
            addCaptionLabel(sheet, 2, i, etu.getMailPerso());
            addCaptionLabel(sheet, 3, i, etu.getSpecialite());
            addCaptionLabel(sheet, 4, i, etu.isRedoublant() ? "Oui" : "Non");
            addCaptionNumber(sheet, 5, i, etu.getNumero(), false);

            addCaptionLabel(sheet, 6, i, listeCommun.get(0).isValide() ? listeCommun.get(0).getNom() + " admis" : listeCommun.get(0).getNom());
            addCaptionLabel(sheet, 7, i, listeCommun.get(1).isValide() ? listeCommun.get(1).getNom() + " admis" : listeCommun.get(1).getNom());
            addCaptionLabel(sheet, 8, i, listeImpo.get(0).isValide() ? listeImpo.get(0).getNom() + " admis" : listeImpo.get(0).getNom());
            addCaptionLabel(sheet, 9, i, listeImpo.get(1).isValide() ? listeImpo.get(1).getNom() + " admis" : listeImpo.get(1).getNom());
            addCaptionLabel(sheet, 10, i, listeImpo.get(2).isValide() ? listeImpo.get(2).getNom() + " admis" : listeImpo.get(2).getNom());
            addCaptionLabel(sheet, 11, i, listeImpo.get(3).isValide()? listeImpo.get(3).getNom() + " admis" : listeImpo.get(3).getNom());
            addCaptionLabel(sheet, 12, i, listeChoix.get(0).isValide() ? listeChoix.get(0).getNom() + " admis" : listeChoix.get(0).getNom());
            addCaptionLabel(sheet, 13, i, listeChoix.get(1).isValide() ? listeChoix.get(1).getNom() + " admis" : listeChoix.get(1).getNom());
            addCaptionLabel(sheet, 14, i, listeChoix.get(2).isValide() ? listeChoix.get(2).getNom() + " admis" : listeChoix.get(2).getNom());
            addCaptionLabel(sheet, 15, i, listeChoix.get(3).isValide() ? listeChoix.get(3).getNom() + " admis" : listeChoix.get(3).getNom());

            listeImpo.clear();
            listeChoix.clear();
            listeCommun.clear();
            i++;
        }

    }

    private void createContentSheetUE(String ueToFill, WritableSheet sheet,
                                      Entry<String, ArrayList<Etudiant>> entry) throws WriteException, SQLException, ClassNotFoundException {

        ArrayList<UE> listeChoix = new ArrayList<>();
        ArrayList<UE> listeImpo = new ArrayList<>();
        ArrayList<UE> listeCommun = new ArrayList<>();
        ArrayList<UE> listeUE;


        int i = 1;
        for (Etudiant etu : entry.getValue()) {
            Boolean isRed = false;
            String numero = etu.getNumero();
            listeUE = GestionBD.getUeEtudiant(numero);
            for (UE ue : listeUE) {
                if (ue.getType().equals(UE.types.IMPOSEE))
                    listeImpo.add(ue);
                else if (ue.getType().equals(UE.types.COMMUN))
                    listeCommun.add(ue);
                else
                    listeChoix.add(ue);
                if (ue.getNom().equals(ueToFill) && ue.isValide())
                    isRed = true;
            }

            addCaptionUE(sheet, 0, i, etu.getNom(), isRed);
            addCaptionUE(sheet, 1, i, etu.getPrenom(), isRed);
            addCaptionUE(sheet, 2, i, etu.getMailPerso(), isRed);
            addCaptionUE(sheet, 3, i, etu.getSpecialite(), isRed);
            addCaptionUE(sheet, 4, i, etu.isRedoublant() ? "Oui" : "Non", isRed);
            addCaptionNumber(sheet, 5, i, etu.getNumero(), isRed);

            addCaptionUE(sheet, 6, i, listeCommun.get(0).isValide() ? listeCommun.get(0).getNom() + " admis" : listeCommun.get(0).getNom(), isRed);
            addCaptionUE(sheet, 7, i, listeCommun.get(1).isValide() ? listeCommun.get(0).getNom() + " admis" : listeCommun.get(1).getNom(), isRed);
            addCaptionUE(sheet, 8, i, listeImpo.get(0).isValide() ? listeImpo.get(0).getNom() + " admis" : listeImpo.get(0).getNom(), isRed);
            addCaptionUE(sheet, 9, i, listeImpo.get(1).isValide() ? listeImpo.get(1).getNom() + " admis" : listeImpo.get(1).getNom(), isRed);
            addCaptionUE(sheet, 10, i, listeImpo.get(2).isValide() ? listeImpo.get(2).getNom() + " admis" : listeImpo.get(2).getNom(), isRed);
            addCaptionUE(sheet, 11, i, listeImpo.get(3).isValide() ? listeImpo.get(3).getNom() + " admis" : listeImpo.get(3).getNom(), isRed);
            addCaptionUE(sheet, 12, i, listeChoix.get(0).isValide() ? listeChoix.get(0).getNom() + " admis" : listeChoix.get(0).getNom(), isRed);
            addCaptionUE(sheet, 13, i, listeChoix.get(1).isValide() ? listeChoix.get(1).getNom() + " admis" : listeChoix.get(1).getNom(), isRed);
            addCaptionUE(sheet, 14, i, listeChoix.get(2).isValide() ? listeChoix.get(2).getNom() + " admis" : listeChoix.get(2).getNom(), isRed);
            addCaptionUE(sheet, 15, i, listeChoix.get(3).isValide() ? listeChoix.get(3).getNom() + " admis" : listeChoix.get(3).getNom(), isRed);

            listeImpo.clear();
            listeChoix.clear();
            listeCommun.clear();
            i++;
        }
    }

    private void createContentSheetNumber(WritableSheet sheet/*, TreeMap<String, Integer> nbEtudiantAvecUeNonValidePerUE*/) throws WriteException, SQLException, ClassNotFoundException {

        HashMap<String, Integer> listeNbEtuNoValideByUE = GestionBD.recupererNombreEtuPerUe();
        int i = 0;
        for (Entry<String,Integer> entry : listeNbEtuNoValideByUE.entrySet()) {
            addCaptionLabel(sheet, 0, i, entry.getKey());
            addCaptionNumber(sheet, 1, i, entry.getValue().toString(), false);
            i++;
        }

    }

    private void addCaptionLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD, false, NO_UNDERLINE, Colour.BLACK);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);
        times.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
        CellView cv = new CellView();
        cv.setSize(100);
        cv.setFormat(times);
        Label label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void addCaptionNumber(WritableSheet sheet, int column, int row, String s, Boolean isValide)
            throws WriteException{
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD, false, NO_UNDERLINE, Colour.BLACK);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);
        times.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
        if (isValide)
            times.setBackground(Colour.RED);
        CellView cv = new CellView();
        cv.setSize(100);
        cv.setFormat(times);
        Number label;
        double value = Double.parseDouble(s);
        label = new Number(column, row, value, times);
        sheet.addCell(label);
    }

    private void addCaptionUE(WritableSheet sheet, int column, int row, String s, Boolean isValide)
            throws WriteException {
        WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD, false, NO_UNDERLINE, Colour.BLACK);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);
        times.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, Colour.BLACK);
        if (isValide == true)
            times.setBackground(Colour.RED);
        CellView cv = new CellView();
        cv.setSize(100);
        cv.setFormat(times);
        Label label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

} 