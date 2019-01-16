package com.acuevas.marvel.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.exceptions.DBException.DBErrors;
import com.acuevas.marvel.lib.DBTable;
import com.acuevas.marvel.lib.DBTable.DBColumn;
import com.acuevas.marvel.lib.MyRunnable;
import com.acuevas.marvel.lib.QueryBuilder;
import com.acuevas.marvel.model.SuperHero;
import com.acuevas.marvel.model.User;
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

//TODO CHANGE THIS TO PRIVATE
	/**
	 * Connects the program with the DB
	 * 
	 * @throws SQLException
	 */
	public void connect() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/marvel?useSSL=false&allowPublicKeyRetrieval=true";
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
		if (connection != null)
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
	public SuperHero findHero(String name) throws DBException, SQLException {

		return (SuperHero) executeQuery(() -> {
			QueryBuilder query = new QueryBuilder();
			query.select().from(DBTable.Superhero).where(DBColumn.name, name);
			ResultSet resultSet = query.executeQuery();
			/*
			 * String query2 = "select * from superhero where name ='" + name + "';";
			 * ResultSet resultSet = statement.executeQuery(query);
			 */
			String superpower = null;
			String name2 = null;
			if (resultSet.next()) {
				name2 = resultSet.getString("name"); // Getting the name again because the user my input it in
														// underCase and it's still valid for the DB.
														// Ex: User input: superjava Real name: SuperJava
				superpower = resultSet.getString("superpower");
			} else
				throw new DBException(DBErrors.DOESNT_EXIST);
			return new SuperHero(name2, superpower);
		});

	}

	// TODO TEST THIS
	public boolean isVillianPresent(String place) throws DBException, SQLException {
		return (boolean) executeQuery(() -> {
			QueryBuilder query = new QueryBuilder();
			query.select().from(DBTable.Enemy).where(DBColumn.place, place);
			ResultSet resultSet = query.executeQuery();

			return resultSet.next();
		});
	}

	// TODO get place by name and get superhero
	// TODO TEST
	public boolean isRegistered(User user) throws DBException, SQLException {
		return (boolean) executeQuery(() -> {
			QueryBuilder query = new QueryBuilder();
			query.select().from(DBTable.User).where(DBColumn.username, user.getUsername());
			ResultSet resultSet = query.executeQuery();

			return resultSet.next();
		});
	}

	// TODO TEST
	public void register(User user) throws DBException, SQLException {
		executeQuery(() -> {
			QueryBuilder query = new QueryBuilder();
			query.insertInto(DBTable.User, getPropertiesListed(user));
			return null; // Interface MyRunable returns T so I need to return something.
		});
	}

	public User getUserByKey(String username) throws DBException, SQLException {
		return (User) executeQuery(() -> {
			QueryBuilder query = new QueryBuilder();
			query.select().from(DBTable.User).where(DBColumn.username, username);
			ResultSet resultSet = query.executeQuery();

			return null;
		});
	}

	/**
	 * This method asks the DB for a UserTO with only username&password to check if
	 * they match with the login provided by the user.
	 */
	public User getUserTOByKey(String username) {
		return (User) executeQuery(() -> {
			QueryBuilder query = new QueryBuilder();
			query.select().from(DBTable.User).where(DBColumn.username, username);
			ResultSet resultSet = query.executeQuery();

			return null;
		});
	}

	// TODO THIS
	/**
	 * Gets the properties of an instance of User and stores them into a map linking
	 * the property with the column of the DB
	 * 
	 * @param user User
	 * @return a Map where the key is the column of the DB and the value its value.
	 */
	public Map<DBColumn, Object> getPropertiesListed(User user) {
		Map<DBColumn, Object> userProperties = new HashMap<>();
		userProperties.put(DBColumn.username, user.getUsername());
		userProperties.put(DBColumn.pass, user.getPassword());
		userProperties.put(DBColumn.level, user.getLevel());
		userProperties.put(DBColumn.superhero, user.getSuperhero().getName());
		userProperties.put(DBColumn.place, user.getPlace());
		userProperties.put(DBColumn.points, user.getPoints());
		return userProperties;
	}

	/**
	 * Connects with the DB, executes the given IMyRunnable and then closes all the
	 * resources before disconnecting from the DB.
	 * 
	 * Note: No need to close ResultSets as they get closed automatically when you
	 * close its Statement/PreparedStatement.
	 * 
	 * @see Statement#close()
	 * @see StatementImpl#close()
	 * @param runnable MyRunnable ... The method to run
	 * @throws DBException  if an error occurs while running the method
	 * @throws SQLException Database access error
	 * @return Object ... An Object.
	 */
	@SuppressWarnings("rawtypes")
	private Object executeQuery(MyRunnable runnable) throws DBException, SQLException {
		Object obj;
		try {
			connect();

			obj = runnable.myRun();

		} catch (SQLException e) {
			throw e;
		} catch (DBException e) {
			throw e;
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (preparedStatement != null && !preparedStatement.isClosed())
				preparedStatement.close();
			disconnect();
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public List<String> getColumnNames(String table) throws DBException, SQLException {
		// TODO RELIES ON ORDER ATM
		return (List<String>) executeQuery(() -> {
			statement = connection.createStatement();
			List<String> columnNames = new ArrayList<>();
			String query = "select * from " + table + " limit 1;";
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.beforeFirst();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int total = resultSetMetaData.getColumnCount();
			for (int i = 1; i < total + 1; i++)
				columnNames.add(resultSetMetaData.getColumnName(i));

			return columnNames;
		});
	}

	/**
	 * 
	 * 
	 * @return Connection... the connection to the DB.
	 */
	public Connection getConnection() {
		return connection;
	}

}
