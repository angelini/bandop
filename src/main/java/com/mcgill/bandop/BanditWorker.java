package com.mcgill.bandop;

import java.util.Map;
import java.util.Random;

import redis.clients.jedis.Jedis;

public class BanditWorker {

	static final String DESIGNS_PREFIX = "user-designs-";
	static final String RESULT_PREFIX = "design-results-";
	static final String WEIGHT_PREFIX = "design-weights-";

	private Jedis conn;

	public BanditWorker(String hostname, int port) {
		conn = new Jedis(hostname, port);
	}

	public WeightedDesignMap getDesignWeights(int userId) {
		String key = WEIGHT_PREFIX + Integer.toString(userId);
		Map<String, String> strWeights = getConn().hgetAll(key);

		return new WeightedDesignMap(strWeights);
	}

	public int getDesignId(int userId, Random random) {
		return getDesignWeights(userId).select(random);
	}

	public void addDesign(int userId, int designId) {
		String key = DESIGNS_PREFIX + Integer.toString(userId);
		getConn().sadd(key, Integer.toString(designId));
	}

	public void pushDesignResult(int designId, int status) {
		String key = RESULT_PREFIX + Integer.toString(designId);
		getConn().sadd(key, Integer.toString(status));
	}

	public Jedis getConn() {
		return conn;
	}

	public void setConn(Jedis conn) {
		this.conn = conn;
	}

}
