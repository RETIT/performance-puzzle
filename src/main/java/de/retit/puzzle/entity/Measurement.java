package de.retit.puzzle.entity;

public class Measurement {

	private final long time;
	private final long value;

	public Measurement(long time, long value) {
		this.time = time;
		this.value = value;
	}

	public long getTime() {
		return time;
	}

	public long getValue() {
		return value;
	}
}