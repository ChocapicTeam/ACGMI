package com.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLiteJDBC
{
	
	
  public static Connection getConnexion() throws SQLException, ClassNotFoundException
  {
	    Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:gestion.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		return c;   
  }
  
  
	public static void close(Statement statement) throws SQLException
	{
		if (statement!=null) statement.close();
	}

	/** pour fermer d'un coup la connexion*/
	public static void close(Connection connection) throws SQLException
	{
		if (connection!=null) connection.close();
	}
  
}