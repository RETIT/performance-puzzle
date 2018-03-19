package de.retit.puzzle.components;

import de.retit.puzzle.entity.Measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultAggregator extends Thread {

    private static Map<String, List<Measurement>> result = new ConcurrentHashMap<>();

    public ResultAggregator() {
    }

    public static Map<String, List<Measurement>> getResult() {
        return result;
    }

    public void run() {

    }

    public void parse(String line) {
        final String[] split = line.split(",");
        String timestamp = split[0];
        String time = split[1];
        String transaction = split[2];

        // Build Measurement
        Measurement measurement = new Measurement(timestamp, time);

        List<Measurement> measurementList;
        synchronized (result) {
            if (result.containsKey(transaction)) {
                measurementList = result.get(transaction);
            } else {
                measurementList = new ArrayList<>();
            }
            measurementList.add(measurement);
            result.put(transaction, measurementList);
        }
    }
}
