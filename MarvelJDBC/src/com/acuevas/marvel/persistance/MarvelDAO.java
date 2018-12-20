package com.acuevas.marvel.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MarvelDAO {

	Connection connection;

	public void connect() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/marvel";
		String user = "marvel";
		String pass = "marvel";
		connection = DriverManager.getConnection(url, user, pass);
	}

	public void disconnect() throws SQLException {
		connection.close();
	}

}
