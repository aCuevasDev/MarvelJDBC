package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.Attack;
import model.Attack.Type;

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
	}
}
