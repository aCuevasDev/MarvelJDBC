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

public class Query {
	String query = "";
	List<Object> comparators = new ArrayList<>();
	Connection connection;
	PreparedStatement preparedStatement;

	boolean where = false;

	public Query() {
	}

	public ResultSet insertInto(DBTable dbTable, List<Object> values) {
		query.concat("insert into " + dbTable.name() + " values (");
		values.forEach(value -> query.concat("?,"));
		query.substring(query.lastIndexOf(",")).replace(",", "");
		return executeQuery();
	}

	private ResultSet executeQuery() {
		insertValuesIntoQuery();
		// TODO Auto-generated method stub
		return null;
	}

	private void insertValuesIntoQuery() {
		// TODO Auto-generated method stub

	}

	public Query delete() {
		query += "delete ";
		return this;
	}

	public Query select() {
		query += "select * ";
		return this;
	}

	public Query select(DBColumn column) {
		query += ("select " + column.name() + " ");
		return this;
	}

	public Query from(DBTable dbTable) {
		query += ("from " + dbTable.name() + " ");
		return this;
	}

	public Query where(DBColumn column, Object comparator) throws SQLException {
		// TODO MAYBE USE query.matches to check if the query is right?

		query += ("where " + column.name() + " = ? ");
		comparators.add(comparator);
		where = true;
		return this;
	}

	public Query and(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query.concat("and " + column.name() + " = ? ");
		comparators.add(comparator);
		return this;
	}

	public Query or(DBColumn column, Object comparator) throws QueryException {
		if (!where)
			throw new QueryException(QueryError.WHERE_BEFORE);

		query.concat("or " + column.name() + " = ? ");
		comparators.add(comparator);
		return this;
	}

	// TODO IN THE SET WHATEVER SELECTOR THROW ERROR IF THE ARGUMENT GIVEN IT'S NOT
	// CORRECT (INVALIDARGUMENT)

}
