package com.acuevas.marvel.model;

public class Battle {

	private Villain villain;
	private User user;

	public Battle(User user, Villain villain) {
		this.villain = villain;
		this.user = user;
	}

	public void run() {
		for (int i = 0; i < villain.level; i++) {
			villain.getStoredAttacks().add(villain.attack());
		}

		for (int i = 0; i < user.level; i++) {
			user.getStoredAttacks().add(user.attack());
		}

		villain.clearStoredAttacks();
		user.clearStoredAttacks();
	}

	/**
	 * @return the villain
	 */
	public Villain getVillain() {
		return villain;
	}

	/**
	 * @param villain the villain to set
	 */
	public void setVillain(Villain villain) {
		this.villain = villain;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
