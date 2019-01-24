package com.acuevas.marvel.view;

import java.util.List;

import com.acuevas.marvel.model.Place;
import com.acuevas.marvel.model.SuperHero;
import com.acuevas.marvel.model.User;
import com.acuevas.marvel.model.Villain;

public class View {

	public enum ViewMessage {
		USER_REGISTERED("User registered."), GOODBYE("Goodbye!"), AVAILABLE_GEMS("Showing available gems:"),
		AVAILABLE_PLACES("You can go:"), INSERT_COMMAND("Please insert a command."), HELLO("Hello!"),
		WELCOME("Welcome,"), PLACE("Place:"), NO_GEMS("There're no gems here."), NOBODY("There's nobody here."),
		SEPARATOR(" | "), MOVING("Moving to..."), NO_AVAILABLE_GEMS("There isn't any gem here."),
		GEM_PICKED("Gem picked up. ");

		private String message;

		private ViewMessage(String message) {
			this.message = message;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return message;
		}

	}

	public enum ViewError {
		CRITICAL_ERROR("A critical error ocurred, program is going to close.");

		private String message;

		private ViewError(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return super.toString();
		}

	}

	public static void printError(String error) {
		System.err.println(error);
	}

	public static void printError(ViewError error) {
		System.err.println(error);
	}

	public static void printMessage(String message) {
		System.out.println(message);
	}

	public static void printMessageInLine(String message) {
		System.out.print(message);
	}

	public static void printMessage(ViewMessage message) {
		System.out.println(message);
	}

	public static void printPlace(Place place) {
		System.out.println(ViewMessage.PLACE + place.getName());
		System.out.println(place.getDescription());
		printMinus();
	}

	public static void printHeroes(List<SuperHero> heroes) {
		heroes.forEach(hero -> printMessage(hero.getName() + " " + hero.getSuperpower() + " "));
	}

	public static void printVillains(List<Villain> villains) {
		System.out.println("Showing villains: ");
		villains.forEach(villan -> printMessage(
				"name: " + villan.getName() + "- debility: " + villan.getDebility() + "- level " + villan.getLevel()));
	}

	public static void printUsername(ViewMessage message, User user) {
		System.out.println(message + " " + user.getUsername());
	}

	public static void printMinus() {
		System.out.println("---");
	}

	public static void nextLine() {
		System.out.println();
	}
}
