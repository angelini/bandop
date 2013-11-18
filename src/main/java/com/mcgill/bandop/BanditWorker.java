package com.mcgill.bandop;

import java.util.Map;
import java.util.Random;

import redis.clients.jedis.Jedis;

public class BanditWorker {

	static final String SEPERATOR = ":";

	static final String USERS_KEY = "users";
	static final String DESIGN_KEY = "design";
	static final String DESIGNS_KEY = "designs";
	static final String PENDING_KEY = "pendingRewards";
	static final String WEIGHTS_KEY = "weights";

	private Jedis conn;

	public BanditWorker(String hostname, int port) {
		conn = new Jedis(hostname, port);
	}

	public WeightedDesignMap getDesignWeights(int userId) {
		String key = buildKey(new String[]{Integer.toString(userId), WEIGHTS_KEY});
		Map<String, String> strWeights = getConn().hgetAll(key);

		return new WeightedDesignMap(strWeights);
	}

	public void addUser(int userId) {
		getConn().sadd(USERS_KEY, Integer.toString(userId));
	}

	public int getDesignId(int userId, Random random) {
		return getDesignWeights(userId).select(random);
	}

	public void addDesign(int userId, int designId) {
		String key = buildKey(new String[] {Integer.toString(userId), DESIGNS_KEY});
		getConn().sadd(key, Integer.toString(designId));

		key = buildKey(new String[] {DESIGN_KEY, Integer.toString(designId)});
		getConn().hmset(key, (new DesignStats()).toMap());
	}

	public void pushDesignResult(int designId, int status) {
		String key = buildKey(new String[] {DESIGN_KEY, Integer.toString(designId), PENDING_KEY});
		getConn().rpush(key, Integer.toString(status));
	}

	public DesignStats getDesignStats(int designId) {
		String key = buildKey(new String[] {DESIGN_KEY, Integer.toString(designId)});
		Map<String, String> stats = getConn().hgetAll(key);

		return new DesignStats(stats);
	}

	public Jedis getConn() {
		return conn;
	}

	public void setConn(Jedis conn) {
		this.conn = conn;
	}

	private String buildKey(String[] parts) {
		String loopDelimiter = "";
		StringBuilder sb = new StringBuilder();

		for (String part : parts) {
			sb.append(loopDelimiter);
			sb.append(part);

			loopDelimiter = SEPERATOR;
		}

		return sb.toString();
	}

}
