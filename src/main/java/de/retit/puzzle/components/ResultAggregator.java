package de.retit.puzzle.components;

import de.retit.puzzle.entity.Measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultAggregator extends Thread {

    private static final String LINE_REGEX = "([0-9]*),([0-9]*),([a-zA-Z]*)";

    private List<String> csv;
    private static Map<String, List<Measurement>> result = new ConcurrentHashMap<>();
    private final static Pattern pattern = Pattern.compile(LINE_REGEX);

    public ResultAggregator(List<String> csv) {
        this.csv = csv;
    }

    public static Map<String, List<Measurement>> getResult() {
        return result;
    }

    public void run() {

    }

    public void parse(String line) {
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String timestamp = matcher.group(1);
            String time = matcher.group(2);
            String transaction = matcher.group(3);

            // Build Measurement
            Measurement measurement = new Measurement(timestamp, time);

            List<Measurement> measurementList = new ArrayList<>(1000);
            if (result.containsKey(transaction)) {
                measurementList = result.get(transaction);
            }
            measurementList.add(measurement);
            result.put(transaction, measurementList);
        }
    }
}
