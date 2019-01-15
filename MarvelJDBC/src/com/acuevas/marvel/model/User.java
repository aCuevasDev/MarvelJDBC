package com.acuevas.marvel.model;

public class User extends Owner {

	private String username;
	private String password;
	private SuperHero superhero;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the superhero
	 */
	public SuperHero getSuperhero() {
		return superhero;
	}

	/**
	 * @param superhero the superhero to set
	 */
	public void setSuperhero(SuperHero superhero) {
		this.superhero = superhero;
	}

}
