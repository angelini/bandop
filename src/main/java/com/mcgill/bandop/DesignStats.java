package com.mcgill.bandop;

import java.util.HashMap;
import java.util.Map;

public class DesignStats {

	private int count;
	private double avgReward;
	private double weight;
	private double prob;

	public DesignStats() {

	}

	public DesignStats(Map<String, String> stats) {
		if (stats.containsKey("count")) setCount(Integer.parseInt(stats.get("count")));
		if (stats.containsKey("avgReward")) setAvgReward(Double.parseDouble(stats.get("avgReward")));
		if (stats.containsKey("weight")) setWeight(Double.parseDouble(stats.get("weight")));
		if (stats.containsKey("prob")) setProb(Double.parseDouble(stats.get("prob")));
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("count", Integer.toString(getCount()));
		map.put("avgReward", Double.toString(getCount()));
		map.put("weight", Double.toString(getCount()));
		map.put("prob", Double.toString(getCount()));

		return map;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getAvgReward() {
		return avgReward;
	}

	public void setAvgReward(double avgReward) {
		this.avgReward = avgReward;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

}
