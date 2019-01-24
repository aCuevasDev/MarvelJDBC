package com.acuevas.marvel.model;

public class Villain extends Owner {

	private String name;
	private String debility;

	/**
	 * @param name
	 * @param debility
	 */
	public Villain(String name, String debility, int level, Place place) {
		this.name = name;
		this.debility = debility;
		this.level = level;
		this.place = place;
	}

	/**
	 * 
	 */
	public void move() {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the debility
	 */
	public String getDebility() {
		return debility;
	}

	/**
	 * @param debility the debility to set
	 */
	public void setDebility(String debility) {
		this.debility = debility;
	}

}
