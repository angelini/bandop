package com.mcgill.bandop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.models.ApplicationModel;

public class Database {

	private Connection conn;

	public Database(Connection conn) {
		this.conn = conn;
	}

	public <T extends ApplicationModel> List<T> fetchModels(Class<T> type, String query) throws DatabaseException {
		return fetchModels(type, query, new ArrayList<Object>());
	}

	public <T extends ApplicationModel> List<T> fetchModels(Class<T> type, String query, List<Object> params) throws DatabaseException {
		try {
			List<T> models = new ArrayList<T>();
			PreparedStatement statement = conn.prepareStatement(query);

			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}

			ResultSet results = statement.executeQuery();

			while(results.next()) {
				models.add(type.getDeclaredConstructor(ResultSet.class).newInstance(results));
			}

			return models;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
