package com.acuevas.marvel.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Place {

	public enum directionsEnum {
		north, south, east, west;

		@Override
		public String toString() {
			return name().substring(0, 1);
		}
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
			getDirections().put(directionsEnum.east, east);
		if (directionAvaliable(west))
			getDirections().put(directionsEnum.west, west);

	}

	/**
	 * Constructor only used to compare/delete places from lists, do not use as a
	 * real one.
	 * 
	 * @param name
	 */
	public Place(String name) {
		this.name = name;
	}

	public String getDirection(directionsEnum directionEnum) {
		return directions.get(directionEnum);
	}

	public Set<directionsEnum> directionsAvaliable() {
		return getDirections().keySet();
	}

	private boolean directionAvaliable(String direction) {
		return (direction != null);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
