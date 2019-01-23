package com.acuevas.marvel.model;

public class GemTO {

	private String name;
	private String user; // UserTO
	private String owner; // OwnerTO
	private String place; // PlaceTO

	/**
	 * @param name
	 * @param user
	 * @param owner
	 * @param place
	 */
	public GemTO(String name, String user, String owner, String place) {
		super();
		this.name = name;
		this.user = user;
		this.owner = owner;
		this.place = place;
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
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}
}
