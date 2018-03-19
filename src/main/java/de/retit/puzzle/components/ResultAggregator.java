package de.retit.puzzle.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.retit.puzzle.entity.Measurement;

public class ResultAggregator extends Thread {

	private static final String LINE_REGEX = "([0-9]*),([0-9]*),([a-zA-Z]*)";

	private List<String> csv;
	private Map<String, List<Measurement>> result = new HashMap<>();

	public ResultAggregator(List<String> csv) {
		this.csv = csv;
	}

	public void run() {
		Pattern p = Pattern.compile(LINE_REGEX);

		for (String line : csv) {
			String[] parts = line.split(",");

			if(parts.length == 3) {
				String timestamp = parts[0];
				String time = parts[1];
				String transaction = parts[2];

				// Build Measurement
				Date date = new Date(Long.parseLong(timestamp));
				Double timeDouble = Double.valueOf(time);
				Measurement measurement = new Measurement(date, timeDouble);

				// Add to map
				List<Measurement> measurementList = result.get(transaction);
				if (measurementList == null) {
					measurementList = new ArrayList<>(1);
					result.put(transaction, measurementList);
				}
				measurementList.add(measurement);
			}
		}
	}

	public void aggregate(Map<String, List<Measurement>> aggregatedResult) {
		for (Entry<String, List<Measurement>> entry : this.result.entrySet()) {
			String transaction = entry.getKey();
			List<Measurement> measurementList = entry.getValue();

			List<Measurement> aggregatedList = aggregatedResult.get(transaction);
			if (aggregatedList != null) {
				aggregatedList.addAll(measurementList);
			}
			else {
				aggregatedResult.put(transaction, measurementList);
			}
		}
	}
}
