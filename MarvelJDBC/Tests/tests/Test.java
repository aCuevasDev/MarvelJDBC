package tests;

import java.sql.SQLException;

import com.acuevas.marvel.lib.DBTable;
import com.acuevas.marvel.lib.DBTable.DBColumn;
import com.acuevas.marvel.lib.QueryBuilder;

class Test {

	@org.junit.jupiter.api.Test
	void testRuntimeExceptionKeepsType() {
		try {
//			MarvelDAO.getInstance().connect();
			QueryBuilder query = new QueryBuilder();
			query.select().from(DBTable.Enemy).where(DBColumn.debility, new Object()).executeQuery();
		} catch (SQLException e) {
			System.out.println("SQLEx");
		}
	}

}
