package io.github.oliviercailloux.avignon_to_vcard;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.common.collect.Range;

public class TestUtils {
	private static final String INPUT_URL = "http://www.avignonleoff.com/programme/2017/mangeront-ils-s18777/";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

	public static Range<Instant> getSlot() {
		final Instant firstStart = ZonedDateTime.parse("2018-07-06T10:00:00+02:00[Europe/Paris]").toInstant();
		final Instant firstEnd = ZonedDateTime.parse("2018-07-06T11:00:00+02:00[Europe/Paris]").toInstant();
		final Range<Instant> firstSlot = Range.closed(firstStart, firstEnd);
		return firstSlot;
	}

	public Document getDocFromResource() throws IOException {
//		try (InputStream htmlStream = this.getClass().getResourceAsStream("Mangeront-2017.html")) {
		try (InputStream htmlStream = this.getClass().getResourceAsStream("Aime.html")) {
			org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlStream, StandardCharsets.UTF_8.name(), INPUT_URL);
			LOGGER.debug("Getting doc.");
			return new W3CDom().fromJsoup(jsoupDoc);
		}
	}

}
