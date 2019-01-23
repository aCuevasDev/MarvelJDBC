package com.acuevas.marvel.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.acuevas.marvel.model.GemTO;
import com.acuevas.marvel.model.Place;
import com.acuevas.marvel.model.SuperHero;
import com.acuevas.marvel.model.User;
import com.acuevas.marvel.view.View;
import com.mysql.jdbc.StatementImpl;

/**
 * Singleton class which manages all the Inputs/Outputs related with data, DAO
 * stands for Data Access Object.
 * 
 * @author Alex
 *
 */

public class MarvelDAO {

	// TODO ELIMINATE DBEXCEPTION THROWS AND PUT A TRYCATCH TO CONTROL ERROR

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
	 * Gets all the heroes from the DB
	 * 
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<SuperHero> findAllHeroes() throws SQLException {
		try {
			return (List<SuperHero>) executeQuery(() -> {
				List<SuperHero> superHeroes = new ArrayList<>();
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.Superhero);
				ResultSet resultSet = query.executeQuery();
				while (resultSet.next()) {
					String name = resultSet.getString(DBColumn.name.toString());
					String superpower = resultSet.getString(DBColumn.superpower.toString());
					superHeroes.add(new SuperHero(name, superpower));
				}
				return superHeroes;
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
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
	public SuperHero findHero(String name) throws SQLException {

		try {
			return (SuperHero) executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.Superhero).where(DBColumn.name, name);
				ResultSet resultSet = query.executeQuery();
				String superpower = null;
				String name2 = null;
				if (resultSet.next()) {
					name2 = resultSet.getString(DBColumn.name.toString());
					// Getting the name again because the user may input it in
					// underCase and it's still valid for the DB.
					// Ex: User input: superjava Real name: SuperJava
					// Must be a new variable (name2) because of the different enclosing space.
					superpower = resultSet.getString(DBColumn.superpower.toString());
				} else
					throw new DBException(DBErrors.DOESNT_EXIST);
				return new SuperHero(name2, superpower);
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	/**
	 * Inserts a user into the DB
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void insert(User user) throws SQLException {

		try {
			if (!isRegistered(user.getUsername())) {
				executeQuery(() -> {
					QueryBuilder query = new QueryBuilder();
					List<Object> values = new ArrayList<>();

					values.add(user.getUsername());
					values.add(user.getPassword());
					values.add(user.getLevel());
					values.add(user.getSuperhero().getName());
					values.add(user.getPlace().getName());
					values.add(user.getPoints());

					query.insertInto(DBTable.User, values);
					return null;
				});
			} else
				throw new DBException(DBErrors.USER_ALREADY_EXISTS);
		} catch (DBException e) {
			View.printError(e.getMessage());
		}

	}

	/**
	 * Gets the gems on a place without owner
	 * 
	 * @param place
	 * @return a List of gems
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<GemTO> getGemsWithoutOwnerOn(Place place) throws SQLException {
		try {
			return (List<GemTO>) executeQuery(() -> {
				List<GemTO> gems = new ArrayList<>();
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.Gem).whereNull(DBColumn.owner).and(DBColumn.place, place.getName());
				ResultSet resultSet = query.executeQuery();
				while (resultSet.next()) {
					String name = resultSet.getString(DBColumn.name.toString());
					String user = resultSet.getString(DBColumn.user.toString());
					String owner = resultSet.getString(DBColumn.owner.toString());
					String place2 = resultSet.getString(DBColumn.place.toString());
					gems.add(new GemTO(name, user, owner, place2));
				}
				return gems;
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	/**
	 * Gets a number of random places from the DB
	 * 
	 * @param limit the number of places you want
	 * @return List<Place>
	 * @throws SQLException
	 */
	public List<Place> getRandomPlace(int limit) throws SQLException {
		List<Place> places = new ArrayList<>();
		String query = "select * from place order by rand() limit " + limit;
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		while (resultSet.next()) {
			String name2 = resultSet.getString(DBColumn.name.toString());
			String description = resultSet.getString(DBColumn.description.toString());
			String north = resultSet.getString(DBColumn.north.toString());
			String south = resultSet.getString(DBColumn.south.toString());
			String east = resultSet.getString(DBColumn.east.toString());
			String west = resultSet.getString(DBColumn.west.toString());
			places.add(new Place(name2, description, north, south, east, west));
		}
		return places;

	}

	/**
	 * Gets if a villain is present on a place
	 * 
	 * @param place
	 * @return
	 * @throws DBException
	 * @throws SQLException
	 */
	public Boolean isVillianPresent(String place) throws SQLException {
		try {
			return (boolean) executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.Enemy).where(DBColumn.place, place);
				ResultSet resultSet = query.executeQuery();

				return resultSet.next();
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	/**
	 * Gets if a user is registered
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public Boolean isRegistered(String username) throws SQLException {
		try {
			return (boolean) executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.User).where(DBColumn.username, username);
				ResultSet resultSet = query.executeQuery();

				return resultSet.next();
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	/**
	 * Gets a user by the primary key
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public User getUserByKey(String username) throws SQLException {
		try {
			return (User) executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.User).where(DBColumn.username, username);
				ResultSet resultSet = query.executeQuery();
				resultSet.next();
				String username2 = resultSet.getString(DBColumn.username.toString());
				String pswrd = resultSet.getString(DBColumn.pass.toString());
				int level = resultSet.getInt(DBColumn.level.toString());
				SuperHero hero = findHero(resultSet.getString(DBColumn.superhero.toString()));
				Place place = getPlaceByKey(resultSet.getString(DBColumn.place.toString()));
				int points = resultSet.getInt(DBColumn.points.toString());
				return new User(username2, pswrd, level, hero, place, points);
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	/**
	 * This method asks the DB for a UserTO with only username&password to check if
	 * they match with the login provided by the user.
	 * 
	 * @throws SQLException
	 * @throws DBException
	 */
	public User getUserTOByKey(String username) throws SQLException {
		try {
			return (User) executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.User).where(DBColumn.username, username);
				ResultSet resultSet = query.executeQuery();
				resultSet.next();
				return new User(resultSet.getString(DBColumn.username.toString()),
						resultSet.getString(DBColumn.pass.toString()));
			});

		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	// TODO THIS
	/**
	 * Gets the properties of an instance of User and stores them into a map linking
	 * the property with the column of the DB
	 * 
	 * @param user User
	 * @return a Map where the key is the column of the DB and the value its value.
	 */
//	public Map<DBColumn, Object> getPropertiesListed(User user) {
//		Map<DBColumn, Object> userProperties = new HashMap<>();
//		userProperties.put(DBColumn.username, user.getUsername());
//		userProperties.put(DBColumn.pass, user.getPassword());
//		userProperties.put(DBColumn.level, user.getLevel());
//		userProperties.put(DBColumn.superhero, user.getSuperhero().getName());
//		userProperties.put(DBColumn.place, user.getPlace());
//		userProperties.put(DBColumn.points, user.getPoints());
//		return userProperties;
//	} 

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
			if (getStatement() != null && !getStatement().isClosed())
				getStatement().close();
			if (getPreparedStatement() != null && !getPreparedStatement().isClosed())
				getPreparedStatement().close();
			disconnect();
		}
		return obj;
	}

	/*
	 * @SuppressWarnings("unchecked") public List<String> getColumnNames(String
	 * table) throws DBException, SQLException { // TODO RELIES ON ORDER ATM return
	 * (List<String>) executeQuery(() -> {
	 * setStatement(connection.createStatement()); List<String> columnNames = new
	 * ArrayList<>(); String query = "select * from " + table + " limit 1;";
	 * ResultSet resultSet = getStatement().executeQuery(query);
	 * resultSet.beforeFirst(); ResultSetMetaData resultSetMetaData =
	 * resultSet.getMetaData(); int total = resultSetMetaData.getColumnCount(); for
	 * (int i = 1; i < total + 1; i++)
	 * columnNames.add(resultSetMetaData.getColumnName(i));
	 * 
	 * return columnNames; }); }
	 */

	/**
	 * 
	 * 
	 * @return Connection... the connection to the DB.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Gets the place from DB using the primary key
	 * 
	 * @param direction
	 * @return ... Place
	 * @throws SQLException
	 */
	public Place getPlaceByKey(String name) throws SQLException {
		try {
			return (Place) executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				query.select().from(DBTable.Place).where(DBColumn.name, name);
				ResultSet resultSet = query.executeQuery();
				if (resultSet.next()) {
					String name2 = resultSet.getString(DBColumn.name.toString());
					String description = resultSet.getString(DBColumn.description.toString());
					String north = resultSet.getString(DBColumn.north.toString());
					String south = resultSet.getString(DBColumn.south.toString());
					String east = resultSet.getString(DBColumn.east.toString());
					String west = resultSet.getString(DBColumn.west.toString());
					return new Place(name2, description, north, south, east, west);
				} else
					throw new DBException(DBErrors.DOESNT_EXIST);
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
		return null;
	}

	/**
	 * Updates a Gem to the DB.
	 * 
	 * @param gem
	 * @throws SQLException
	 */
	public void updateGem(final GemTO gem) throws SQLException {
		try {
			executeQuery(() -> {
				QueryBuilder query = new QueryBuilder();
				Map<DBColumn, Object> properties = new HashMap<>();

				properties.put(DBColumn.owner, gem.getOwner());
				properties.put(DBColumn.place, gem.getPlace());

				query.update(DBTable.Gem, properties).where(DBColumn.name, gem.getName()).and(DBColumn.user,
						gem.getUser());

				query.executeUpdate();

				return null;
			});
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
	}

	/**
	 * @return the preparedStatement
	 */
	public PreparedStatement getPreparedStatement() {
		return QueryBuilder.getPreparedStatement();
	}

	/**
	 * @return the statement
	 */
	public Statement getStatement() {
		return QueryBuilder.getStatement();
	}

}
