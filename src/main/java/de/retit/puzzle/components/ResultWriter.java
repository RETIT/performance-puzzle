package de.retit.puzzle.components;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.retit.puzzle.entity.Measurement;

public class ResultWriter {

	private static final Logger LOGGER = Logger.getLogger(ResultWriter.class.getName());

	private String outputDirectory;
	private Map<String, List<Measurement>> transactionMap;

	public ResultWriter(String outputDirectory, Map<String, List<Measurement>> transactionMap) {
		this.outputDirectory = outputDirectory;
		this.transactionMap = transactionMap;
	}

	public void write() {

		Path outputDirectoryPath = new File(outputDirectory).toPath();
		for (Entry<String, List<Measurement>> entry : transactionMap.entrySet()) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < entry.getValue().size(); i++) {
				Measurement measurement = entry.getValue().get(i);
				stringBuilder.append(measurement.getTime().getTime())
						.append(",")
						.append(measurement.getValue().longValue());
				if (i < entry.getValue().size() - 1) {
					stringBuilder.append("\n");
				}
			}
			Path outputFile = outputDirectoryPath.resolve(entry.getKey() + ".csv");
			String content = stringBuilder.toString();
			try {
				Files.write(outputFile, content.getBytes(), StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error writing CSV file for " + entry.getKey(), e);
			}
		}
	}

}
