package com.acuevas.marvel.controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.model.Attack;
import com.acuevas.marvel.model.Attack.Type;
import com.acuevas.marvel.model.Hero;
import com.acuevas.marvel.persistance.DBTable;
import com.acuevas.marvel.persistance.DBTable.DBColumn;
import com.acuevas.marvel.persistance.MarvelDAO;
import com.acuevas.marvel.persistance.Query;

public class Manager {
	// IMPORTANT NOTE: MySql-ConnectorJ Drivers are v.5.1.47, more updated versions
	// give problems.

	public static void main(String[] args) {
		List<Type> types = Arrays.asList(Type.values());
		int random = new Random().nextInt(types.size());
		int random2 = new Random().nextInt(types.size());

		Attack attack1 = new Attack(types.get(random));
		Attack attack2 = new Attack(types.get(random2));
		System.out.println(attack1.getType());
		System.out.println(attack2.getType());
		System.out.println(attack1.compareTo(attack2));

		try {
			Hero hero = MarvelDAO.getInstance().findHero("superjava");
			System.out.println(hero.getName());
			System.out.println(hero.getSuperpower());
			MarvelDAO.getInstance().toString();

			List<String> list = MarvelDAO.getInstance().getColumnNames("gem");

//			list.forEach(System.out::println);

			Query query = new Query();

			query.select(DBColumn.name).from(DBTable.Gem).where(DBColumn.name, "Mind Gem");

		} catch (DBException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
