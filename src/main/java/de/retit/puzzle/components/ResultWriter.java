package de.retit.puzzle.components;

import de.retit.puzzle.entity.Measurement;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultWriter {

	private static final Logger LOGGER = Logger.getLogger(ResultWriter.class.getName());

	private String outputDirectory;
	private Map<String, List<Measurement>> transactionMap;

	public ResultWriter(String outputDirectory, Map<String, List<Measurement>> transactionMap) {
		this.outputDirectory = outputDirectory;
		this.transactionMap = transactionMap;
	}

	public void write() {
		for (Entry<String, List<Measurement>> entry : transactionMap.entrySet()) {
			try (Writer writer = new BufferedWriter(new FileWriter(new File(outputDirectory, entry.getKey() + ".csv")))) {
				for (int i = 0; i < entry.getValue().size(); i++) {
					Measurement measurement = entry.getValue().get(i);
					writer.write(measurement.getTime());
					writer.write(",");
					writer.write(measurement.getValue());
					if (i < entry.getValue().size() - 1) {
						writer.write('\n');
					}
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error writing CSV file for " + entry.getKey(), e);
			}
		}
	}

}
