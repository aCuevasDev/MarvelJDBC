package com.acuevas.marvel.controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.acuevas.marvel.exceptions.DBException;
import com.acuevas.marvel.model.Attack;
import com.acuevas.marvel.model.Attack.Type;
import com.acuevas.marvel.model.Hero;
import com.acuevas.marvel.persistance.MarvelDAO;

public class Manager {

	public static void main(String[] args) {
		List<Type> types = Arrays.asList(Type.values());
		int random = new Random().nextInt(types.size());
		int random2 = new Random().nextInt(types.size());

		Attack attack1 = new Attack(types.get(random));
		Attack attack2 = new Attack(types.get(random2));
		System.out.println(attack1.getType());
		System.out.println(attack2.getType());
		System.out.println(attack1.compareTo(attack2));

		try {
			Hero hero = MarvelDAO.getInstance().findHero("potato");
			System.out.println(hero.getName());
			System.out.println(hero.getSuperpower());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
