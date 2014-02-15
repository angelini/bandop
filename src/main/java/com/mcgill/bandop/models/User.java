package com.mcgill.bandop.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mcgill.bandop.Database;
import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.exceptions.ResourceNotFoundException;

public class User extends ApplicationModel {

	public static List<User> loadUsers(Database db) throws DatabaseException {
		String query = " SELECT id, email, password, domain" +
				   	   " FROM users";

		return db.fetchModels(User.class, query);
	}

	public static User loadUser(Database db, int id) throws DatabaseException {
		String query = " SELECT id, email, password, domain" +
					   " FROM users" +
					   " WHERE id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(id);

		List<User> users = db.fetchModels(User.class, query, params);

		if (users.size() == 0) {
			throw new ResourceNotFoundException("User not found");
		}

		return users.get(0);
	}

	public static User loadUser(Database db, String email) throws DatabaseException {
		String query = " SELECT id, email, password, domain" +
					   " FROM users" +
					   " WHERE email = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(email);

		List<User> users = db.fetchModels(User.class, query, params);

		if (users.size() == 0) {
			throw new ResourceNotFoundException("User not found");
		}

		return users.get(0);
	}

	private String email;
	private String password;
	private String domain;
	private String key;

	public User() {

	}

	public User(String email, String password, String domain) {
		this.email    = email;
		this.password = password;
		this.domain   = domain;
	}

	public User(ResultSet result) throws SQLException {
		id       = result.getInt(result.findColumn("id"));
		email    = result.getString(result.findColumn("email"));
		password = result.getString(result.findColumn("password"));
		domain   = result.getString(result.findColumn("domain"));
	}

	public void save(Database db) {
		if (this.getId() == 0) {
			createUser(db);
		} else {
			updateUser(db);
		}
	}

	public void createUser(Database db) {
		String query = " INSERT INTO users (email, password, domain)" +
					   " VALUES (?, ?, ?)";

		List<Object> params = new ArrayList<Object>();
		params.add(email);
		params.add(password);
		params.add(domain);

		db.createModel(this, query, params);
	}

	public void updateUser(Database db) {
		String query = " UPDATE users" +
					   " SET email = ?, password = ?, domain = ?" +
				   	   " WHERE id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(email);
		params.add(password);
		params.add(domain);
		params.add(id);

		db.executeUpdate(query, params);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
