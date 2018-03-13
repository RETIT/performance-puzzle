package de.retit.puzzle.entity;

import java.util.Date;

public class Measurement {

	private final Date time;
	private final Double value;

	public Measurement(Date time, Double value) {
		this.time = time;
		this.value = value;
	}

	public Date getTime() {
		return time;
	}

	public Double getValue() {
		return value;
	}
}