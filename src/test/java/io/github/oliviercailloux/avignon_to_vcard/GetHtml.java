package io.github.oliviercailloux.avignon_to_vcard;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.nio.charset.Charset;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSParser;

public class GetHtml {

	private static final String INPUT_URL = "http://www.avignonleoff.com/programme/2018/je-t-aime-papa-mais-merci-d-etre-mort-s23286/";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GetHtml.class);

	@Test
	public void testJSoup() throws Exception {
		org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(new URL(INPUT_URL), 5 * 1000);
		LOGGER.debug("Getting doc.");
		Document doc = new W3CDom().fromJsoup(jsoupDoc);
		Element docE = doc.getDocumentElement();
		assertEquals("html", docE.getTagName());
	}

//	@Test(expected = BadRequestException.class)
	public void testRestToDom() throws Exception {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(INPUT_URL);
		final Builder req = target.request();
		LOGGER.info("Obtaining response.");
		final Response response = req.get();
		LOGGER.info("MT: {}.", response.getMediaType());
		LOGGER.info("Obtaining entity.");
		final Object entity = response.getEntity();
		LOGGER.info("Entity: {}.", entity);
		LOGGER.info("Parsing response.");
		req.get(Document.class);
		LOGGER.info("Parsing response html.");
		final Builder reqHtml = target.request(MediaType.TEXT_HTML);
		reqHtml.get(Document.class);
		client.close();
	}

//	@Test(expected = LSException.class)
	public void testStdDom() throws Exception {
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
		LSParser builder = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		LOGGER.info("Attempting parsing.");
		builder.parseURI(INPUT_URL);
	}

	public void testViaResponse() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(INPUT_URL);
		LOGGER.info("Getting response.");
		Response response = target.request(MediaType.TEXT_HTML).get(Response.class);
		final MediaType mediaType = response.getMediaType();
		final Charset charset = Charset.forName(mediaType.getParameters().get("charset"));
		LOGGER.info("Received charset: {}.", charset);
		final String result = response.readEntity(String.class);
		LOGGER.info("Received entity:\n{}", result);
		client.close();
//		Files.write(result, new File("out.html"), StandardCharsets.UTF_8);
	}
}
