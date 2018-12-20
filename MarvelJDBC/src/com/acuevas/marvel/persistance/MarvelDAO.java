package com.acuevas.marvel.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.exceptions.DBException.DBErrors;
import com.acuevas.marvel.model.Hero;

public class MarvelDAO {

	private Connection connection;
	private static MarvelDAO marvelDAO;

	private MarvelDAO() {
		// TODO Auto-generated constructor stub
	}

	public static MarvelDAO getInstance() {
		if (marvelDAO == null)
			marvelDAO = new MarvelDAO();
		return marvelDAO;
	}

	private void connect() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/marvel?useSSL=false";
		String user = "marvel";
		String pass = "marvel";
		connection = DriverManager.getConnection(url, user, pass);
	}

	private void disconnect() throws SQLException {
		connection.close();
	}

	public Hero findHero(String name) throws SQLException, DBException {
		connect();
		Statement statement;
		statement = connection.createStatement();
		String query = "select * from superhero where name ='" + name + "'";
		ResultSet resultSet = statement.executeQuery(query);
		String superpower = "";
		if (resultSet.next())
			superpower = resultSet.getString("superpower");
		else
			throw new DBException(DBErrors.DOESNT_EXIST);

		resultSet.close();
		statement.close();
		disconnect();
		return new Hero(name, superpower);
	}

}
