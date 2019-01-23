package com.acuevas.marvel.model;

import com.acuevas.marvel.exceptions.CommandException;
import com.acuevas.marvel.exceptions.CommandException.CommandErrors;
import com.acuevas.marvel.model.Place.directionsEnum;
import com.acuevas.marvel.persistance.MarvelDAO;

public class User extends Owner {

	private String username;
	private String password;
	private SuperHero superhero;
	private int points;

	/**
	 * TO Constructor
	 * 
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

//	public User(String username, String password, SuperHero superhero) {
//		this.username = username;
//		this.password = password;
//		this.superhero = superhero;
//	}

	public User(String username, String password, SuperHero hero, Place place) {
		this.username = username;
		this.password = password;
		this.superhero = hero;
		this.place = place;
	}

	public void move(String direction) throws CommandException {
		Place newPlace;
		switch (direction.substring(0, 1).toUpperCase()) {
		case "N":
			newPlace = MarvelDAO.getInstance().getPlaceByKey(place.getDirection(directionsEnum.north));
			break;
		case "S":
			newPlace = MarvelDAO.getInstance().getPlaceByKey(place.getDirection(directionsEnum.south));
			break;
		case "E":
			newPlace = MarvelDAO.getInstance().getPlaceByKey(place.getDirection(directionsEnum.east));
			break;
		case "W":
			newPlace = MarvelDAO.getInstance().getPlaceByKey(place.getDirection(directionsEnum.west));
			break;

		default:
			throw new CommandException(CommandErrors.WRONG_COMMAND);
		}

		place = newPlace;
	}

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

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}
}
