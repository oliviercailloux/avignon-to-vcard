package io.github.oliviercailloux.avignon_to_vcard.servlets;

import java.net.URL;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import io.github.oliviercailloux.avignon_to_vcard.ShowReader;
import io.github.oliviercailloux.avignon_to_vcard.model.Base;
import io.github.oliviercailloux.avignon_to_vcard.model.Show;
import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

@Path("descr/{show}")
public class Descriptor {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(Descriptor.class);

	// @GET
//	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getDescriptionAsHtml(@PathParam("show") String relativeShowUrl) throws WebApplicationException {
		final Show show = getShow(relativeShowUrl);
		return show.toJson();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public JsonObject getDescriptionAsJson(@PathParam("show") String relativeShowUrl) throws WebApplicationException {
		final Show show = getShow(relativeShowUrl);
		return show.toJson();
	}

	public Show getShow(String relativeShowUrl) {
		final URL showUrl = Base.getShowUrl(relativeShowUrl);
		final Document doc = DomUtils.getDoc(showUrl);
		final ShowReader reader = new ShowReader(Base.getBaseUrl());
		final Show show = reader.readShow(doc, showUrl);
		return show;
	}

}
