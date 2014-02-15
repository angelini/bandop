package com.mcgill.bandop.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mcgill.bandop.BanditWorker;
import com.mcgill.bandop.Database;
import com.mcgill.bandop.exceptions.DatabaseException;
import com.mcgill.bandop.exceptions.ResourceNotFoundException;

import com.mcgill.bandopshared.DesignStats;

public class Design extends ApplicationModel {

	public static List<Design> loadDesigns(Database db, int experimentId) {
		String query = " SELECT id, experiment_id, name, css_file, js_file, screenshot" +
					   " FROM designs" +
					   " WHERE experiment_id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(new Integer(experimentId));

		return db.fetchModels(Design.class, query, params);
	}

	public static Design loadDesign(Database db, int id) throws DatabaseException {
		String query = " SELECT id, experiment_id, name, css_file, js_file, screenshot" +
					   " FROM designs" +
					   " AND id = ?";

		List<Object> params = new ArrayList<Object>();
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

	private int experimentId;
	private String name;
	private String cssFile;
	private String jsFile;
	private String screenshot;

	private DesignStats stats;

	public Design() {

	}

	public Design(int experimentId, String name, String cssFile, String jsFile, String screenshot) {
		this.experimentId = experimentId;
		this.name         = name;
		this.cssFile      = cssFile;
		this.jsFile       = jsFile;
		this.screenshot   = screenshot;
	}

	public Design(ResultSet result) throws SQLException {
		id           = result.getInt(result.findColumn("id"));
		experimentId = result.getInt(result.findColumn("experiment_id"));
		name         = result.getString(result.findColumn("name"));
		cssFile      = result.getString(result.findColumn("css_file"));
		jsFile       = result.getString(result.findColumn("js_file"));
		screenshot   = result.getString(result.findColumn("screenshot"));
	}

	public void save(Database db) {
		if (this.getId() == 0) {
			createDesign(db);
		} else {
			updateDesign(db);
		}
	}

	public void createDesign(Database db) {
		String query = " INSERT INTO designs (experiment_id, name, css_file, js_file, screenshot)" +
					   " VALUES (?, ?, ?, ?, ?)";

		List<Object> params = new ArrayList<Object>();
		params.add(experimentId);
		params.add(name);
		params.add(cssFile);
		params.add(jsFile);
		params.add(screenshot);

		db.createModel(this, query, params);
	}

	public void updateDesign(Database db) {
		String query = " UPDATE designs" +
					   " SET name = ?, css_file = ?, js_file = ?, screenshot = ?" +
				   	   " WHERE id = ?";

		List<Object> params = new ArrayList<Object>();
		params.add(name);
		params.add(cssFile);
		params.add(jsFile);
		params.add(screenshot);
		params.add(id);

		db.executeUpdate(query, params);
	}

	public void loadStats(BanditWorker worker) {
		setStats(worker.getDesignStats(getId()));
	}

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
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

	public DesignStats getStats() {
		return stats;
	}

	public void setStats(DesignStats stats) {
		this.stats = stats;
	}

}
