package de.retit.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.retit.puzzle.components.CsvReader;
import de.retit.puzzle.components.ResultAggregator;
import de.retit.puzzle.components.ResultWriter;
import de.retit.puzzle.entity.Measurement;
import de.retit.puzzle.util.MultiThreadingUtil;

public class Puzzle {
	
	private static final int THREAD_COUNT = 4;
	
	private static long previousTime;

	private String inputFile;
	private String outputDirectory;
	
	public Puzzle(String inputFile, String outputDirectory) {
		this.inputFile = inputFile;
		this.outputDirectory = outputDirectory;
	}
	
	public void start() throws InterruptedException {
		previousTime = System.nanoTime();
		
		List<String> csv = new CsvReader(inputFile).read();
		printTimeForTask("CsvReader");
		
		List<List<String>> csvChunks = MultiThreadingUtil.chunkList(csv, THREAD_COUNT);
		List<ResultAggregator> aggregators = new ArrayList<>();
		for(List<String> csvChunk : csvChunks) {
			ResultAggregator aggregator = new ResultAggregator(csvChunk);
			aggregators.add(aggregator);
			aggregator.start();
		}
		Map<String, List<Measurement>> aggregatedResult = new HashMap<>();
		for(ResultAggregator aggregator : aggregators) {
			aggregator.join();
		}
		aggregatedResult = ResultAggregator.getResult();
		printTimeForTask("ResultAggregator");
		
		new ResultWriter(outputDirectory, aggregatedResult).write();
		printTimeForTask("ResultWriter");
	}
	
	private static void printTimeForTask(String task) {
		long currentTime = System.nanoTime();
		if(previousTime != 0) {
			double passedTime = (currentTime - previousTime) / 1000000000.0;
			System.out.println(task + ": "+ passedTime + "s");
		}
		previousTime = currentTime;
	}
}
