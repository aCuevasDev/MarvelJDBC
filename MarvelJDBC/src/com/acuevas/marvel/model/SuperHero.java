package com.acuevas.marvel.model;

public class SuperHero {

	private String name;
	private String superpower;

	public SuperHero(String name, String superpower) {
		this.name = name;
		this.superpower = superpower;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SuperHero [name=" + name + ", superpower=" + superpower + "]";
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the superpower
	 */
	public String getSuperpower() {
		return superpower;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param superpower the superpower to set
	 */
	public void setSuperpower(String superpower) {
		this.superpower = superpower;
	}
}
