package com.acuevas.marvel.view;

public class View {

	public enum ViewMessage {
		USER_REGISTERED("User registered.");

		private String message;

		private ViewMessage(String message) {
			this.message = message;
		}
	}
}
