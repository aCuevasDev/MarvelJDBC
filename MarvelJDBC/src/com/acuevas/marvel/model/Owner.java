package com.acuevas.marvel.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.acuevas.marvel.exceptions.CommandException;
import com.acuevas.marvel.exceptions.CommandException.CommandErrors;
import com.acuevas.marvel.model.Attack.Type;
import com.acuevas.marvel.persistance.MarvelDAO;

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

	/**
	 * Updates the place of the gems in the DB
	 * 
	 * @throws SQLException
	 */
	public void updateGemsPlace() throws SQLException {
		for (GemTO gemTO : gems) {
			gemTO.setPlace(place.getName());
			MarvelDAO.getInstance().updateGem(gemTO);
		}
	}

	/**
	 * Picks up the gem
	 * 
	 * @param availableGems
	 * @param name
	 * @throws SQLException
	 * @throws CommandException
	 */
	public void pickUpGem(String name) throws SQLException, CommandException {
		// TODO MODEL CANNOT MAKE CALLS TO DAO
		List<GemTO> availableGems = MarvelDAO.getInstance().getGemsWithoutOwnerOn(place);
		GemTO newGem = new GemTO(name);
		if (availableGems.contains(newGem)) {
			int index = availableGems.indexOf(newGem);
			gems.add(availableGems.get(index));
		} else
			throw new CommandException(CommandErrors.WRONG_GEM);

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
