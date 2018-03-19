package de.retit.puzzle.entity;

public class Measurement {

	private final Long time;
	private final Long value;

	public Measurement(Long time, Long value) {
		this.time = time;
		this.value = value;
	}

	public Long getTime() {
		return time;
	}

	public Long getValue() {
		return value;
	}
}