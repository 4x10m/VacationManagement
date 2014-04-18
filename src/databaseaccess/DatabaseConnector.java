package databaseaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
	private boolean connected;
	private Connection connection = null;

	public boolean is_connected() {
		return this.connected;
	}

	public DatabaseConnector() {
		this.connected = false;
	}

	public boolean connect(final String host, final String database_name,
			final String user, final String password) throws SQLException {
		String url = "jdbc:postgresql://" + host + database_name;

		System.out.println("Connecting to database ...");

		try {
			this.connection = DriverManager.getConnection(url, user, password);

			this.connected = true;

		} catch (SQLException e) {
			System.out.println(e.getMessage());

			throw e;
		}

		return this.connected;
	}

	public Connection getConnection() {
		return this.connection;
	}
}
