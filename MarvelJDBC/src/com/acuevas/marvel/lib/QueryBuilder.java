package com.acuevas.marvel.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	String query = "";
	List<Object> comparators = new ArrayList<>();
	Connection connection;
	PreparedStatement preparedStatement;

	boolean where = false;

	public QueryBuilder() {
		connection = MarvelDAO.getInstance().getConnection();
	}

	public boolean insertInto(DBTable dbTable, List<Object> values) throws SQLException, QueryException {
		query.concat("insert into " + dbTable.name() + " values (");
		values.forEach(value -> query.concat("?,"));
		query.substring(query.lastIndexOf(",")).replace(",", ")");
		return executeUpdate();
	}

	public ResultSet executeQuery() {
		// TODO EXECUTES THE QUERY AND RETURNS A RESULTSET.

		try {
			insertValuesIntoQuery();
		} catch (SQLException | QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private boolean executeUpdate() {
		// TODO THIS METHOD EXECUTES A QUERY WHICH RETURNS NOTHING, JUST TRUE IF
		// EVERYTHING'S OKAY.
		try {
			insertValuesIntoQuery();
		} catch (SQLException | QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void insertValuesIntoQuery() throws SQLException, QueryException {
		// TODO inserts the values into the ? of the PreparedStatement.
		preparedStatement = connection.prepareStatement(query);
		try {
			comparators.forEach(this::safeSelector);
		} catch (RuntimeException e) {
			throw new QueryException(e.getCause());
		}
		// TODO TRYCATCH TO THROW NEW EXCEPTION
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
	 * Sets the parameter given into the PreparedStatement.
	 * 
	 * @param comparator ... The object to insert.
	 * @throws SQLException   When JDBC encounters an error connecting to the DB.
	 * @throws QueryException When the object inserted is not supported.
	 */
	private void setSelector(Object comparator) throws SQLException, QueryException {
		int index = comparators.indexOf(comparator);
		@SuppressWarnings("rawtypes")
		Class objClass = comparator.getClass();

		if (objClass.equals(Integer.class) || objClass.equals(int.class))
			preparedStatement.setInt(index, (int) comparator);
		else if (objClass.equals(String.class))
			preparedStatement.setString(index, (String) comparator);
		else if (objClass.equals(Double.class) || objClass.equals(double.class))
			preparedStatement.setDouble(index, (double) comparator);
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

	public QueryBuilder from(DBTable dbTable) {
		query += ("from " + dbTable.name() + " ");
		return this;
	}

	public QueryBuilder where(DBColumn column, Object comparator) throws SQLException {
		// TODO MAYBE USE query.matches to check if the query is right?

		query += ("where " + column.name() + " = ? ");
		comparators.add(comparator);
		where = true;
		return this;
	}

	public QueryBuilder and(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query.concat("and " + column.name() + " = ? ");
		comparators.add(comparator);
		return this;
	}

	public QueryBuilder or(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query.concat("or " + column.name() + " = ? ");
		comparators.add(comparator);
		return this;
	}

}
