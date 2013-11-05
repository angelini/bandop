package com.mcgill.bandop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.FixedStringSaltGenerator;

public class Bandop {

	// Development security and deployment constants
	// In production fetch these values from a configuration file
	// kept outside of version control

	static final int PORT = 8080;

	static final String DB_CONNECTION = "jdbc:postgresql://localhost/bandop";
	static final String DB_USER = "bandop";
	static final String DB_PASS = "bandop";

	static final String ENCRYPTION_SALT = "mEu0lAhHNOYV8xOOefBoi72ZP3L5sbQoybLrwlSs";
	static final String ENCRYPTION_KEY = "S3KR3T";

	public static void main( String[] args ) {
		Server server = new Server(8080);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
		jerseyServlet.setInitOrder(1);
		jerseyServlet.setInitParameter("javax.ws.rs.Application", "com.mcgill.bandop.Application");

		ServletHolder staticServlet = context.addServlet(DefaultServlet.class, "/*");
		staticServlet.setInitParameter("resourceBase", "src/main/app");
		staticServlet.setInitParameter("pathInfoOnly", "true");

		try {
			context.setAttribute("db", connectToDatabase());
			context.setAttribute("encryptor", buildEncryptor());

			server.start();
			server.join();

		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

	private static Database connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASS);

		return new Database(conn);
	}

	private static StandardPBEStringEncryptor buildEncryptor() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

		FixedStringSaltGenerator saltGenerator = new FixedStringSaltGenerator();
		saltGenerator.setSalt(ENCRYPTION_SALT);

		encryptor.setSaltGenerator(saltGenerator);
		encryptor.setPassword(ENCRYPTION_KEY);

		return encryptor;
	}

}
