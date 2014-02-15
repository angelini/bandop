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

public class AlgorithmType extends ApplicationModel {

    public static List<AlgorithmType> loadTypes(Database db) {
        String query = " SELECT id, name, defaults" +
                       " FROM algorithm_types";

        return db.fetchModels(AlgorithmType.class, query);
    }

    public static AlgorithmType loadType(Database db, int id) throws DatabaseException {
        String query = " SELECT id, name, defaults" +
                       " FROM algorithm_types" +
                       " WHERE id = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(id);

        List<AlgorithmType> algorithmTypes = db.fetchModels(AlgorithmType.class, query, params);

        if (algorithmTypes.size() == 0) {
            throw new ResourceNotFoundException("Algorithm Type not found");
        }

        return algorithmTypes.get(0);
    }

    private String name;
    private Map<String, Double> defaults;

    public AlgorithmType() {

    }

    public AlgorithmType(ResultSet result) throws SQLException {
        id   = result.getInt(result.findColumn("id"));
        name = result.getString(result.findColumn("name"));

        Map<String, String> rawDefaults = (Map<String, String>) result.getObject(result.findColumn("defaults"));
        Map<String, Double> defaultConfig = new HashMap<String, Double>(rawDefaults.size());

        for (Map.Entry<String, String> rawDefault : rawDefaults.entrySet()) {
            defaultConfig.put(rawDefault.getKey(), Double.parseDouble(rawDefault.getValue()));
        }

        defaults = defaultConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getDefaults() {
        return defaults;
    }

    public void setDefaults(Map<String, Double> defaults) {
        this.defaults = defaults;
    }

}
