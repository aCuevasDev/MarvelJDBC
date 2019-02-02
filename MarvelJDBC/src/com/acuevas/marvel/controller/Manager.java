package com.acuevas.marvel.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.acuevas.marvel.exceptions.CommandException;
import com.acuevas.marvel.exceptions.CommandException.CommandErrors;
import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.model.BattleHelper;
import com.acuevas.marvel.model.BattleTurnResult;
import com.acuevas.marvel.model.GemTO;
import com.acuevas.marvel.model.Owner;
import com.acuevas.marvel.model.Place;
import com.acuevas.marvel.model.SuperHero;
import com.acuevas.marvel.model.User;
import com.acuevas.marvel.model.Villain;
import com.acuevas.marvel.persistance.MarvelDAO;
import com.acuevas.marvel.util.Commands;
import com.acuevas.marvel.util.Img;
import com.acuevas.marvel.view.View;
import com.acuevas.marvel.view.View.ViewMessage;

public class Manager {
	// IMPORTANT NOTE: MySql-ConnectorJ Drivers are v.5.1.47, more updated versions
	// give problems.
	private static List<String> gemNames = Arrays.asList("Mind Gem", "Power Gem", "Reality Gem", "Soul Gem",
			"Space Gem", "Time Gem");
	private static User loggedInUser;

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		View.printMessage(ViewMessage.HELLO);
		Img.run();
		do {
			try {
				View.printMessage(ViewMessage.INSERT_COMMAND);
				line = br.readLine();
				readCommand(line.trim().split(" "));
			} catch (IOException e) {
				View.printError(e.getMessage());
			} catch (SQLException e) {
				View.printError(e.getMessage());
				System.exit(1);
			}
		} while (!(line.substring(0, 1).equalsIgnoreCase("X")));

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
				logIn(line);
				break;
			case "R":
				commandArguments(line, Commands.REGISTER.getMaxArguments());
				registerUser(line[1], line[2], line[3]);
				break;
			case "N":
			case "S":
			case "E":
			case "W":
				move(letter);
				break;
			case "G":
				pickUpGem(line);
				break;
			case "B":
				battle(line);
				break;
			case "V":
				commandArguments(line, Commands.VIEW_HEROS.getMaxArguments());
				viewAllHeroes();
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

	private static void battle(String[] line) throws CommandException, SQLException {
		commandArguments(line, Commands.BATTLE.getMaxArguments());
		Villain villain = MarvelDAO.getInstance().getVillainByKey(line[1]);
		BattleHelper battleHelper = new BattleHelper(loggedInUser, villain);
		Map<Integer, BattleTurnResult> turns = battleHelper.getTurnsPlayed();
		for (Integer turn : turns.keySet()) {
			System.out.println("You Attack: " + turns.get(turn).getAttackUser());
			System.out.println("Villain Attack: " + turns.get(turn).getAttackVillain());
			System.out.println("Winner: " + turns.get(turn).getWinner());
			System.out.println("-------------------------------------------------");
		}

	}

	private static void pickUpGem(String[] line) throws CommandException, SQLException {
		commandArguments(line, Commands.PICKUP.getMaxArguments());
		String gem = line[1] + " " + line[2];
		pickUpGem(gem);
	}

	public static void logIn(String[] line) throws CommandException, SQLException {
		commandArguments(line, Commands.LOGIN.getMaxArguments());
		logIn(line[1], line[2]);
		View.printUsername(View.ViewMessage.WELCOME, loggedInUser);
		View.printPlace(loggedInUser.getPlace());
		showVillains();
		showAvailableGems();
		showDirections();
	}

	/**
	 * Moves a user to the letter indicated
	 * 
	 * @param letter
	 * @throws SQLException
	 * @throws CommandException
	 */
	public static void move(String letter) throws SQLException, CommandException {
		if (loggedInUser != null) {
			loggedInUser.move(letter);
			updateGemsPlace(loggedInUser);
			MarvelDAO.getInstance().updateUserPlace(loggedInUser);
			View.printMessage(ViewMessage.MOVING + letter);
			View.printPlace(loggedInUser.getPlace());
			showVillains();
			showAvailableGems();
			showDirections();
		} else
			throw new CommandException(CommandErrors.NOT_LOGGED_IN);
	}

