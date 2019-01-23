package com.acuevas.marvel.model;

import java.util.HashSet;
import java.util.Set;

public class Attack implements Comparable<Attack> {

	public enum Type {
		ROCK("Lizard", "Paper"), SCISSORS("Lizard", "Paper"), PAPER("Rock", "Spock"), LIZARD("Spock", "Paper"),
		SPOCK("Scissors", "Rock");

		private Set<String> wins = new HashSet<>();

		/**
		 * 
		 * @param win  One Type your Type wins
		 * @param win2 the other one
		 */
		Type(String win, String win2) {
			wins.add(win.toUpperCase());
			wins.add(win2.toUpperCase());
		}

		public Set<String> getWins() {
			return wins;
		}

	}

	private Type type;

	public Attack(Type type) {
		this.type = type;
	}

	@Override
	public int compareTo(Attack attack) {
		if (this.type.wins.contains(attack.type.toString())) // toString redundant but IDE gives warnings if not used.
			return 1;
		else if (attack.type.wins.contains(this.type.toString()))
			return -1;
		else
			return 0;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

}
