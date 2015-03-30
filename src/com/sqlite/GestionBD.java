package com.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import com.gestion.Etudiant;
import com.gestion.UE;

public class GestionBD {

	public static void creationTable() throws SQLException,
			ClassNotFoundException {
		Connection connection = (Connection) SQLiteJDBC.getConnexion();
		Statement statement = (Statement) connection.createStatement();
		// crÈation des 2 tables,si elles existaient deja, une SQLException
		// sera alors lancee
		// par jdbc. On attrape l'erreur dans le bloc catch.
		try {

			statement.executeUpdate("DROP TABLE IF EXISTS etudiant ");
			statement.executeUpdate("DROP TABLE IF EXISTS lien ");
			statement.executeUpdate("DROP TABLE IF EXISTS ue ");

			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS etudiant "
							+ "(numeroEtudiant INTEGER PRIMARY KEY, nom VARCHAR(25) , prenom VARCHAR(25) ,"
							+ "mail VARCHAR(40) ,  specialite VARCHAR(10), redoublant INTEGER not NULL)");

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS ue "
					+ "(id INTEGER PRIMARY KEY,nomue VARCHAR(10))");

			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS lien "
							+ "(numetu INTEGER, idue VARCHAR(10), type VARCHAR(20), valide INTEGER, primary key(numetu,idue))");
		}

		catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			SQLiteJDBC.close(statement);
			SQLiteJDBC.close(connection);
		}
	}

	public static void ajouterEtudiant(Etudiant etudiant) throws SQLException,
			ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statementEtudiant = null;
		try {
			// Ajout d'un etudiant dans la base de donnée
			connection = (Connection) SQLiteJDBC.getConnexion();
			statementEtudiant = (PreparedStatement) connection
					.prepareStatement("INSERT INTO etudiant(numeroEtudiant,nom,prenom,mail,specialite,redoublant) VALUES (?,?,?,?,?,?)");
			statementEtudiant.setInt(1, Integer.parseInt(etudiant.getNumero()));
			statementEtudiant.setString(2, etudiant.getNom());
			statementEtudiant.setString(3, etudiant.getPrenom());
			statementEtudiant.setString(4, etudiant.getMailPerso());
			statementEtudiant.setString(5, etudiant.getSpecialite());
			if (etudiant.isRedoublant()) {
				statementEtudiant.setInt(6, 1);
			} else {
				statementEtudiant.setInt(6, 0);
			}
			statementEtudiant.executeUpdate();
		} finally {
			SQLiteJDBC.close(statementEtudiant);
			SQLiteJDBC.close(connection);
		}
	}

	public static void ajouterLien(Etudiant etudiant) throws SQLException,
			ClassNotFoundException {
		Connection connection = null;
		PreparedStatement statementLien = null;
		try {
			// Ajout dans la table lien (etudiant / nom Ue )
			for (int i = 0; i < etudiant.getListeUE().size(); i++) {
				connection = (Connection) SQLiteJDBC.getConnexion();
				statementLien = (PreparedStatement) connection
						.prepareStatement("INSERT INTO lien(numetu,idue, type, valide) VALUES (?,?,?,?)");
				statementLien.setInt(1, Integer.parseInt(etudiant.getNumero()));
				statementLien.setString(2, etudiant.getListeUE().get(i)
						.getNom());
				statementLien.setString(3, etudiant.getListeUE().get(i)
						.getType().toString());
				statementLien.setBoolean(4, etudiant.getListeUE().get(i)
						.isValide());
				statementLien.executeUpdate();
			}

		} finally {
			SQLiteJDBC.close(statementLien);
			SQLiteJDBC.close(connection);
		}

	}

	// retourne la liste de tout les étudiants
	public static ArrayList<Etudiant> recupererEtudiants() throws SQLException,
			ClassNotFoundException {
		Connection connection = null;
		Statement statement = null;
		ArrayList<Etudiant> listeEtudiant = new ArrayList<Etudiant>();

		try {
			connection = (Connection) SQLiteJDBC.getConnexion();
			statement = connection.createStatement();
			// execute select SQL stetement
			ResultSet rs = statement
					.executeQuery("SELECT * from Etudiant order by specialite");

			while (rs.next()) {
				Etudiant e = new Etudiant();
				e.setNom(rs.getString("nom"));
				e.setPrenom(rs.getString("prenom"));
				e.setMailPerso(rs.getString("mail"));
				e.setNumero(String.valueOf(rs.getInt("numeroEtudiant")));

				e.setSpecialite(rs.getString("specialite"));
				if (rs.getInt("redoublant") == 0) {
					e.setRedoublant(false);
				} else {
					e.setRedoublant(true);
				}
				listeEtudiant.add(e);
			}

		}

		finally {
			SQLiteJDBC.close(statement);
			SQLiteJDBC.close(connection);
		}

		return listeEtudiant;

	}


    public static ArrayList<String> recupererUe() throws SQLException,
            ClassNotFoundException {
        Connection connection = null;
        Statement statement = null;
        ArrayList<String> listeUe = new ArrayList<>();

        try {
            connection = (Connection) SQLiteJDBC.getConnexion();
            statement = connection.createStatement();
            // execute select SQL stetement
            ResultSet rs = statement
                    .executeQuery("SELECT nomue from UE");
            while (rs.next()) {
                listeUe.add(rs.getString("nomue"));
            }
        }
        finally {
            SQLiteJDBC.close(statement);
            SQLiteJDBC.close(connection);
        }
        return listeUe;

    }



	public static void initTableUE() throws SQLException,
			ClassNotFoundException {

		String tabUe[] = { "PROJET", "TER", "STAGE", "3DIS", "AHP", "AIRC2",
				"AL", "AS", "ASDSI", "CSR", "DCLL", "IAA", "IAN", "IATI",
				"IAWS", "IG3D", "IHMUL", "IVO", "JEE", "MA", "MCPOOA", "MPI",
				"NSTR", "RC", "RO", "BDOO", "BDPR", "IR", "SPR" };

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			// Ajout de l'id Etudiant et du nom de l'Ue dans la table UE
			connection = (Connection) SQLiteJDBC.getConnexion();

			for (int i = 0; i < tabUe.length; i++) {
				statement = (PreparedStatement) connection
						.prepareStatement("INSERT INTO ue(id,nomue) VALUES (?,?)");
				statement.setInt(1, i + 1);
				statement.setString(2, tabUe[i]);
				statement.executeUpdate();
			}

		}

		finally {
			SQLiteJDBC.close(statement);
			SQLiteJDBC.close(connection);
		}

	}

	// Prend en paramètre le numero etudiant et retourne la liste d'Ue
	// associée
	public static ArrayList<UE> getUeEtudiant(String numEtu)
			throws SQLException, ClassNotFoundException {
		Connection connection = null;
		Statement statement = null;
		ArrayList<UE> listeUE = new ArrayList<UE>();

		try {
			connection = (Connection) SQLiteJDBC.getConnexion();
			statement = connection.createStatement();
			// execute select SQL stetement
			ResultSet rs = statement
					.executeQuery("SELECT idue,type,valide from lien where numetu = "
							+ numEtu);
			// recuperer une liste d'UE et non pas de String
			while (rs.next()) {

				UE ue = new UE(rs.getString("idue"));

				if (rs.getString("type").equals("IMPOSEE"))
					ue.setType(UE.types.IMPOSEE);
				else if (rs.getString("type").equals("COMMUN"))
					ue.setType(UE.types.COMMUN);
				else if (rs.getString("type").equals("CHOIX"))
					ue.setType(UE.types.CHOIX);

				if (rs.getInt("valide") == 1)
					ue.setValide(true);
				else
					ue.setValide(false);

				listeUE.add(ue);
			}
		} finally {
			SQLiteJDBC.close(statement);
			SQLiteJDBC.close(connection);
		}
		return listeUE;
	}

	public static TreeMap<String, ArrayList<Etudiant>> getEtudiantsPerUE(ArrayList<Etudiant> tousLesEtu)
			throws SQLException, ClassNotFoundException {
		Connection connection = null;
		Statement statement = null;
		TreeMap<String, ArrayList<Etudiant>> mapUEAndEtu = new TreeMap<>();
		ArrayList<String> listeUE = new ArrayList<String>();


		try {
			connection = (Connection) SQLiteJDBC.getConnexion();
			statement = connection.createStatement();
			// execute select SQL stetement
			ResultSet rs1 = statement
					.executeQuery("SELECT * from ue order by id");
			// ResultSet rs2 =
			// statement.executeQuery("SELECT id from ue order by id");

			while (rs1.next()) {
				listeUE.add(rs1.getString("nomue"));
			}

			for (String ue : listeUE) {

				ArrayList<Etudiant> listeEtu = new ArrayList<>();
				listeEtu = getEtudiantsFiltresForOneUE(tousLesEtu, ue);
				mapUEAndEtu.put(ue, listeEtu);
			}
		} finally {
			SQLiteJDBC.close(statement);
			SQLiteJDBC.close(connection);
		}

		return mapUEAndEtu;
	}
	

	public static ArrayList<Etudiant> getEtudiantsFiltres(ArrayList<String> lstSpe) throws SQLException, ClassNotFoundException {
		Connection connection = null;
		Statement stmt = null;
		StringBuffer attr = new StringBuffer();

		ArrayList<Etudiant> lstEtu = new ArrayList<Etudiant>();

		for (int i = 0; i < lstSpe.size(); i++) {
			attr.append('\'' + lstSpe.get(i) + '\'');
			if (i != lstSpe.size() - 1)
				attr.append(',');
		}

		String req = "SELECT e.numeroEtudiant, e.nom, e.prenom, e.mail, e.specialite, e.redoublant,"
				+ " l.idue, l.type, l.valide"
				+ " FROM etudiant e, lien l"
				+ " WHERE e.numeroEtudiant = l.numetu"
				+ " AND e.specialite IN (" + attr.toString() + ')';

		try {
			connection = (Connection) SQLiteJDBC.getConnexion();
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(req);
			if (rs.next()) {
				Etudiant e = new Etudiant();
				e.setNom(rs.getString("nom"));
				e.setPrenom(rs.getString("prenom"));
				e.setMailPerso(rs.getString("mail"));
				e.setNumero(String.valueOf(rs.getInt("numeroEtudiant")));

				e.setSpecialite(rs.getString("specialite"));
				e.setRedoublant(rs.getBoolean("redoublant"));
				e.getListeUE().add(
						new UE(UE.getType(rs.getString("type").toLowerCase()),
								rs.getString("idue"), rs.getBoolean("valide")));
				lstEtu.add(e);
			}

			while (rs.next()) {
				if (rs.getString("numeroEtudiant").equals(
						(lstEtu.get(lstEtu.size() - 1).getNumero()))) {
					lstEtu.get(lstEtu.size() - 1)
							.getListeUE()
							.add(new UE(UE.getType(rs.getString("type")
									.toLowerCase()), rs.getString("idue"), rs
									.getBoolean("valide")));
				} else {
					Etudiant e = new Etudiant();
					e.setNom(rs.getString("nom"));
					e.setPrenom(rs.getString("prenom"));
					e.setMailPerso(rs.getString("mail"));
					e.setNumero(String.valueOf(rs.getInt("numeroEtudiant")));

					e.setSpecialite(rs.getString("specialite"));
					e.setRedoublant(rs.getBoolean("redoublant"));

					e.getListeUE().add(
							new UE(UE.getType(rs.getString("type")
									.toLowerCase()), rs.getString("idue"), rs
									.getBoolean("valide")));
					lstEtu.add(e);
				}

			}
			return lstEtu;
		} finally {
			SQLiteJDBC.close(stmt);
			SQLiteJDBC.close(connection);
		}
	}

	public static ArrayList<Etudiant> getEtudiantsFiltresUE(
			ArrayList<Etudiant> lstEtu, ArrayList<String> lstUE) {
		ArrayList<Etudiant> result = new ArrayList<Etudiant>();

		for (int i = 0; i < lstEtu.size(); i++) {
			int nbUEPresente = 0;

			for (int j = 0; j < lstEtu.get(i).getListeUE().size(); j++) {
				for (int k = 0; k < lstUE.size(); k++) {
					if (lstEtu.get(i).getListeUE().get(j).getNom()
							.toLowerCase().equals(lstUE.get(k).toLowerCase())) {
						nbUEPresente += 1;
					}
				}
			}

			if (nbUEPresente == lstUE.size()) {
				result.add(lstEtu.get(i));
			}
		}
		return result;
	}
	
	public static ArrayList<Etudiant> getEtudiantsFiltresForOneUE(
			ArrayList<Etudiant> lstEtu, String ue) throws ClassNotFoundException, SQLException {
		ArrayList<Etudiant> result = new ArrayList<Etudiant>();
		
		for (Etudiant etu : lstEtu) {
	    	String numero = etu.getNumero();
	    	ArrayList<UE> listeUE ; 
			listeUE = GestionBD.getUeEtudiant(numero);
	    	for(UE ueEtu : listeUE)
				if (ueEtu.getNom().toLowerCase().equals(ue.toLowerCase()))
						result.add(etu);
		}
		return result;
	}

}
