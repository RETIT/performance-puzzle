package de.retit.puzzle.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.retit.puzzle.entity.Measurement;

public class ResultAggregator extends Thread {

	private List<String> csv;
	private static Map<String, List<Measurement>> result = new HashMap<>();

	public ResultAggregator(List<String> csv) {
		this.csv = csv;
	}

	public static Map<String, List<Measurement>> getResult() {
		return result;
	}

	public void run() {
		for (String line : csv) {
			int firstComma = line.indexOf(',');
			int secondComma = line.indexOf(',', firstComma + 1);

			String timestamp = line.substring(0, firstComma);
			String time = line.substring(firstComma + 1, secondComma);
			String transaction = line.substring(secondComma + 1);

			// Build Measurement
			Long date = Long.parseLong(timestamp);
			Long timeLong = Long.parseLong(time);
			Measurement measurement = new Measurement(date, timeLong);

			// Add to map
			List<Measurement> measurementList;
			if (result.containsKey(transaction)) {
				measurementList = result.get(transaction);
			} else {
				synchronized (result) {
					if (!result.containsKey(transaction)) {
						measurementList = Collections.synchronizedList(new ArrayList<>());
						result.put(transaction, measurementList);
					} else {
						measurementList = result.get(transaction);
					}
				}
			}
			measurementList.add(measurement);
		}
	}
}
