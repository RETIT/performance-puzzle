package de.retit.puzzle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import de.retit.puzzle.components.CsvReader;

public class PuzzleTest {

	private static final String[] INPUT_FILES = { "input1.jtl", "input2.jtl", "input3.jtl" };
	private static final String[] OUTPUT_FILES = { "OpenCargoRegistrationPage", "RegisterCargo", "OpenRouteCargoPage",
			"AssignCargoRoute", "LoadCargoRequest", "ReceiveCargoRequest", "UnLoadCargoRequest", "CustomsCargoRequest",
			"ClaimCargoRequest", "TestCargoLifecycle" };

	private static Path inputFileTmp;
	private static Path outputDirectoryTmp;
	private static Collection<RunResult> results;

	@BeforeClass
	public static void setup() throws IOException, RunnerException {
		outputDirectoryTmp = Files.createTempDirectory("puzzle");
		outputDirectoryTmp.toFile().deleteOnExit();
		inputFileTmp = copyInputDataToTmp();

		results = runBenchmark();
	}

	@Test
	public void testAllFilesCreated() {
		for (String outputFile : OUTPUT_FILES) {
			Path file = outputDirectoryTmp.resolve(outputFile + ".csv");
			assertTrue("File for " + outputFile + " does not exist", Files.exists(file));
		}
	}

	@Test
	public void testNumberOfLines() throws IOException {
		Properties prop = new Properties();
		InputStream in = PuzzleTest.class.getClassLoader().getResourceAsStream("linenumbers.properties");
		prop.load(in);
		in.close();

		for (String outputFile : OUTPUT_FILES) {
			Path file = outputDirectoryTmp.resolve(outputFile + ".csv");
			List<String> lines = Files.readAllLines(file);
			int reference = Integer.parseInt(prop.getProperty(outputFile));
			int actual = lines.size();
			assertEquals("Line numbers do not match for " + outputFile, reference, actual);
		}
	}

	@Test
	public void testResponseTimeSums() throws IOException {
		Properties prop = new Properties();
		InputStream in = PuzzleTest.class.getClassLoader().getResourceAsStream("responsetimes.properties");
		prop.load(in);
		in.close();

		for (String outputFile : OUTPUT_FILES) {
			Path file = outputDirectoryTmp.resolve(outputFile + ".csv");
			List<String> lines = Files.readAllLines(file);
			long actual = 0;
			for (String line : lines) {
				int index = line.indexOf(',');
				assertTrue("Malformed output file for " + outputFile, index > 0);
				actual += Long.parseLong(line.substring(index + 1));
			}
			long reference = Long.parseLong(prop.getProperty(outputFile));
			assertEquals("Response times do not match for " + outputFile, reference, actual);
		}
	}

	public static Collection<RunResult> runBenchmark() throws RunnerException {
		Options opt = new OptionsBuilder().include(SimpleBenchmark.class.getSimpleName())
				.param("inputFile", inputFileTmp.toFile().getAbsolutePath())
				.param("outputDirectory", outputDirectoryTmp.toFile().getAbsolutePath()).build();
		return new Runner(opt).run();
	}

	private static Path copyInputDataToTmp() throws IOException {
		Path result = Files.createTempFile("puzzle", "input");
		if (result != null) {
			result.toFile().deleteOnExit();
		}
		for (String inputFile : INPUT_FILES) {
			try (InputStream is = CsvReader.class.getClassLoader().getResourceAsStream(inputFile);
					OutputStream out = new FileOutputStream(result.toFile(), true)) {

				byte[] buf = new byte[1024];
				int len;
				while ((len = is.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			}
		}
		return result;
	}
}
