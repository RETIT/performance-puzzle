package de.retit.puzzle.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderWriter {

    private static final Logger LOGGER = Logger.getLogger(ReaderWriter.class.getName());

    private final Path outputDirectoryPath;
    private String inputFileLocation;
    private Map<String, FileWriter> transactionFiles = new HashMap<>();

    public ReaderWriter(String inputFileLocation, String outputDirectory) {
        this.inputFileLocation = inputFileLocation;

        outputDirectoryPath = new File(outputDirectory).toPath();
    }

    public void start() {
        File file = new File(inputFileLocation);
        if (!file.exists()) {
            throw new IllegalStateException("File does not exist!");
        }

        try {
            Files.lines(file.toPath()).forEach(this::parseAndWrite);

            for (final FileWriter writer : transactionFiles.values()) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading input file file", e);
        }
    }

    private void parseAndWrite(String line) {
        final String[] split = line.split(",");
        String timestamp = split[0];
        String time = split[1];
        String transaction = split[2];

        if (transactionFiles.containsKey(transaction)) {
            try {
                transactionFiles.get(transaction).append(timestamp + ',' + time + '\n');
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error appending text to " + transaction, e);
            }
        } else {
            try {
                final FileWriter fileWriter = new FileWriter(outputDirectoryPath.resolve(transaction + ".csv").toFile(),
                        false);
                transactionFiles.put(transaction, fileWriter);
                fileWriter.append(timestamp + ',' + time + '\n');
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error opening fileWriter for " + transaction, e);
            }
        }
    }
}
