package io.github.oliviercailloux.avignon_to_vcard.servlets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("hello")
public class Hello {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(Hello.class);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		LOGGER.info("Being polite.");
		return "Hello, world.";
	}
}