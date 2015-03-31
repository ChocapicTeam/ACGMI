package com.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.gestion.Etudiant;
import com.gestion.UE;

public class GestionBD {

	public static void creationTable() throws SQLException,
			ClassNotFoundException {
		Connection connection = SQLiteJDBC.getConnexion();
		Statement statement = connection.createStatement();
		// crÈation des 2 tables,si elles existaient deja, une SQLException
		// sera alors lancee
		// par jdbc. On attrape l'erreur dans le bloc catch.
		try {
			statement.executeUpdate("DROP TABLE IF EXISTS etudiant ");
			statement.executeUpdate("DROP TABLE IF EXISTS lien ");
			statement.executeUpdate("DROP TABLE IF EXISTS ue ");

			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS etudiant "
							+ "(numeroEtudiant INTEGER UNIQUE, nom VARCHAR(25) , prenom VARCHAR(25) ,"
							+ "mail VARCHAR(40) ,  specialite VARCHAR(10), redoublant INTEGER not NULL," +
                            " PRIMARY KEY(numeroEtudiant, nom, prenom))");

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS ue "
					+ "(id INTEGER PRIMARY KEY,nomue VARCHAR(10))");

			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS lien "
                            + "(numetu INTEGER, idue VARCHAR(10), type VARCHAR(20), valide INTEGER, primary key(numetu,idue))");
            statement.executeUpdate("CREATE unique INDEX ind" +
                    " on lien (numetu, idue);");
		}

		catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			SQLiteJDBC.close(statement);
			SQLiteJDBC.close(connection);
		}
	}

	public static void ajouterEtudiant(List<Etudiant> liteEtu) throws SQLException,
			ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statementEtudiant = null;
		try {
			// Ajout d'un etudiant dans la base de donnée
			connection = SQLiteJDBC.getConnexion();
            connection.setAutoCommit(false);
            statementEtudiant = connection
                    .prepareStatement("INSERT or replace INTO etudiant(numeroEtudiant,nom,prenom,mail,specialite,redoublant) VALUES (?,?,?,?,?,?)");
            for(int i=0; i<liteEtu.size();i++) {
                statementEtudiant.setInt(1, Integer.parseInt(liteEtu.get(i).getNumero()));
                statementEtudiant.setString(2, liteEtu.get(i).getNom());
                statementEtudiant.setString(3, liteEtu.get(i).getPrenom());
                statementEtudiant.setString(4, liteEtu.get(i).getMailPerso());
                statementEtudiant.setString(5, liteEtu.get(i).getSpecialite());
                statementEtudiant.setBoolean(6, liteEtu.get(i).isRedoublant());
                /*if (liteEtu.get(i).isRedoublant()) {
                    statementEtudiant.setInt(6, 1);
                } else {
                    statementEtudiant.setInt(6, 0);
                }
                statementEtudiant.executeUpdate();*/
                statementEtudiant.addBatch();
            }
            statementEtudiant.executeBatch();
            connection.commit();
		} finally {
			SQLiteJDBC.close(statementEtudiant);
			SQLiteJDBC.close(connection);
		}
	}

	public static void ajouterLien(List<Etudiant> listeEtudiant) throws SQLException,
			ClassNotFoundException {
		Connection connection = null;
		PreparedStatement statementLien = null;

		try {
                connection = SQLiteJDBC.getConnexion();
                connection.setAutoCommit(false);
            statementLien = connection
                    .prepareStatement("INSERT or replace INTO lien(numetu,idue, type, valide) VALUES (?,?,?,?)");

            for (Etudiant etu : listeEtudiant) {
                for (UE ue : etu.getListeUE()) {
                    //statementLien.clearParameters();
                    statementLien.setInt(1, Integer.parseInt(etu.getNumero()));
                    statementLien.setString(2, ue
                            .getNom().toUpperCase());
                    statementLien.setString(3, ue
                            .getType().toString());
                    statementLien.setBoolean(4, ue
                            .isValide());
                    statementLien.addBatch();
                }
            }
            statementLien.executeBatch();
            connection.commit();
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

    public static HashMap<String, Integer> recupererNombreEtuPerUe ()
            throws SQLException, ClassNotFoundException {
        HashMap<String, Integer> res = new HashMap<>();
        Connection connection = null;
        Statement statement = null;

        try {
            connection = (Connection) SQLiteJDBC.getConnexion();
            statement = connection.createStatement();
            // execute select SQL stetement
            ResultSet rs = statement
                    .executeQuery("SELECT distinct ue.nomue, count (lien.idue) " +
                            "as nb from ue left join lien on ue.nomue = lien.idue " +
                            "and lien.valide = 0 group by ue.nomue order by ue.id");
            while (rs.next()) {
                res.put(rs.getString("nomue"), rs.getInt("nb"));
            }
        }
        finally {
            SQLiteJDBC.close(statement);
            SQLiteJDBC.close(connection);
        }

        return res;
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
			connection = SQLiteJDBC.getConnexion();
            connection.setAutoCommit(false);
            statement =connection
                    .prepareStatement("INSERT INTO ue(id,nomue) VALUES (?,?)");
            for (int i = 0; i < tabUe.length; i++) {
				statement.setInt(1, i + 1);
				statement.setString(2, tabUe[i]);
				//statement.executeUpdate();
                statement.addBatch();
			}
            statement.executeBatch();
            connection.commit();
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
			connection = SQLiteJDBC.getConnexion();
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
