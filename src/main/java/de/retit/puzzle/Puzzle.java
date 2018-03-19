package de.retit.puzzle;

import de.retit.puzzle.components.CsvReader;
import de.retit.puzzle.components.ResultAggregator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;

public class Puzzle {

	private static final int THREAD_COUNT = 4;


	private String inputFile;
	private String outputDirectory;

	public Puzzle(String inputFile, String outputDirectory) {
		this.inputFile = inputFile;
		this.outputDirectory = outputDirectory;
	}

	public void start() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 1, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
		
		Map<String, BufferedWriter> outputs = new ConcurrentHashMap<>();
		
		List<Future<?>> futures = Collections.synchronizedList(new ArrayList<>());

		// Read input CSV data
		new CsvReader(inputFile).read(line -> futures.add(executor.submit(() -> {
			Matcher matcher = ResultAggregator.PATTERN.matcher(line);
			if (matcher.matches()) {
				String nums = matcher.group(1);
				String file = matcher.group(4);
				BufferedWriter writer = outputs.computeIfAbsent(file, s -> {
					try {
						return new BufferedWriter(new FileWriter(new File(outputDirectory, s + ".csv")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
				//noinspection SynchronizationOnLocalVariableOrMethodParameter
				synchronized (writer) {
					try {
						writer.write(nums);
						writer.write('\n');
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		})));

		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}

		for (BufferedWriter writer : outputs.values()) {
			try {
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
