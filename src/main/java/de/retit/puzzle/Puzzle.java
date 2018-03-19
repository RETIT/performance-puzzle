package de.retit.puzzle;

import de.retit.puzzle.components.ReaderWriter;

public class Puzzle {

    private static long previousTime;

    private String inputFile;
    private String outputDirectory;

    public Puzzle(String inputFile, String outputDirectory) {
        this.inputFile = inputFile;
        this.outputDirectory = outputDirectory;
    }

    public void start() {
        System.out.println();
        previousTime = System.nanoTime();

        new ReaderWriter(inputFile, outputDirectory).start();

        printTimeForTask("ReaderWriter");
    }

    private static void printTimeForTask(String task) {
        long currentTime = System.nanoTime();
        if (previousTime != 0) {
            double passedTime = (currentTime - previousTime) / 1000000000.0;
            System.out.println(task + ": " + passedTime + "s");
        }
        previousTime = currentTime;
    }
}
