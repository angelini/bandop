package com.mcgill.bandop;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedDesignMap {

	private final NavigableMap<Double, Integer> map = new TreeMap<Double, Integer>();

	private double total = 0;

    public WeightedDesignMap(Map<String, String> weights) {
		for (Map.Entry<String, String> entry : weights.entrySet()) {
			Integer designId = Integer.parseInt(entry.getKey());
			Double weight = Double.parseDouble(entry.getValue());

			if (designId != null && weight != null) {
				add(weight, designId);
			}
		}
    }

    private void add(double weight, int designId) {
    	if (weight <= 0) return;
        total += weight;
        map.put(total, designId);
    }

    public int select(Random random) {
    	double value = random.nextDouble() * total;
    	return map.ceilingEntry(value).getValue();
    }

}
