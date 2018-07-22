package io.github.oliviercailloux.avignon_to_vcard.servlets;

import java.net.URL;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import io.github.oliviercailloux.avignon_to_vcard.ShowReader;
import io.github.oliviercailloux.avignon_to_vcard.model.Base;
import io.github.oliviercailloux.avignon_to_vcard.model.Show;

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

	private Document getDoc(final URL showUrl) {
		org.jsoup.nodes.Document jsoupDoc;
		try {
			LOGGER.info("Fetching {}.", showUrl);
			jsoupDoc = Jsoup.connect(showUrl.toString()).timeout(5 * 1000).get();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
		LOGGER.debug("Getting doc.");
		final Document doc = new W3CDom().fromJsoup(jsoupDoc);
		return doc;
	}

	private Show getShow(String relativeShowUrl) {
		final Base base = new Base();
		final URL showUrl = base.getShowUrl(relativeShowUrl);
		final Document doc = getDoc(showUrl);
		final ShowReader reader = new ShowReader(base.getBaseUrl());
		final Show show = reader.readShow(doc, showUrl);
		return show;
	}

}
