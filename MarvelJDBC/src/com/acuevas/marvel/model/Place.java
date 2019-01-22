package com.acuevas.marvel.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Place {

	public enum directionsEnum {
		north, south, east, west;
	}

	private String name;
	private String description;

	private Map<directionsEnum, String> directions = new LinkedHashMap<>();

	/**
	 * @param name
	 * @param description
	 * @param north
	 * @param south
	 * @param east
	 */
	public Place(String name, String description, String north, String south, String east, String west) {
		super();
		this.name = name;
		this.description = description;

		if (directionAvaliable(north))
			getDirections().put(directionsEnum.north, north);
		if (directionAvaliable(south))
			getDirections().put(directionsEnum.south, south);
		if (directionAvaliable(east))
			getDirections().put(directionsEnum.east, south);
		if (directionAvaliable(west))
			getDirections().put(directionsEnum.west, west);

	}

	public String getDirection(directionsEnum directionEnum) {
		return directions.get(directionEnum);
	}

	public Set<directionsEnum> directionsAvaliable() {
		return getDirections().keySet();
	}

	private boolean directionAvaliable(String direction) {
		if (direction != null)
			return true;
		return false;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the directions
	 */
	public Map<directionsEnum, String> getDirections() {
		return directions;
	}

	/**
	 * @param directions the directions to set
	 */
	public void setDirections(Map<directionsEnum, String> directions) {
		this.directions = directions;
	}

}
