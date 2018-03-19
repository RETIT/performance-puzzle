package de.retit.puzzle.components;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvReader {

	private static final Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

	private String fileLocation;

	public CsvReader(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public MappedByteBuffer read() {
		File file = new File(fileLocation);
		if (!file.exists()) {
			throw new IllegalStateException("File does not exist!");
		}
		try (FileReader fileReader = new FileReader(file)) {
			FileChannel fileChannel = FileChannel.open(file.toPath());
			MappedByteBuffer byteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
			return byteBuffer;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading input file file", e);
		}
		return null;
	}
}