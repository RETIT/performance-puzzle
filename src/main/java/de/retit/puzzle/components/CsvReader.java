package de.retit.puzzle.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvReader {

	private static final Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

	private String fileLocation;

	public CsvReader(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void read(Consumer<String> lineConsumer) {
		File file = new File(fileLocation);
		if (!file.exists()) {
			throw new IllegalStateException("File does not exist!");
		}
		try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
			fileReader.lines().filter(s -> !s.isEmpty()).forEach(lineConsumer);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading input file file", e);
		}
	}
}