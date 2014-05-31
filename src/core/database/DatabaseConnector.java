package core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
	private Connection connection = null;

	public DatabaseConnector() {

	}

	public void connect() {
		System.out.println("Tentative de connexion a la base de donnée");

		try {
			String url = "jdbc:postgresql://localhost/vacation_management";
			this.connection = DriverManager.getConnection(url, "postgres",
					"postgres");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("connexion a la base de donnée établie");
	}

	public Connection getConnection() {
		return this.connection;
	}
}
