package com.acuevas.marvel.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.exceptions.DBException.DBErrors;
import com.acuevas.marvel.model.Hero;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.StatementImpl;

/**
 * Singleton class which manages all the Inputs/Outputs related with data, DAO
 * stands for Data Access Object.
 * 
 * @author Alex
 *
 */
public class MarvelDAO {

	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private static MarvelDAO marvelDAO;

	/**
	 * Constructor
	 */
	private MarvelDAO() {
	}

	/**
	 * Gets the actual instance of the MarvelDAO class.
	 * 
	 * @return MarvelDAO ... An instance of MarvelDAO.
	 */
	public static MarvelDAO getInstance() {
		if (marvelDAO == null)
			marvelDAO = new MarvelDAO();
		return marvelDAO;
	}

	/**
	 * Connects the program with the DB
	 * 
	 * @throws SQLException
	 */
	private void connect() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/marvel?useSSL=false";
		String user = "marvel";
		String pass = "marvel";
		connection = DriverManager.getConnection(url, user, pass);
	}

	/**
	 * Disconnects the program from the DB
	 * 
	 * @throws SQLException
	 */
	private void disconnect() throws SQLException {
		connection.close();
	}

	/**
	 * Gets a Hero instance with the specified name from the DB.
	 * 
	 * @param name String ... the name of the Hero you want to get.
	 * @return
	 * @return Hero ... an instance of Hero from the DB
	 * @throws SQLException Database access error
	 * @throws DBException  if the name given doesn't exist in the DB
	 */
	public Hero findHero(String name) throws DBException, SQLException {

		return (Hero) marvelDAO.execute(() -> {
			statement = connection.createStatement();
			String query = "select * from superhero where name ='" + name + "'";
			ResultSet resultSet = statement.executeQuery(query);
			String superpower = null;
			if (resultSet.next())
				superpower = resultSet.getString("superpower");
			else
				throw new DBException(DBErrors.DOESNT_EXIST);
			return new Hero(name, superpower);
		});

	}

	/**
	 * Connects with the DB, executes the given IMyRunnable and then closes all the
	 * resources before disconnecting from the DB.
	 * 
	 * Note: No need to close ResultSets as they close automatically when you close
	 * its Statement/PreparedStatement.
	 * 
	 * @see Statement#close()
	 * @see StatementImpl#close()
	 * @param runnable IMyRunnable ... The method to run
	 * @throws DBException  if an error occurs while running the method
	 * @throws SQLException Database access error
	 * @return Object ... An Object.
	 */
	@SuppressWarnings("rawtypes")
	private Object execute(IMyRunnable runnable) throws DBException, SQLException {
		Object obj;
		try {
			connect();

			obj = runnable.myRun();

			if (statement != null && !statement.isClosed())
				statement.close();
			if (preparedStatement != null && !preparedStatement.isClosed())
				preparedStatement.close();
			disconnect();
		} catch (SQLException e) {
			throw e;
		} catch (DBException e) {
			throw e;
		} catch (Exception e) {
			throw new DBException(e);
		}
		return obj;
	}

}
