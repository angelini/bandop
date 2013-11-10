package com.mcgill.bandop.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mcgill.bandop.Database;
import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.exceptions.ResourceNotFoundException;

public class Design extends ApplicationModel {

	public static List<Design> loadDesigns(Database db, int userId) {
		String query = " SELECT id, user_id, name, css_file, js_file, screenshot" +
					   " FROM designs" +
					   " WHERE user_id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(new Integer(userId));

		return db.fetchModels(Design.class, query, params);
	}

	public static Design loadDesign(Database db, int userId, int id) throws DatabaseException {
		String query = " SELECT id, user_id, name, css_file, js_file, screenshot" +
					   " FROM designs" +
					   " WHERE user_id = ?" +
					   " AND id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(new Integer(userId));
		params.add(new Integer(id));

		List<Design> designs = db.fetchModels(Design.class, query, params);

		if (designs.size() == 0) {
			throw new ResourceNotFoundException("Design not found");
		}

		return designs.get(0);
	}

	public static String loadJsFile(Database db, int id) throws DatabaseException {
		String query = " SELECT js_file" +
					   " FROM designs" +
					   " WHERE id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(new Integer(id));

		ResultSet result = db.executeQuery(query, params);

		try {
			if (!result.next()) return "";
			return result.getString(1);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public static String loadCssFile(Database db, int id) throws DatabaseException {
		String query = " SELECT css_file" +
					   " FROM designs" +
					   " WHERE id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(new Integer(id));

		ResultSet result = db.executeQuery(query, params);

		try {
			if (!result.next()) return "";
			return result.getString(1);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	private int userId;
	private String name;
	private String cssFile;
	private String jsFile;
	private String screenshot;

	public Design() {

	}

	public Design(int userId, String name, String cssFile, String jsFile, String screenshot) {
		this.setUserId(userId);
		this.setName(name);
		this.setCssFile(cssFile);
		this.setJsFile(jsFile);
		this.setScreenshot(screenshot);
	}

	public Design(ResultSet result) throws SQLException {
		this.setId(result.getInt(result.findColumn("id")));
		this.setUserId(result.getInt(result.findColumn("user_id")));
		this.setName(result.getString(result.findColumn("name")));
		this.setCssFile(result.getString(result.findColumn("css_file")));
		this.setJsFile(result.getString(result.findColumn("js_file")));
		this.setScreenshot(result.getString(result.findColumn("screenshot")));
	}

	public void save(Database db) {
		if (this.getId() == 0) {
			this.createDesign(db);
		} else {
			this.updateDesign(db);
		}
	}

	public void createDesign(Database db) {
		String query = " INSERT INTO designs (user_id, name, css_file, js_file, screenshot)" +
					   " VALUES (?, ?, ?, ?, ?)";

		List<Object> params = new ArrayList<Object>();
		params.add(this.getUserId());
		params.add(this.getName());
		params.add(this.getCssFile());
		params.add(this.getJsFile());
		params.add(this.getScreenshot());

		db.createModel(this, query, params);
	}

	public void updateDesign(Database db) {
		String query = " UPDATE designs" +
					   " SET name = ?, css_file = ?, js_file = ?, screenshot = ?" +
				   	   " WHERE id = ?" +
					   " AND user_id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(this.getName());
		params.add(this.getCssFile());
		params.add(this.getJsFile());
		params.add(this.getScreenshot());
		params.add(this.getId());
		params.add(this.getUserId());

		db.executeUpdate(query, params);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCssFile() {
		return cssFile;
	}

	public void setCssFile(String cssFile) {
		this.cssFile = cssFile;
	}

	public String getJsFile() {
		return jsFile;
	}

	public void setJsFile(String jsFile) {
		this.jsFile = jsFile;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

}
