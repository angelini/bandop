package com.mcgill.bandop.models;

import com.mcgill.bandop.Database;
import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.exceptions.ResourceNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Algorithm extends ApplicationModel {

    public static Algorithm loadAlgorithm(Database db, int id) throws DatabaseException {
        String query = " SELECT a.id, a.type, t.name, a.config" +
                       " FROM algorithms a, algorithm_types t" +
                       " WHERE a.id = ?" +
                       " AND a.type = t.id";

        List<Object> params = new ArrayList<Object>();
        params.add(id);

        List<Algorithm> algorithms = db.fetchModels(Algorithm.class, query, params);

        if (algorithms.size() == 0) {
            throw new ResourceNotFoundException("Algorithm not found");
        }

        return algorithms.get(0);
    }

    private int type;
    private String name;
    private Map<String, Double> config;

    public Algorithm() {

    }

    public Algorithm(ResultSet result) throws SQLException {
        id   = result.getInt(result.findColumn("id"));
        type = result.getInt(result.findColumn("type"));
        name = result.getString(result.findColumn("name"));

        Map<String, String> rawConfig = (Map<String, String>) result.getObject(result.findColumn("config"));
        Map<String, Double> configMap = new HashMap<String, Double>(rawConfig.size());

        for (Map.Entry<String, String> configEntry : rawConfig.entrySet()) {
            configMap.put(configEntry.getKey(), Double.parseDouble(configEntry.getValue()));
        }

        config = configMap;
    }

    public void save(Database db) {
        if (id == 0) {
            createAlgorithm(db);
        } else {
            updateAlgorithm(db);
        }
    }

    public void createAlgorithm(Database db) {
        String query = " INSERT INTO algorithms (type, config)" +
                       " VALUES (?, ?)";

        List<Object> params = new ArrayList<Object>();
        params.add(type);
        params.add(config);

        db.createModel(this, query, params);
    }

    public void updateAlgorithm(Database db) {
        String query = " UPDATE algorithms" +
                       " SET config = ?" +
                       " WHERE id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(config);
        params.add(id);

        db.executeUpdate(query, params);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Double> config) {
        this.config = config;
    }

}
