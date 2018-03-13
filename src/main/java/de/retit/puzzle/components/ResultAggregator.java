package de.retit.puzzle.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.retit.puzzle.entity.Measurement;

public class ResultAggregator extends Thread {

	private static final String LINE_REGEX = "([0-9]*),([0-9]*),([a-zA-Z]*)";

	private List<String> csv;
	private static Map<String, List<Measurement>> result = Collections.synchronizedMap(new HashMap<>());

	public ResultAggregator(List<String> csv) {
		this.csv = csv;
	}

	public static Map<String, List<Measurement>> getResult() {
		return result;
	}

	public void run() {
		synchronized (result) {
			for (String line : csv) {
				if (line.matches(LINE_REGEX)) {
					// Match line to pattern to get required fields
					Matcher matcher = Pattern.compile(LINE_REGEX).matcher(line);
					matcher.matches();
					String timestamp = matcher.group(1);
					String time = matcher.group(2);
					String transaction = matcher.group(3);

					// Build Measurement
					Date date = new Date(Long.parseLong(timestamp));
					Double timeDouble = Double.valueOf(time);
					Measurement measurement = new Measurement(date, timeDouble);

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
}
