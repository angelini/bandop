package com.mcgill.bandop;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mcgill.bandopshared.DesignStats;
import com.mcgill.bandopshared.RedisKeys;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BanditWorker {

    private JedisPool pool;

	public BanditWorker(String hostname, int port) {
        pool = new JedisPool(new JedisPoolConfig(), hostname, port);
	}

	public WeightedDesignMap getDesignWeights(int experimentId) {
        Jedis conn = getConn();

        Set<String> designs = conn.smembers(RedisKeys.designs(experimentId));
        Map<Integer, Double> probs = new HashMap<Integer, Double>(designs.size());

        for (String design : designs) {
            String prob = conn.hget(RedisKeys.design(design), "prob");
            probs.put(Integer.parseInt(design), Double.parseDouble(prob));
        }

        returnConn(conn);

		return new WeightedDesignMap(probs);
	}

	public void addExperiment(int experimentId, Map<String, Double> config) {
        Jedis conn = getConn();
        Map<String, String> configString = new HashMap<String, String>();

        for (Map.Entry<String, Double> configEntry : config.entrySet()) {
            configString.put(configEntry.getKey(), Double.toString(configEntry.getValue()));
        }

        conn.sadd(RedisKeys.experiments(), Integer.toString(experimentId));
        conn.hmset(RedisKeys.config(experimentId), configString);
        conn.set(RedisKeys.count(experimentId), "0");

        returnConn(conn);
	}

	public int getDesignId(int experimentId, Random random) {
		return getDesignWeights(experimentId).select(random);
	}

	public void addDesign(int experimentId, int designId) {
        Jedis conn = getConn();

		conn.sadd(RedisKeys.designs(experimentId), Integer.toString(designId));
		conn.hmset(RedisKeys.design(designId), (new DesignStats()).toMap());

        returnConn(conn);
	}

	public void pushDesignResult(int designId, double status) {
        Jedis conn = getConn();

		getConn().rpush(RedisKeys.pending(designId), Double.toString(status));

        returnConn(conn);
	}

	public DesignStats getDesignStats(int designId) {
        Jedis conn = getConn();

		Map<String, String> stats = conn.hgetAll(RedisKeys.design(designId));
        returnConn(conn);

		return new DesignStats(stats);
	}

	public Jedis getConn() {
		return pool.getResource();
	}

    public void returnConn(Jedis conn) {
        pool.returnResource(conn);
    }

}
