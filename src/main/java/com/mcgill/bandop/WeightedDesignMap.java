package com.mcgill.bandop;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedDesignMap {

	private final NavigableMap<Double, Integer> map = new TreeMap<Double, Integer>();

	private double total = 0;

    public WeightedDesignMap(Map<Integer, Double> weights) {
		for (Map.Entry<Integer, Double> entry : weights.entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				add(entry.getValue(), entry.getKey());
			}
		}
    }

    private void add(double prob, int designId) {
    	if (prob <= 0) return;
        total += prob;
        map.put(total, designId);
    }

    public int select(Random random) {
    	double value = random.nextDouble() * total;
    	return map.ceilingEntry(value).getValue();
    }

}
