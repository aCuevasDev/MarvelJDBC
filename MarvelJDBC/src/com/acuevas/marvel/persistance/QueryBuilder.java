package com.acuevas.marvel.persistance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.acuevas.marvel.exceptions.QueryException;
import com.acuevas.marvel.exceptions.QueryException.QueryError;
import com.acuevas.marvel.persistance.DBTable.DBColumn;
import com.mysql.jdbc.Connection;

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
	}

	public boolean insertInto(DBTable dbTable, List<Object> values) {
		query.concat("insert into " + dbTable.name() + " values (");
		values.forEach(value -> query.concat("?,"));
		query.substring(query.lastIndexOf(",")).replace(",", "");
		return executeUpdate();
	}

	private ResultSet executeQuery() {
		// TODO EXECUTES THE QUERY AND RETURNS A RESULTSET.
		insertValuesIntoQuery();
		return null;
	}

	private boolean executeUpdate() {
		// TODO THIS METHOD EXECUTES A QUERY WHICH RETURNS NOTHING, JUST TRUE IF
		// EVERYTHING'S OKAY.
		insertValuesIntoQuery();
		return false;
	}

	private void insertValuesIntoQuery() {
		// TODO inserts the values into the ? of the PreparedStatement.

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

	// TODO IN THE SET SELECTOR, THROW ERROR IF THE ARGUMENT GIVEN IT'S NOT
	// CORRECT (INVALIDARGUMENT)

}
