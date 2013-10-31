package com.mcgill.bandop;

import java.sql.Connection;
import java.sql.DriverManager;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Bandop {

	static final int PORT = 8080;
	static final String DB_CONNECTION = "jdbc:postgresql://localhost/bandop";
	static final String DB_USER = "bandop";
	static final String DB_PASS = "bandop";

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
        	Class.forName("org.postgresql.Driver");
        	Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASS);
        	context.setAttribute("db", new Database(conn));

            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

}
