package io.github.oliviercailloux.avignon_to_vcard;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Iterables;

import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

public class ReadHtmlTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadHtmlTest.class);

	private Document doc;

	private final DomUtils domUtils;

	public ReadHtmlTest() {
		domUtils = new DomUtils();
	}

	@Test
	public void testReadData() throws Exception {
		doc = new TestUtils().getDocFromResource();
		Element docE = doc.getDocumentElement();
		assertEquals("html", docE.getTagName());
		final NodeList h1s = doc.getElementsByTagName("h1");
		assertEquals(1, h1s.getLength());
		final Node h1 = h1s.item(0);
		assertEquals("Je t'aime papa mais...Merci d'être mort !", h1.getFirstChild().getNodeValue());
		final NodeList h2s = doc.getElementsByTagName("h2");
		assertEquals(1, h2s.getLength());
		final Node h2 = h2s.item(0);
		final NodeList h2childs = h2.getChildNodes();
		assertEquals(1, h2childs.getLength());
		final Node h2span = h2childs.item(0);
		final NodeList h2spanChilds = h2span.getChildNodes();
		assertEquals(2, h2spanChilds.getLength());
		final Node timeNode = h2spanChilds.item(1);
		assertEquals(" À 10h00", timeNode.getNodeValue());
		{
			final Pattern patternLength = Pattern.compile("Durée : (?<length>[\\dh]+)");
			final List<Node> textNodes = domUtils.getTextNodes(doc.getDocumentElement());
			final List<Matcher> matchers = domUtils.searchText(textNodes, patternLength);
			assertEquals(1, matchers.size());
			final Matcher matcher = Iterables.getOnlyElement(matchers);
			final String readLength = matcher.group("length");
			assertEquals("1h", readLength);
		}
	}

	@Test
	public void testReadData2() throws Exception {
		final TestUtils testUtils = new TestUtils();
		doc = testUtils.getDocFromResource();
		final ShowReader reader = new ShowReader(testUtils.getBaseUrl());
		final Theater theater = reader.readTheater(doc);
		assertEquals("11 • GILGAMESH BELLEVILLE", theater.getName());
		assertEquals("11, bd Raspail\n84000 - Avignon", theater.getAddress());
		assertEquals("https://maps.google.fr/maps?q=43.9442326+4.80408790000001", theater.getMapUrl().toString());
		assertEquals("http://www.avignonleoff.com/programme/2018/11-gilgamesh-belleville-t2074/",
				theater.getTheaterUrl().toString());

	}

}
