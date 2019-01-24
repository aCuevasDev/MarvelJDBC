package com.acuevas.marvel.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.acuevas.marvel.model.Attack.Type;

public abstract class Owner {

	protected int level = 1;
	protected Place place;
	protected List<GemTO> gems = new ArrayList<>();

	/**
	 * Generates a random Attack
	 * 
	 * @return Attack
	 */
	public Attack attack() {
		List<Type> types = Arrays.asList(Type.values());
		int random = new Random().nextInt(types.size());
		Attack attack = new Attack(types.get(random));
		return attack;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Owner [level=" + level + ", place=" + place + ", gems=" + gems + "]";
	}

	/**
	 * @return the place
	 */
	public Place getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(Place place) {
		this.place = place;
	}

	/**
	 * @return the gems
	 */
	public List<GemTO> getGems() {
		return gems;
	}

	/**
	 * @param gems the gems to set
	 */
	public void setGems(List<GemTO> gems) {
		this.gems = gems;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