	/**
	 * Shows all the villains from a place.
	 * 
	 * @throws SQLException
	 */
	private static void showVillains() throws SQLException {
		List<Villain> villains = MarvelDAO.getInstance().getAllVillainFromAPlace(loggedInUser.getPlace().getName());
		View.printVillains(villains);
	}

	/**
	 * Updates the place of the gems in the DB
	 * 
	 * @throws SQLException
	 */
	public static void updateGemsPlace(Owner owner) throws SQLException {
		for (GemTO gemTO : owner.getGems()) {
			gemTO.setPlace(owner.getPlace().getName());
			MarvelDAO.getInstance().updateGem(gemTO);
		}
	}

	/**
	 * Shows the available gems in the place of the user
	 * 
	 * @throws SQLException
	 */
	public static void showAvailableGems() throws SQLException {
		List<GemTO> gems;
		gems = MarvelDAO.getInstance().getGemsWithoutOwnerOn(loggedInUser.getPlace(), loggedInUser);
		View.printMessage(ViewMessage.AVAILABLE_GEMS);
		gems.forEach(gem -> View.printMessage(gem.toString()));
		if (gems.size() == 0)
			View.printMessage(ViewMessage.NO_AVAILABLE_GEMS);
	}

	/**
	 * Shows the directions available in the new place.
	 */
	private static void showDirections() {
		View.printMessage(ViewMessage.AVAILABLE_PLACES);
		loggedInUser.getPlace().directionsAvaliable().forEach(direction -> {
			View.printMessageInLine(View.ViewMessage.SEPARATOR + direction.toString() + View.ViewMessage.SEPARATOR);
		});
		View.nextLine();
	}

	/**
	 * Picks up the gem
	 * 
	 * @param availableGems
	 * @param name
	 * @throws SQLException
	 * @throws CommandException
	 */
	public static void pickUpGem(String name) throws SQLException, CommandException {
		if (loggedInUser == null)
			throw new CommandException(CommandErrors.NOT_LOGGED_IN);

		List<GemTO> availableGems = MarvelDAO.getInstance().getGemsWithoutOwnerOn(loggedInUser.getPlace(),
				loggedInUser);
		GemTO newGem = new GemTO(name);
		if (availableGems.contains(newGem)) {
			int index = availableGems.indexOf(newGem);
			loggedInUser.getGems().add(availableGems.get(index));
			View.printMessage(ViewMessage.GEM_PICKED);
		} else
			throw new CommandException(CommandErrors.WRONG_GEM);
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
		final int maxGems = 6;
		try {
			SuperHero hero = MarvelDAO.getInstance().findHero(superhero);
			if (hero == null)
				return;
			Place place = MarvelDAO.getInstance().getPlaceByKey("New York");
			User newUser = new User(username, password, hero, place);
			MarvelDAO.getInstance().insert(newUser);
			List<Place> places = MarvelDAO.getInstance().getRandomPlace(maxGems);
			Place newYork = new Place("New York");
			while (places.contains(newYork)) {
				places.remove(newYork);
				places.add(MarvelDAO.getInstance().getRandomPlace(1).get(0));
			}
			for (int i = 0; i < maxGems; i++) {
				GemTO gem = new GemTO(gemNames.get(i), newUser.getUsername(), null, places.get(i).getName());
				MarvelDAO.getInstance().insertGem(gem);
			}
			List<GemTO> gemsAlone = MarvelDAO.getInstance().getAloneGemsOnVilliansPlace(newUser);
			for (GemTO gem : gemsAlone) {
				Villain villain = MarvelDAO.getInstance().getRandomVillainFromAPlace(gem.getPlace());
				gem.setOwner(villain.getName());
				MarvelDAO.getInstance().updateGem(gem);
			}

			View.printMessage(ViewMessage.USER_REGISTERED);
		} catch (DBException e) {
			View.printError(e.getMessage());
		}
	}

	/**
	 * Shows all the heroes from the DB
	 * 
	 * @throws SQLException
	 */
	public static void viewAllHeroes() throws SQLException {
		List<SuperHero> heroes = MarvelDAO.getInstance().findAllHeroes();
		View.printHeroes(heroes);
	}

	/**
	 * @return the loggedInUser
	 */
	public User getLoggedInUser() {
		return loggedInUser;
	}

}
