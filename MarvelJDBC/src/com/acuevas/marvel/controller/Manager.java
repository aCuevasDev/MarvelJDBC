package com.acuevas.marvel.controller;

import java.sql.SQLException;

import com.acuevas.marvel.exceptions.CommandException;
import com.acuevas.marvel.exceptions.CommandException.CommandErrors;
import com.acuevas.marvel.model.SuperHero;
import com.acuevas.marvel.model.User;
import com.acuevas.marvel.persistance.MarvelDAO;
import com.acuevas.marvel.util.Commands;
import com.acuevas.marvel.view.View;
import com.acuevas.marvel.view.View.ViewError;
import com.acuevas.marvel.view.View.ViewMessage;

public class Manager {
	// IMPORTANT NOTE: MySql-ConnectorJ Drivers are v.5.1.47, more updated versions
	// give problems.

	private User loggedInUser;

	public static void main(String[] args) {

		SuperHero hero;
		try {
			hero = MarvelDAO.getInstance().findHero("SuperJava");
			User user = new User("AlexTest", "AlexPass", hero);
			MarvelDAO.getInstance().insert(user);

		} catch (SQLException e) {
			View.printError(ViewError.CRITICAL_ERROR);
			View.printError(e.getMessage());
			View.printMessage(ViewMessage.GOODBYE);
		}

		/*
		 * GemTO gem = new GemTO("Mind Gem", "user1", "Loki", "Attilan"); try {
		 * MarvelDAO.getInstance().updateGem(gem); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		/*
		 * SuperHero hero; try { hero = MarvelDAO.getInstance().findHero("SuperJava");
		 * System.out.println(hero.getName() + " : " + hero.getSuperpower());
		 * 
		 * User user = MarvelDAO.getInstance().getUserTOByKey("user1");
		 * System.out.println(user.getUsername() + " : " + user.getPassword()); } catch
		 * (DBException | SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

//		Place place = new Place("test", "testDesc", "Asgard", "Midgard", null, null);
//		place.directionsAvaliable().forEach(direction -> System.out.println(direction));

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

	private void splitLine(String line) {
		String[] lineArray = line.split(" ");
	}

	private void readCommand(String[] line) throws CommandException {
		String letter = line[0];
		switch (letter.toUpperCase()) {
		case "L":
			commandArguments(line, Commands.LOGIN.getMaxArguments());
			break;
		case "R":
			break;

		case "X":
			View.printMessage(ViewMessage.GOODBYE);
			break;

		default:
			break;
		}
	}

	public void commandArguments(String[] line, int commandArgumentsNumber) throws CommandException {
		int argumentsLine = line.length;
		if (argumentsLine < commandArgumentsNumber || argumentsLine > commandArgumentsNumber)
			throw new CommandException(CommandErrors.INCORRECT_NUM_ARGUMENTS);
	}

	// TODO THROWS HERE? maybe use db exception and just throw command here
	public void logIn(String username, String password) throws SQLException, CommandException {
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
