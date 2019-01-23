package com.acuevas.marvel.model;

public class BattleTurnResult {

	private Attack attackUser;
	private Attack attackVillain;
	private Owner winner;
	private int turnNum;

	/**
	 * @param attackUser
	 * @param attackVillain
	 * @param winner
	 * @param turnNum
	 */
	public BattleTurnResult(Attack attackUser, Attack attackVillain, Owner winner, int turnNum) {
		this.attackUser = attackUser;
		this.attackVillain = attackVillain;
		this.winner = winner;
		this.turnNum = turnNum;
	}

	/**
	 * @return the attackUser
	 */
	public Attack getAttackUser() {
		return attackUser;
	}

	/**
	 * @param attackUser the attackUser to set
	 */
	public void setAttackUser(Attack attackUser) {
		this.attackUser = attackUser;
	}

	/**
	 * @return the attackVillain
	 */
	public Attack getAttackVillain() {
		return attackVillain;
	}

	/**
	 * @param attackVillain the attackVillain to set
	 */
	public void setAttackVillain(Attack attackVillain) {
		this.attackVillain = attackVillain;
	}

	/**
	 * @return the winner
	 */
	public Owner getWinner() {
		return winner;
	}

	/**
	 * @param winner the winner to set
	 */
	public void setWinner(Owner winner) {
		this.winner = winner;
	}

	/**
	 * @return the turnNum
	 */
	public int getTurnNum() {
		return turnNum;
	}

	/**
	 * @param turnNum the turnNum to set
	 */
	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}

}
