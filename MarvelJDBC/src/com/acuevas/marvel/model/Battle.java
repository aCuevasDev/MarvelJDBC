package com.acuevas.marvel.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Battle {

	/**
	 * Auxiliary class to save useful data about the battle but not relevant to the
	 * model itself.
	 * 
	 * @author Alex
	 * @param <T> The owner which is going to delegate on.
	 *
	 */
	private class OwnerInBattle<T> {
		// Making the fields public because they're encapsulated anyway by the private
		// class and I don't dirty the whole compilation unit by adding getters/setters
		// to an inner class.
		public int wins;
		public List<Attack> storedAttacks = new ArrayList<>();
		public T owner;

		public OwnerInBattle(T owner) {
			this.owner = owner;
		}
	}

	private OwnerInBattle<Villain> villainInBattle;
	private OwnerInBattle<User> userInBattle;
	private Map<Integer, BattleTurnResult> turnsPlayed = new HashMap<>();

	public Battle(User user, Villain villain) {
		this.villainInBattle = new OwnerInBattle<>(villain);
		this.userInBattle = new OwnerInBattle<>(user);
	}

	/**
	 * This method checks if the Villain is weakened by the User's superpower.
	 * 
	 * @return true if it is weakened, false otherwise.
	 */
	private boolean isVillainWeakened() {
		return userInBattle.owner.getSuperhero().getSuperpower().equals(villainInBattle.owner.getDebility()) ? true
				: false;
	}

	/**
	 * Calculates the "real level" of the user summing 1 if the villain is weakened
	 * by his superpower or 0 otherwise.
	 * 
	 * @return the level of the user calculated.
	 */
	private int getUserCalculatedLevel() {
		return (userInBattle.owner.level + ((isVillainWeakened()) ? 1 : 0));
	}

	/**
	 * Prepares the attacks for each participant of the battle by adding them to
	 * their storedAttacks List.
	 */
	private void setAttacks() {
		for (int i = 0; i < villainInBattle.owner.level; i++) {
			villainInBattle.storedAttacks.add(villainInBattle.owner.attack());
		}

		for (int i = 0; i < getUserCalculatedLevel(); i++) {
			userInBattle.storedAttacks.add(userInBattle.owner.attack());
		}
	}

	/**
	 * Returns who has the lowest level of this battle considering the weakness of
	 * the Villain, if they're equal returns User.
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * 
	 * @return an instance of who has the lowest level.
	 */
	private Owner whoHasLowestLevel() {
		return (villainInBattle.owner.level < getUserCalculatedLevel()) ? villainInBattle.owner : userInBattle.owner;
	}

	/**
	 * Returns who has the maximum level of this battle considering the weakness of
	 * the Villain, if they're equal returns <strong>null</strong>.
	 * 
	 * @return an instance of who has the maximum level or <strong>null</strong> if
	 *         they're equal.
	 */
	private Owner whoHasMaxLevel() {
		if (villainInBattle.owner.level == userInBattle.owner.level)
			return null;

		if (villainInBattle.owner.level > getUserCalculatedLevel())
			return villainInBattle.owner;
		else
			return userInBattle.owner;
	}

	/**
	 * Returns the winner of this turn or null if it's even.
	 * 
	 * @param               <T>
	 * 
	 * @param attackUser    ... The Attack of the User
	 * @param attackVillain ... The Attack of the Villain
	 * @return An instance of the winner of this turn or null if even.
	 */
	private Owner getWinnerOfTurn(Attack attackUser, Attack attackVillain) {
		if (attackUser.compareTo(attackVillain) == 1)
			return userInBattle.owner;
		else if (attackUser.compareTo(attackVillain) == -1)
			return villainInBattle.owner;
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
		Attack attackUser = userInBattle.storedAttacks.get(turn);
		Attack attackVillain = villainInBattle.storedAttacks.get(turn);
		Owner winner = getWinnerOfTurn(attackUser, attackVillain);
		BattleTurnResult result = new BattleTurnResult(attackUser, attackVillain, winner, turn);
		turnsPlayed.put(turn, result);
	}

	/**
	 * Main method of the Battle, controls the flow of it.
	 */
	public void run() {
		int turn = 0;
		setAttacks();

		while (whoHasLowestLevel().level < turn++) {
			fightTurn(turn);
		}
	}
}
