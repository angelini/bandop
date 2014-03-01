package com.mcgill.bandop;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {

	public Application() {
		packages("org.glassfish.jersey.examples.jackson;com.mcgill.bandop");
        register(JacksonConfigurator.class);
		register(JacksonFeature.class);
	}

}
