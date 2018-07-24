package io.github.oliviercailloux.avignon_to_vcard;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.time.Instant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Range;

import io.github.oliviercailloux.avignon_to_vcard.model.Base;
import io.github.oliviercailloux.avignon_to_vcard.model.Show;
import io.github.oliviercailloux.avignon_to_vcard.model.Theater;

public class ReadHtmlTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadHtmlTest.class);

	private Document doc;

	@Test
	public void readShow() throws Exception {
		final TestUtils testUtils = new TestUtils();
		doc = testUtils.getDocFromResource();
		final ShowReader reader = new ShowReader(Base.getBaseUrl());
		final Show show = reader.readShow(doc, new URL("http://example.com"));
		assertEquals("Je t'aime papa mais...Merci d'être mort !", show.getTitle());
		final Range<Instant> firstSlot = TestUtils.getSlot();
		assertEquals(firstSlot, show.getSlots().iterator().next());
		expected(show.getTheater());
	}

	@Test
	public void readTheater() throws Exception {
		final TestUtils testUtils = new TestUtils();
		doc = testUtils.getDocFromResource();
		final ShowReader reader = new ShowReader(Base.getBaseUrl());
		final Theater theater = reader.readTheater(doc);
		expected(theater);

	}

	@Test
	public void testReadData() throws Exception {
		doc = new TestUtils().getDocFromResource();
		Element docE = doc.getDocumentElement();
		assertEquals("html", docE.getTagName());
	}

	private void expected(Theater theater) {
		assertEquals("11 • GILGAMESH BELLEVILLE", theater.getName());
		assertEquals("11, bd Raspail\n84000 - Avignon", theater.getAddress());
		assertEquals("https://maps.google.fr/maps?q=43.9442326+4.80408790000001", theater.getMapUrl().toString());
		assertEquals("http://www.avignonleoff.com/programme/2018/11-gilgamesh-belleville-t2074/",
				theater.getTheaterUrl().toString());
	}

}
