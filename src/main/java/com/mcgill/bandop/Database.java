package com.mcgill.bandop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.models.ApplicationModel;

public class Database {

	private Connection conn;

	public Database(Connection conn) {
		this.conn = conn;
	}

	public ResultSet executeQuery(String query) throws DatabaseException {
		return executeQuery(query, new ArrayList<Object>());
	}

	public ResultSet executeQuery(String query, List<Object> params) throws DatabaseException {
		try {
			PreparedStatement statement = buildQuery(query, params);
			return statement.executeQuery();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public PreparedStatement executeUpdate(String query, List<Object> params) throws DatabaseException {
		try {
			PreparedStatement statement = buildQuery(query, params, true);
			statement.executeUpdate();

			return statement;

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public <T extends ApplicationModel> List<T> fetchModels(Class<T> type, String query) throws DatabaseException {
		return fetchModels(type, query, new ArrayList<Object>());
	}

	public <T extends ApplicationModel> List<T> fetchModels(Class<T> type, String query, List<Object> params) throws DatabaseException {
		try {
			List<T> models = new ArrayList<T>();
			ResultSet results = executeQuery(query, params);

			while(results.next()) {
				models.add(type.getDeclaredConstructor(ResultSet.class).newInstance(results));
			}

			return models;

		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public <T extends ApplicationModel> void createModel(T model, String query, List<Object> params) throws DatabaseException {
		try {
			PreparedStatement statement = executeUpdate(query, params);
			ResultSet generatedKeys = statement.getGeneratedKeys();

			if (generatedKeys.next()) {
				model.setId(generatedKeys.getInt(1));
			} else {
				throw new DatabaseException("Error creating model");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	private PreparedStatement buildQuery(String query, List<Object> params) throws SQLException {
		return buildQuery(query, params, false);
	}

	private PreparedStatement buildQuery(String query, List<Object> params, boolean includeGeneratedKeys) throws SQLException {
		PreparedStatement statement;

		if (includeGeneratedKeys) {
			statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		} else {
			statement = conn.prepareStatement(query);
		}

		for (int i = 0; i < params.size(); i++) {
			statement.setObject(i + 1, params.get(i));
		}

		return statement;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
