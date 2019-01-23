package com.acuevas.marvel.model;

import java.util.HashMap;
import java.util.Map;

public class BattleHelper {

	/**
	 * Auxiliary class to save useful data about the battle but not relevant to the
	 * model itself.
	 * 
	 * @author Alex
	 *
	 */
	private class OwnerInBattle {
		// Making the fields public because they're encapsulated anyway by the private
		// class and I don't dirty the whole compilation unit by adding getters/setters
		// to an inner class.
		public int wins;
		public Owner owner;
		public boolean isEmpowered;

		public OwnerInBattle(Owner owner, boolean empowered) {
			this.owner = owner;
			isEmpowered = empowered;
		}

	}

	private OwnerInBattle villainInBattle;
	private OwnerInBattle userInBattle;
	private Map<Integer, BattleTurnResult> turnsPlayed = new HashMap<>();

	public BattleHelper(User user, Villain villain) {
		this.villainInBattle = new OwnerInBattle(villain, false);
		this.userInBattle = new OwnerInBattle(user, isVillainWeakened(user, villain));
		run();
	}

	/**
	 * This method checks if the Villain is weakened by the User's superpower.
	 * 
	 * @return true if it is weakened, false otherwise.
	 */
	private boolean isVillainWeakened(User user, Villain villain) {
		return user.getSuperhero().getSuperpower().equals(villain.getDebility()) ? true : false;
	}

	/**
	 * Calculates the "real level" of the user summing 1 if the villain is weakened
	 * by his superpower or 0 otherwise.
	 * 
	 * @return the level of the user calculated.
	 */
	private int getUserCalculatedLevel() {
		return (userInBattle.owner.level + ((userInBattle.isEmpowered) ? 1 : 0));
	}

	/**
	 * Returns who has the lowest level of this battle considering the weakness of
	 * the Villain, if they're equal returns User.
	 * 
	 * @param <T>
	 * 
	 * @return an instance of who has the lowest level.
	 */
	private OwnerInBattle whoHasLowestLevel() {
		return (villainInBattle.owner.level < getUserCalculatedLevel()) ? villainInBattle : userInBattle;
	}

	/**
	 * Returns who has the maximum level of this battle considering the weakness of
	 * the Villain, if they're equal returns <strong>null</strong>.
	 * 
	 * @return an instance of who has the maximum level or <strong>null</strong> if
	 *         they're equal.
	 */
	private OwnerInBattle whoHasMaxLevel() {
		if (villainInBattle.owner.level == userInBattle.owner.level)
			return null;

		if (villainInBattle.owner.level > getUserCalculatedLevel())
			return villainInBattle;
		else
			return userInBattle;
	}

	/**
	 * Returns the winner of this turn or null if it's even.
	 * 
	 * @param attackUser    ... The Attack of the User
	 * @param attackVillain ... The Attack of the Villain
	 * @return An instance of the winner of this turn or null if even.
	 */
	private OwnerInBattle getWinnerOfTurn(Attack attackUser, Attack attackVillain) {
		if (attackUser.compareTo(attackVillain) == 1)
			return userInBattle;
		else if (attackUser.compareTo(attackVillain) == -1)
			return villainInBattle;
		else
			return null;
	}

	/**
	 * Fights a turn getting the attacks of both Owner and setting the result of
	 * this turn in the Map turnsPlayed.
	 * 
	 * @param turn ... An int of the actual turn.
	 */
	private void fightTurn(int turn) {
		Attack attackUser = userInBattle.owner.attack();
		Attack attackVillain = villainInBattle.owner.attack();
		OwnerInBattle winner = getWinnerOfTurn(attackUser, attackVillain);
		winner.wins += 1;
		BattleTurnResult result = new BattleTurnResult(attackUser, attackVillain, winner.owner, turn);
		turnsPlayed.put(turn, result);
	}

	/**
	 * Main method of the Battle, controls the flow of it.
	 */
	public void run() {
		int turn = 0;

		// TODO TRY IT WITHOUT BRACKETS
		while (whoHasLowestLevel().owner.level >= turn++) {
			fightTurn(turn);
		}

		if (whoHasMaxLevel() != null) {
			while (whoHasMaxLevel().owner.level >= turn++) {
				if (getWinner() != whoHasMaxLevel()) {
					fightTurn(turn);
				}
			}
		}
	}

	public OwnerInBattle getWinner() {
		if (villainInBattle.wins == userInBattle.wins)
			return null;
		if (villainInBattle.wins > userInBattle.wins)
			return villainInBattle;
		else
			return userInBattle;
	}

	/*
	 * public Owner getBattleWinner() { long villainWins =
	 * turnsPlayed.values().stream().map(BattleTurnResult::getWinner).filter(winner
	 * -> winner.equals(villainInBattle.owner)).count(); long userWins =
	 * turnsPlayed.values().stream().map(BattleTurnResult::getWinner).filter(winner
	 * -> winner.equals(userInBattle.owner)).count();
	 * 
	 * return (villainWins >= userWins) ? }
	 */
}
