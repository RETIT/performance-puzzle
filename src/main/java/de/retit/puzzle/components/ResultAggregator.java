package de.retit.puzzle.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.retit.puzzle.entity.Measurement;

public class ResultAggregator {

	private static final String LINE_REGEX = "([0-9]*),([0-9]*),([a-zA-Z]*)";

	private List<String> csv;
	private Map<String, List<Measurement>> result = new HashMap<>();

	public ResultAggregator(List<String> csv) {
		this.csv = csv;
	}

	public Map<String, List<Measurement>> getResult() {
		return result;
	}

	public void aggregate() {
		for (String line : csv) {
			String[] array = line.split(",");
			String timestamp = array[0];
			String time = array[1];
			String transaction = array[2];

			// Build Measurement
			Date date = new Date(Long.parseLong(timestamp));
			Double timeDouble = Double.valueOf(time);
			Measurement measurement = new Measurement(date, timeDouble);

			// Add to map
			List<Measurement> measurementList = new ArrayList<>();
			if (result.containsKey(transaction)) {
				measurementList = result.get(transaction);
			}
			measurementList.add(measurement);
			result.put(transaction, measurementList);
		}
	}
}
