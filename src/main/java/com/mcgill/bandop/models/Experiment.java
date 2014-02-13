package com.mcgill.bandop.models;

import com.mcgill.bandop.Database;
import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.exceptions.ResourceNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Experiment extends ApplicationModel {

    public static List<Experiment> loadExperiments(Database db, int userId) {
        String query = " SELECT id, user_id, algorithm_id, name" +
                       " FROM experiments" +
                       " WHERE user_id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(new Integer(userId));

        return db.fetchModels(Experiment.class, query, params);
    }

    public static Experiment loadExperiment(Database db, int id) throws DatabaseException {
        String query = " SELECT id, user_id, algorithm_id, name" +
                       " FROM experiments" +
                       " AND id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(new Integer(id));

        List<Experiment> experiments = db.fetchModels(Experiment.class, query, params);

        if (experiments.size() == 0) {
            throw new ResourceNotFoundException("Experiment not found");
        }

        experiments.get(0).loadAlgorithm(db);
        return experiments.get(0);
    }

    private int userId;
    private int algorithmId;
    private String name;

    private Algorithm algorithm;

    public Experiment() {

    }

    public Experiment(int userId, int algorithmId, String name) {
        this.setUserId(userId);
        this.setAlgorithmId(algorithmId);
        this.setName(name);
    }

    public Experiment(ResultSet result) throws SQLException {
        this.setId(result.getInt(result.findColumn("id")));
        this.setUserId(result.getInt(result.findColumn("user_id")));
        this.setAlgorithmId(result.getInt(result.findColumn("algorithm_id")));
        this.setName(result.getString(result.findColumn("name")));
    }

    public void loadAlgorithm(Database db) {
        setAlgorithm(Algorithm.loadAlgorithm(db, getAlgorithmId()));
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(int algorithmId) {
        this.algorithmId = algorithmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

}
