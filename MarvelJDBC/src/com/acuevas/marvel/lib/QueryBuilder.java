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
import java.util.stream.Collectors;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.exceptions.DBException.DBErrors;
import com.acuevas.marvel.lib.DBTable.DBColumn;
import com.acuevas.marvel.lib.exceptions.QueryException;
import com.acuevas.marvel.lib.exceptions.QueryException.QueryError;
import com.acuevas.marvel.persistance.MarvelDAO;
import com.acuevas.marvel.view.View;

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
	boolean innerJoin = false;

	public QueryBuilder() {
		connection = MarvelDAO.getInstance().getConnection();

	}

	/**
	 * Inserts an object into a table. MUST CONTAIN ALL THE VALUES
	 * 
	 * @param dbTable
	 * @param columnValues
	 * @throws SQLException
	 * @throws QueryException
	 * @throws DBException
	 */
	public void insertInto(DBTable dbTable, List<Object> values) throws DBException, SQLException {
		try {
			connection.setAutoCommit(false);
			query += ("insert into " + dbTable.name() + " values (");
			comparators.addAll(values);
			values.forEach(value -> query += (" ? ,"));
			checkEnding(true);
			insertValuesIntoQuery();
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			connection.rollback();
			throw new DBException(DBErrors.COULD_NOT_UPDATE);
		} catch (QueryException e) {
			View.printError(e.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
	}

	/**
	 * Inserts the specified columns of an object into a table.
	 * 
	 * @param dbTable
	 * @param columnValues
	 * @throws DBException
	 * @throws SQLException
	 */
	public void insertInto(DBTable dbTable, Map<DBColumn, Object> columnValues) throws DBException, SQLException {
		try {
			connection.setAutoCommit(false);
			query += ("insert into " + dbTable + " (" + separator(columnValues) + ") values (");
			Collection<Object> values = columnValues.values();
			comparators.addAll(values);
			values.forEach(value -> query += (" ? ,"));
			checkEnding(true);
			insertValuesIntoQuery();
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			connection.rollback();
			throw new DBException(DBErrors.COULD_NOT_UPDATE);
		} catch (QueryException e) {
			View.printError(e.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
	}

	/**
	 * Separates the column names with a ","
	 * 
	 * @param columnValues
	 * @return
	 * @return
	 */
	private String separator(Map<DBColumn, Object> columnValues) {
		return columnValues.keySet().stream().map(column -> column.name()).collect(Collectors.joining(", "));
	}

	public void gemsWithoutOwnerQuery(String username) {
		query = "select distinct g.name, g.place,g.user,g.owner  from Gem g inner join Enemy e on g.owner is null and g.place = e.place and g.user = '"
				+ username + "'";
	}

	/**
	 * Updates an object of a table.
	 * 
	 * @param dbTable
	 * @param columnValues
	 * @throws QueryException
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

	private void checkEnding(boolean nothing) {
		if (query.endsWith(",")) {
			query = query.substring(0, query.length() - 1);
			query += ") ";
		}
	}

	/**
	 * Executes the query, <strong>do not use for update/insert.</strong>
	 * 
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException {
		try {
			insertValuesIntoQuery();
		} catch (QueryException e) {
			View.printError(e.getMessage());
		}
		return preparedStatement.executeQuery();
	}

	/**
	 * Executes the query to update the table.
	 * 
	 * @param columnValue
	 * @throws SQLException
	 * @throws DBException
	 */
	public void executeUpdate() throws DBException, SQLException {
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

	public QueryBuilder from(DBTable dbTable, char varName) {
		query += ("from " + dbTable.name() + " " + varName);
		return this;
	}

	public QueryBuilder where(DBColumn column, Object comparator) {
		query += ("where " + column.name() + " = '" + comparator.toString() + "' ");
		where = true;
		return this;
	}

	public QueryBuilder nWhere(DBColumn column, Object comparator) {
		query += ("where " + column.name() + " != '" + comparator.toString() + "' ");
		where = true;
		return this;
	}

	public QueryBuilder whereNotNull(DBColumn column) {
		query += ("where " + column.name() + " IS NOT NULL ");
		where = true;
		return this;
	}

	public QueryBuilder whereNull(DBColumn column) {
		query += ("where " + column.name() + " IS NULL ");
		where = true;
		return this;
	}

	public QueryBuilder and(DBColumn column, Object comparator) {
		try {
			if (!(where || innerJoin))
				throw new QueryException(QueryError.WHERE_BEFORE);

			query += ("and " + column.name() + " = '" + comparator.toString() + "'");
		} catch (QueryException e) {
			View.printError(e.getMessage());
		}
		return this;
	}

	public QueryBuilder nAnd(DBColumn column, Object comparator) {
		try {
			if (!(where || innerJoin))
				throw new QueryException(QueryError.WHERE_BEFORE);

			query += ("and " + column.name() + " != '" + comparator.toString() + "'");
		} catch (QueryException e) {
			View.printError(e.getMessage());
		}
		return this;
	}

	public QueryBuilder or(DBColumn column, Object comparator) {
		try {
			if (!(where || innerJoin))
				throw new QueryException(QueryError.WHERE_BEFORE);

			query += ("or " + column.name() + " = '" + comparator.toString() + "'");
		} catch (QueryException e) {
			View.printError(e.getMessage());
		}
		return this;
	}

	public QueryBuilder nOr(DBColumn column, Object comparator) {
		try {
			if (!(where || innerJoin))
				throw new QueryException(QueryError.WHERE_BEFORE);

			query += ("or " + column.name() + " != '" + comparator.toString() + "'");
		} catch (QueryException e) {
			View.printError(e.getMessage());
		}
		return this;
	}

	public QueryBuilder orderBy(DBColumn column) {
		query += ("order by " + column.name());
		return this;
	}

	public QueryBuilder orderByRandom() {
		query += ("order by rand() ");
		return this;
	}

	public QueryBuilder limit(int limit) {
		query += ("limit " + limit + " ");
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

	public QueryBuilder innerJoin(DBTable dbTable) throws QueryException {
		if (where)
			throw new QueryException(QueryError.NOT_WHERE_BEFORE);

		query += "inner join " + dbTable.name() + " ";
		innerJoin = true;
		return this;
	}

	public QueryBuilder innerJoin(DBTable dbTable, char varName) throws QueryException {
		if (where)
			throw new QueryException(QueryError.NOT_WHERE_BEFORE);

		query += "inner join " + dbTable.name() + " " + varName;
		innerJoin = true;
		return this;
	}

	public QueryBuilder on(DBTable dbTable1, DBColumn dbColumn1, DBTable dbTable2, DBColumn dbColumn2)
			throws QueryException {
		if (!innerJoin)
			throw new QueryException(QueryError.INNER_BEFORE);

		query += "on " + dbTable1.name() + "." + dbColumn1.name() + " = " + dbTable2.name() + "." + dbColumn2.name()
				+ " ";
		return this;
	}

	public QueryBuilder onNull(DBTable dbTable, DBColumn dbColumn) throws QueryException {
		if (!innerJoin)
			throw new QueryException(QueryError.INNER_BEFORE);

		query += "on " + dbTable.name() + "." + dbColumn.name() + " is null ";
		return this;
	}

	public QueryBuilder innerAnd(DBTable dbTable1, DBColumn dbColumn1, DBTable dbTable2, DBColumn dbColumn2)
			throws QueryException {
		if (!innerJoin)
			throw new QueryException(QueryError.INNER_BEFORE);

		query += "and " + dbTable1.name() + "." + dbColumn1.name() + " = " + dbTable2.name() + "." + dbColumn2.name()
				+ " ";

		return this;
	}

}
