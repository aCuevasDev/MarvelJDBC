package com.acuevas.marvel.controller;

import java.sql.SQLException;

import com.acuevas.marvel.exceptions.CommandException;
import com.acuevas.marvel.exceptions.CommandException.CommandErrors;
import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.model.Place;
import com.acuevas.marvel.model.User;
import com.acuevas.marvel.persistance.MarvelDAO;

public class Manager {
	// IMPORTANT NOTE: MySql-ConnectorJ Drivers are v.5.1.47, more updated versions
	// give problems.

	private User loggedInUser;

	public static void main(String[] args) {

		Place place = new Place("test", "testDesc", "Asgard", "Midgard", null, null);
		place.directionsAvaliable().forEach(direction -> System.out.println(direction));

		/*
		 * List<Type> types = Arrays.asList(Type.values()); int random = new
		 * Random().nextInt(types.size()); int random2 = new
		 * Random().nextInt(types.size());
		 * 
		 * Attack attack1 = new Attack(types.get(random)); Attack attack2 = new
		 * Attack(types.get(random2)); System.out.println(attack1.getType());
		 * System.out.println(attack2.getType());
		 * System.out.println(attack1.compareTo(attack2));
		 * 
		 * try { SuperHero hero = MarvelDAO.getInstance().findHero("superjava");
		 * System.out.println(hero.getName()); System.out.println(hero.getSuperpower());
		 * MarvelDAO.getInstance().toString();
		 * 
		 * List<String> list = MarvelDAO.getInstance().getColumnNames("gem");
		 * 
		 * // list.forEach(System.out::println);
		 * 
		 * QueryBuilder query = new QueryBuilder();
		 * 
		 * query.select(DBColumn.name).from(DBTable.Gem).where(DBColumn.name,
		 * "Mind Gem");
		 * 
		 * } catch (DBException e) { e.printStackTrace(); } catch (SQLException e) {
		 * e.printStackTrace(); }
		 */
	}

	public void commandArguments(int argumentsLine, int commandArgumentsNumber) throws CommandException {
		if (argumentsLine < commandArgumentsNumber || argumentsLine > commandArgumentsNumber)
			throw new CommandException(CommandErrors.INCORRECT_NUM_ARGUMENTS);
	}

	// TODO THROWS HERE? maybe use db exception and just throw command here
	public void logIn(String username, String password) throws DBException, SQLException, CommandException {
		boolean incorrect = false;
		if (MarvelDAO.getInstance().isRegistered(username)) {
			User user = MarvelDAO.getInstance().getUserTOByKey(username);
			if (user.getPassword().equals(password))
				loggedInUser = user;
			else
				incorrect = true;
		} else {
			incorrect = true;
		}

		if (incorrect)
			throw new CommandException(CommandErrors.USER_OR_PSWRD_INCORRECT);
	}

	/**
	 * @return the loggedInUser
	 */
	public User getLoggedInUser() {
		return loggedInUser;
	}

	/*
	 * private checkDirectionsAvaliable() { loggedInUser.getPlace() }
	 */

}
