package de.retit.puzzle.components;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.retit.puzzle.entity.Measurement;

public class ResultAggregator extends Thread {

	private MappedByteBuffer csv;
	private static Map<String, List<Measurement>> result = new HashMap<>();

	public ResultAggregator(MappedByteBuffer csv) {
		this.csv = csv;
	}

	public static Map<String, List<Measurement>> getResult() {
		return result;
	}

	public void run() {
		StringBuilder sb = new StringBuilder();
		Long timestamp = null;
		Long time = null;
		String transaction = null;
		while (csv.hasRemaining()) {
			byte c = csv.get();
			if (c == ',') {
				if (timestamp == null) {
					timestamp = Long.parseLong(sb.toString());
				} else {
					time = Long.parseLong(sb.toString());
				}
				sb = new StringBuilder();
			} else if (c == '\n') {
				transaction = sb.toString();
				sb = new StringBuilder();
				Measurement measurement = new Measurement(timestamp, time);

				// Add to map
				List<Measurement> measurementList;
				if (result.containsKey(transaction)) {
					measurementList = result.get(transaction);
				} else {
					measurementList = new ArrayList<>();
					result.put(transaction, measurementList);
				}
				measurementList.add(measurement);
			} else if (c == '\r') {
				continue;
			} else {
				sb.append((char) c);
			}
		}
	}
}
