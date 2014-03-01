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
        params.add(userId);

        List<Experiment> experiments = db.fetchModels(Experiment.class, query, params);

        for (Experiment experiment : experiments) {
            experiment.loadAlgorithm(db);
        }

        return experiments;
    }

    public static Experiment loadExperiment(Database db, int userId, int id) throws DatabaseException {
        String query = " SELECT id, user_id, algorithm_id, name" +
                       " FROM experiments" +
                       " WHERE user_id = ?" +
                       " AND id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        params.add(id);

        List<Experiment> experiments = db.fetchModels(Experiment.class, query, params);

        if (experiments.size() == 0) {
            throw new ResourceNotFoundException("Experiment not found");
        }

        experiments.get(0).loadAlgorithm(db);
        return experiments.get(0);
    }

    public static boolean ownedByUser(Database db, int userId, int id) throws DatabaseException {
        String query = " SELECT id" +
                       " FROM experiments" +
                       " WHERE user_id = ?" +
                       " AND id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        params.add(id);

        ResultSet results = db.executeQuery(query, params);

        try {
            return results.next();

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private int userId;
    private int algorithmId;
    private String name;

    private Algorithm algorithm;

    public Experiment() {

    }

    public Experiment(int userId, int algorithmId, String name) {
        this.userId      = userId;
        this.algorithmId = algorithmId;
        this.name        = name;
    }

    public Experiment(ResultSet result) throws SQLException {
        id          = result.getInt(result.findColumn("id"));
        userId      = result.getInt(result.findColumn("user_id"));
        algorithmId = result.getInt(result.findColumn("algorithm_id"));
        name        = result.getString(result.findColumn("name"));
    }

    public void save(Database db) {
        if (id == 0) {
            getAlgorithm().save(db);

            algorithmId = getAlgorithm().getId();
            createExperiment(db);
        } else {
            getAlgorithm().save(db);
            updateExperiment(db);
        }
    }

    public void createExperiment(Database db) {
        String query = " INSERT INTO experiments (user_id, algorithm_id, name)" +
                       " VALUES (?, ?, ?)";

        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        params.add(algorithmId);
        params.add(name);

        db.createModel(this, query, params);
    }

    public void updateExperiment(Database db) {
        String query = " UPDATE experiments" +
                       " SET name = ?" +
                       " WHERE id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(name);
        params.add(id);

        db.executeUpdate(query, params);
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
