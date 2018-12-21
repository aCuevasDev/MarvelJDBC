package com.acuevas.marvel.persistance;

import java.util.Arrays;
import java.util.List;

public enum DBTable {
	Gem(DBColumn.name, DBColumn.user, DBColumn.owner, DBColumn.place), Enemy, Superhero, User, Place;
	private List<DBColumn> columns;

	private DBTable(DBColumn... columns) {
		this.columns = Arrays.asList(columns);
	}

	public List<DBColumn> getColumns() {
		return columns;
	}

	@SuppressWarnings("rawtypes")
	public enum DBColumn {
		name(String.class), user(String.class), owner(String.class), place(String.class), superpower(String.class),
		debility(String.class), level(int.class), username(String.class), pass(String.class), superhero(String.class),
		points(int.class), description(String.class), north(String.class), south(String.class), east(String.class),
		west(String.class);

		Class returnType;

		DBColumn(Class class1) {
			returnType = class1;
		}
	}
}
