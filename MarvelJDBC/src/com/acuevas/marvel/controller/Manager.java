package com.acuevas.marvel.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.acuevas.marvel.exceptions.CommandException;
import com.acuevas.marvel.exceptions.CommandException.CommandErrors;
import com.acuevas.marvel.model.GemTO;
import com.acuevas.marvel.model.Place;
import com.acuevas.marvel.model.SuperHero;
import com.acuevas.marvel.model.User;
import com.acuevas.marvel.persistance.MarvelDAO;
import com.acuevas.marvel.util.Commands;
import com.acuevas.marvel.view.View;
import com.acuevas.marvel.view.View.ViewMessage;

public class Manager {
	// IMPORTANT NOTE: MySql-ConnectorJ Drivers are v.5.1.47, more updated versions
	// give problems.

	private static User loggedInUser;

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		View.printMessage(ViewMessage.HELLO);
		while (!line.toUpperCase().subSequence(0, 0).equals("X")) {
			try {
				View.printMessage(ViewMessage.INSERT_COMMAND);
				line = br.readLine();
				readCommand(line.split(" "));
			} catch (IOException e) {
				View.printError(e.getMessage());
			} catch (SQLException e) {
				View.printError(e.getMessage());
			}
		}

		// TODO AN ENEMY MUST PICKUP GEMS IN A PLACE WITHOUT OWNER
		/*
		 * SuperHero hero; try { hero = MarvelDAO.getInstance().findHero("SuperJava");
		 * Place place = MarvelDAO.getInstance().getPlaceByKey("New York"); User user =
		 * new User("AlexTest", "AlexPass", hero, place);
		 * MarvelDAO.getInstance().insert(user);
		 * 
		 * } catch (SQLException e) { View.printError(ViewError.CRITICAL_ERROR);
		 * View.printError(e.getMessage()); View.printMessage(ViewMessage.GOODBYE); }
		 */

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

	/**
	 * Reads a command and executes the pertinent action.
	 * 
	 * @param line
	 * @throws CommandException
	 * @throws SQLException
	 */
	private static void readCommand(String[] line) throws SQLException {
		try {
			String letter = line[0];
			switch (letter.toUpperCase()) {
			case "L":
				commandArguments(line, Commands.LOGIN.getMaxArguments());
				logIn(line[1], line[2]);
				View.printMessage(View.ViewMessage.WELCOME, loggedInUser);
				View.printPlace(loggedInUser.getPlace());
				break;
			case "R":
				commandArguments(line, Commands.REGISTER.getMaxArguments());
				registerUser(line[1], line[2], line[3]);
				break;
			case "N":
			case "S":
			case "E":
			case "W":
				if (loggedInUser != null) {
					loggedInUser.move(letter);
					showAvailableGems();
					showDirections();
				} else
					throw new CommandException(CommandErrors.NOT_LOGGED_IN);
				break;
			case "G":
				break;

			case "V":
				commandArguments(line, Commands.VIEW_HEROS.getMaxArguments());
				viewHeros();
				break;

			case "X":
				View.printMessage(ViewMessage.GOODBYE);
				break;

			default:
				throw new CommandException(CommandErrors.WRONG_COMMAND);
			}
		} catch (CommandException e) {
			View.printError(e.getMessage());
		}

	}

	/**
	 * Shows the available gems in the place of the user
	 * 
	 * @throws SQLException
	 */
	public static void showAvailableGems() throws SQLException {
		List<GemTO> gems;
		gems = MarvelDAO.getInstance().getGemsWithoutOwnerOn(loggedInUser.getPlace());
		View.printMessage(ViewMessage.AVAILABLE_GEMS);
		gems.forEach(gem -> View.printMessage(gem.toString()));
	}

	/**
	 * Shows the directions available in the new place.
	 */
	private static void showDirections() {
		View.printMessage(ViewMessage.AVAILABLE_PLACES);
		loggedInUser.getPlace().directionsAvaliable().forEach(direction -> {
			View.printMessage(direction.toString());
		});
	}

	/**
	 * Shows all the heroes from the DB
	 * 
	 * @throws SQLException
	 */
	private static void viewHeros() throws SQLException {
		MarvelDAO.getInstance().findAllHeroes().forEach(hero -> {
			View.printMessage(hero.toString());
		});
	}

	/**
	 * Evaluates if a command has a wrong number of arguments
	 * 
	 * @param line                   the command
	 * @param commandArgumentsNumber number of arguments expected
	 * @throws CommandException
	 */
	public static void commandArguments(String[] line, int commandArgumentsNumber) throws CommandException {
		int argumentsLine = line.length;
		if (argumentsLine != commandArgumentsNumber)
			throw new CommandException(CommandErrors.INCORRECT_NUM_ARGUMENTS);
	}

	/**
	 * Logs in the user
	 * 
	 * @param username
	 * @param password
	 * @throws SQLException
	 * @throws CommandException
	 */
	public static void logIn(String username, String password) throws SQLException, CommandException {
		boolean incorrect = false;
		if (MarvelDAO.getInstance().isRegistered(username)) {
			User user = MarvelDAO.getInstance().getUserByKey(username);
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
	 * Registers the user
	 * 
	 * @param username
	 * @param password
	 * @param superhero
	 * @throws SQLException
	 */
	public static void registerUser(String username, String password, String superhero) throws SQLException {
		SuperHero hero = MarvelDAO.getInstance().findHero(superhero);
		Place place = MarvelDAO.getInstance().getPlaceByKey("New York");
		User newUser = new User(username, password, hero, place);
		MarvelDAO.getInstance().insert(newUser);
	}

	/**
	 * @return the loggedInUser
	 */
	public User getLoggedInUser() {
		return loggedInUser;
	}

}
