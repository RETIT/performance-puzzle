package de.retit.puzzle;

import de.retit.puzzle.components.CsvReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Puzzle {

	private static final int THREAD_COUNT = 4;


	private String inputFile;
	private String outputDirectory;

	public Puzzle(String inputFile, String outputDirectory) {
		this.inputFile = inputFile;
		this.outputDirectory = outputDirectory;
	}

	public void start() {
		try {
			doStart();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void doStart() throws Exception {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 1, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

		Map<String, BufferedWriter> outputs = new ConcurrentHashMap<>();

		List<Future<?>> futures = Collections.synchronizedList(new ArrayList<>());
		
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

		AtomicBoolean finished = new AtomicBoolean();

		for (int i = 0; i < THREAD_COUNT; i++) {
			futures.add(executor.submit(() -> {
				while (true) {
					String line;
					try {
						line = queue.poll(10, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					if (line == null) {
						if (finished.get()) break;
						continue;
					}
					
					int comma1 = line.indexOf(',');
					int comma2 = line.indexOf(',', comma1 + 1);
					if (comma1 > 0 && comma2 > 0) {
						String nums = line.substring(0, comma2);
						String file = line.substring(comma2 + 1);
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
					
				}
			}));
		}

		// Read input CSV data
		new CsvReader(inputFile).read(queue::add);
		finished.set(true);

		for (Future<?> future : futures) {
			future.get();
		}

		for (BufferedWriter writer : outputs.values()) {
			writer.close();
		}
	}

}
