package de.retit.puzzle.components;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;

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
			BufferedReader bf = new BufferedReader(fileReader);
			String l;
			while((l = bf.readLine()) != null) {
				lines.add(l);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading input file file", e);
		}
		return lines;
	}
}