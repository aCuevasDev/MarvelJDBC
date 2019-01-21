package com.acuevas.marvel.model;

import java.util.List;
import java.util.Map;

public class Place {

	private enum directionsEnum {
		north, south, east, west;
	}

	private String name;
	private String description;

	private Map<directionsEnum, String> directions;

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
			directions.put(directionsEnum.north, north);
		if (directionAvaliable(south))
			directions.put(directionsEnum.south, south);
		if (directionAvaliable(east))
			directions.put(directionsEnum.east, south);
		if (directionAvaliable(west))
			directions.put(directionsEnum.west, west);

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

	public List<Character> directionsAvaliable() {
		if ()
		return null;
	}

	private boolean directionAvaliable(String direction) {
		if (direction != null)
			return true;
		return false;
	}
}
