package de.retit.puzzle.components;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvReader {

	private static final Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

	private String fileLocation;

	public CsvReader(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public List<String> read() {
		File file = new File(fileLocation);
		if (!file.exists()) {
			throw new IllegalStateException("File does not exist!");
		}
		List<String> lines = new ArrayList<>();
		try (FileReader fileReader = new FileReader(file)) {
			String line = "";
			int character;
			while ((character = fileReader.read()) > 0) {
				if ((character == '\r' || character == '\n')) {
					if(!line.equals("")) {
						lines.add(line);
						line = "";
					}
				} else {
					line += (char) character;
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading input file file", e);
		}
		return lines;
	}
}