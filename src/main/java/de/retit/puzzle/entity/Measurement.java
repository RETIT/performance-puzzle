package de.retit.puzzle.entity;

import java.util.Date;

public class Measurement {

	private final String time;
	private final String value;

	public Measurement(String time, String value) {
		this.time = time;
		this.value = value;
	}

	public String getTime() {
		return time;
	}

	public String getValue() {
		return value;
	}
}