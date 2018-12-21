package com.acuevas.marvel.persistance;

public enum DBTable {
	Gem, Enemy, Superhero, User, Place;

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
