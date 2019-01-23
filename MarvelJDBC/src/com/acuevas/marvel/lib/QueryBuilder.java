package com.acuevas.marvel.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.exceptions.DBException.DBErrors;
import com.acuevas.marvel.lib.DBTable.DBColumn;
import com.acuevas.marvel.lib.exceptions.QueryException;
import com.acuevas.marvel.lib.exceptions.QueryException.QueryError;
import com.acuevas.marvel.persistance.MarvelDAO;

/**
 * Allows the creation of different complex SQL query by separating the
 * construction process. Follows the Builder design pattern.
 * 
 * @author Alex
 *
 */
public class QueryBuilder {
	private String query = "";
	private List<Object> comparators = new ArrayList<>();
	private Connection connection;
	private static PreparedStatement preparedStatement;
	private static Statement statement;

	boolean where = false;

	public QueryBuilder() {
		connection = MarvelDAO.getInstance().getConnection();

	}

	// TODO CHANGE ALL CONCAT SEEMS TO NOT WORK

	/**
	 * Inserts an object into a table.
	 * 
	 * @param dbTable
	 * @param columnValue
	 * @throws SQLException
	 * @throws QueryException
	 * @throws DBException
	 */
	public QueryBuilder insertInto(DBTable dbTable, Map<DBColumn, Object> columnValue) {
		// TODO do insert with columns instead of index
		query += ("insert into " + dbTable.name() + " values (");
		Collection<Object> values = columnValue.values();
		values.forEach(value -> query += ("?,"));
		checkEnding();
		return this;
	}

	/**
	 * Updates an object of a table.
	 * 
	 * @param dbTable
	 * @param columnValues
	 * @throws SQLException
	 */
	public QueryBuilder update(DBTable dbTable, Map<DBColumn, Object> columnValues) {
		query += ("update " + dbTable.name() + " set ");
		columnValues.keySet().forEach(dbColumn -> {
			query += (dbColumn.toString() + " = '" + columnValues.get(dbColumn) + "', ");
		});
		checkEnding();
		return this;
	}

	private void checkEnding() {
		if (query.endsWith(", ")) {
			query = query.substring(0, query.length() - 2);
			query += " ";
		}
	}

	/**
	 * Executes the query, <strong>do not use for update/insert.</strong>
	 * 
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException {
		// TODO EXECUTES THE QUERY AND RETURNS A RESULTSET.

		try {
			insertValuesIntoQuery();
		} catch (QueryException e) {
			// TODO show message, query is wrong.
			e.printStackTrace();
		}
		return preparedStatement.executeQuery();
	}

	/**
	 * Executes the query to update/insert the table.
	 * 
	 * @param columnValue
	 * @throws SQLException
	 * @throws DBException
	 */
	public void executeUpdate() throws DBException, SQLException {
		// TODO THIS METHOD EXECUTES A QUERY WHICH RETURNS NOTHING AND UPDATES DB.
		Statement statement = connection.createStatement();
		try {
			connection.setAutoCommit(false);
			statement.executeUpdate(query);
			connection.commit();
		} catch (SQLException ex) {
			connection.rollback();
			throw new DBException(DBErrors.COULD_NOT_UPDATE);
		} finally {
			connection.setAutoCommit(true);
		}
	}

	/**
	 * Sets the values into the query
	 * 
	 * @throws SQLException
	 * @throws QueryException
	 */
	private void insertValuesIntoQuery() throws SQLException, QueryException {
		// TODO inserts the values into the ? of the PreparedStatement.
		preparedStatement = connection.prepareStatement(query);
		try {
			comparators.forEach(this::safeSelector);
		} catch (RuntimeException e) {
			exceptionDiscriminator(e);
		}
	}

	/**
	 * Discriminates between the exceptions while wrapped in a RuntimeException
	 * 
	 * @param e
	 * @throws SQLException
	 * @throws QueryException
	 */
	private void exceptionDiscriminator(RuntimeException e) throws SQLException, QueryException {
		if (e.getCause().getClass() == SQLException.class)
			throw new SQLException(e);
		else
			throw new QueryException(e);
	}

	/**
	 * Safely wraps the exceptions thrown in
	 * {@link QueryBuilder#setSelector(Object)} into a RunnableException. Allowing
	 * the method to be used in the
	 * {@link Iterable#forEach(java.util.function.Consumer)} without loosing the
	 * Exceptions.
	 * 
	 * @param comparator .. The object to insert.
	 * @throws RuntimeException When a {@link SQLException} or
	 *                          {@link QueryException} is thrown.
	 */
	private void safeSelector(Object comparator) throws RuntimeException {
		try {
			setSelector(comparator);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the parameter given into the PreparedStatement discriminating by type.
	 * 
	 * @param comparator ... The object to insert.
	 * @throws SQLException   When JDBC encounters an error connecting to the DB.
	 * @throws QueryException When the object inserted is not supported.
	 */
	private void setSelector(Object comparator) throws SQLException, QueryException {
		int index = comparators.indexOf(comparator) + 1;
		@SuppressWarnings("rawtypes")
		Class objClass = comparator.getClass();

		if (objClass.equals(Integer.class) || objClass.equals(int.class))
			getPreparedStatement().setInt(index, (int) comparator);
		else if (objClass.equals(String.class))
			getPreparedStatement().setString(index, (String) comparator);
		else if (objClass.equals(Double.class) || objClass.equals(double.class))
			getPreparedStatement().setDouble(index, (double) comparator);
		else
			throw new QueryException(QueryError.NOT_SUPPORTED, comparator);
	}

	public QueryBuilder delete() {
		query += "delete ";
		return this;
	}

	public QueryBuilder select() {
		query += "select * ";
		return this;
	}

	public QueryBuilder select(DBColumn column) {
		query += ("select " + column.name() + " ");
		return this;
	}

	public QueryBuilder select(List<DBColumn> columns) {
		query += "select";
		columns.forEach(column -> {
			query += (column.name() + ", ");
		});
		checkEnding();
		return this;
	}

	public QueryBuilder from(DBTable dbTable) {
		query += ("from " + dbTable.name() + " ");
		return this;
	}

	public QueryBuilder where(DBColumn column, Object comparator) {
		// TODO MAYBE USE query.matches to check if the query is right?

		query += ("where " + column.name() + " = '" + comparator.toString() + "' ");
//		comparators.add(comparator);
		where = true;
		return this;
	}

	public QueryBuilder and(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query += ("and " + column.name() + " = '" + comparator.toString() + "'");
//		comparators.add(comparator);
		return this;
	}

	public QueryBuilder nAnd(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query += ("and " + column.name() + " != '" + comparator.toString() + "'");
//		comparators.add(comparator);
		return this;
	}

	public QueryBuilder or(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query += ("or " + column.name() + " = '" + comparator.toString() + "'");
//		comparators.add(comparator);
		return this;
	}

	public QueryBuilder nOr(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query += ("or " + column.name() + " != '" + comparator.toString() + "'");
//		comparators.add(comparator);
		return this;
	}

	/**
	 * @return the preparedStatement
	 */
	public static PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	/**
	 * @param preparedStatement the preparedStatement to set
	 */
	public static void setPreparedStatement(PreparedStatement preparedStatement) {
		QueryBuilder.preparedStatement = preparedStatement;
	}

	/**
	 * @return the statement
	 */
	public static Statement getStatement() {
		return statement;
	}

	/**
	 * @param statement the statement to set
	 */
	public static void setStatement(Statement statement) {
		QueryBuilder.statement = statement;
	}

}
