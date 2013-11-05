package com.mcgill.bandop;

import java.util.Map;
import java.util.Random;

import redis.clients.jedis.Jedis;

public class BanditWorker {

	static final String DESIGN_PREFIX = "design-";

	private Jedis conn;

	public BanditWorker(String hostname, int port) {
		conn = new Jedis(hostname, port);
	}

	public WeightedDesignMap getDesignWeights(int userId) {
		Map<String, String> strWeights = getConn().hgetAll(DESIGN_PREFIX + Integer.toString(userId));
		return new WeightedDesignMap(strWeights);
	}

	public int getDesignId(int userId, Random random) {
		return getDesignWeights(userId).select(random);
	}

	public Jedis getConn() {
		return conn;
	}

	public void setConn(Jedis conn) {
		this.conn = conn;
	}

}
