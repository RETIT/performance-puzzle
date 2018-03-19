package de.retit.puzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.retit.puzzle.entity.Measurement;

public class Puzzle {
	
	private String inputFile;
	private String outputDirectory;

	private Map<String, List<Measurement>> result = new HashMap<>();

	public Puzzle(String inputFile, String outputDirectory) {
		this.inputFile = inputFile;
		this.outputDirectory = outputDirectory;
	}

	public void start() throws InterruptedException {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] array = line.split(",");
				String timestamp = array[0];
				String time = array[1];
				String transaction = array[2];

				// Build Measurement
				Date date = new Date(Long.parseLong(timestamp));
				Double timeDouble = Double.valueOf(time);
				Measurement measurement = new Measurement(date, timeDouble);

				// Add to map
				List<Measurement> measurementList;
				if (result.containsKey(transaction)) {
					measurementList = result.get(transaction);
				} else {
					measurementList = new ArrayList<>();
				}
				measurementList.add(measurement);
				result.put(transaction, measurementList);
			}

			for (Entry<String, List<Measurement>> entry : result.entrySet()) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < entry.getValue().size(); i++) {
					Measurement measurement = entry.getValue().get(i);
					builder.append(measurement.getTime().getTime() + "," + measurement.getValue().longValue());
					if (i < entry.getValue().size() - 1) {
						builder.append("\n");
					}
				}
				Path outputDirectoryPath = new File(outputDirectory).toPath();
				Path outputFile = outputDirectoryPath.resolve(entry.getKey() + ".csv");
				try {
					Files.write(outputFile, builder.toString().getBytes(), StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
