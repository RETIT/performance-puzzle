package de.retit.puzzle.components;

import de.retit.puzzle.entity.Measurement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultAggregator extends Thread {
	public static final Pattern PATTERN = Pattern.compile("([0-9]*),([0-9]*),([a-zA-Z]*)");

	private List<String> csv;
	private static final Map<String, List<Measurement>> result = Collections.synchronizedMap(new HashMap<>());

	public ResultAggregator(List<String> csv) {
		this.csv = csv;
	}

	public static Map<String, List<Measurement>> getResult() {
		return result;
	}

	public void run() {
		for (String line : csv) {
			Matcher matcher = PATTERN.matcher(line);
			if (matcher.matches()) {
				// Match line to pattern to get required fields
				String timestamp = matcher.group(1);
				String time = matcher.group(2);
				String transaction = matcher.group(3);

				// Build Measurement
				Measurement measurement = new Measurement(timestamp, time);

				// Add to map
				synchronized (result) {
					List<Measurement> measurementList = new ArrayList<>(1);
					if (result.containsKey(transaction)) {
						measurementList = result.get(transaction);
					}
					measurementList.add(measurement);
					result.put(transaction, measurementList);
				}
			}
		}
	}
}
