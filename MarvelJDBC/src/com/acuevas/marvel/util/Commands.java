package com.acuevas.marvel.util;

public enum Commands {

	LOGIN(4), REGISTER(4), VIEW_HEROS(1);

	private int maxArguments;

	private Commands(int maxArguments) {
		this.maxArguments = maxArguments;
	}

	/**
	 * @return the maxArguments
	 */
	public int getMaxArguments() {
		return maxArguments;
	}

	/**
	 * @param maxArguments the maxArguments to set
	 */
	public void setMaxArguments(int maxArguments) {
		this.maxArguments = maxArguments;
	}
}
