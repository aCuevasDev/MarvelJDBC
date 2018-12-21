package com.acuevas.marvel.persistance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.acuevas.marvel.persistance.DBTable.DBColumn;
import com.mysql.jdbc.Connection;

public class Query {
	String query;
	Connection connection;
	PreparedStatement preparedStatement;

	public Query() {
		query = "select * from ?";
	}

	public Query select(DBColumn column) {
		query.replace("select *", "select " + column.name());
		return this;
	}

	public Query from(DBTable dbTable) {
		query.replace("from ?", "from " + dbTable.name());
		return this;
	}

	public Query where(DBColumn column, LinkedHashMap<DBColumn, Object> comparators) throws SQLException {
		// TODO MAYBE USE query.matches to check if the query is right?

		query.concat("where " + column.name() + " = '");
		List<Object> comparatorsValues = new ArrayList<Object>(comparators.values());
		comparatorsValues.forEach(comparator -> query.concat("?,"));
		query.substring(query.lastIndexOf(",")).replace(",", ""); // TODO CHECK BETTER WAY TO THIS WITH DOCUMENTATION

		List<DBColumn> preparedStatement = (List<DBColumn>) connection.prepareStatement(query);
		/*
		 * comparatorsValues.forEach(comparator -> { int i = 0; if
		 * (comparator.getClass().equals(String.class))
		 * preparedStatement.setString(comparators., arg1); });
		 */
		return this;
	}

}
