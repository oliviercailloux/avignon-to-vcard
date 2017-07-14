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

public class ReadHtml {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadHtml.class);

	private Document doc;

	private final DomUtils domUtils;

	public ReadHtml() {
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
		assertEquals("Mangeront-ils?", h1.getFirstChild().getNodeValue());
		final NodeList h2s = doc.getElementsByTagName("h2");
		assertEquals(1, h2s.getLength());
		final Node h2 = h2s.item(0);
		final NodeList h2childs = h2.getChildNodes();
		assertEquals(1, h2childs.getLength());
		final Node h2span = h2childs.item(0);
		final NodeList h2spanChilds = h2span.getChildNodes();
		assertEquals(2, h2spanChilds.getLength());
		final Node timeNode = h2spanChilds.item(1);
		assertEquals(" À 15h50", timeNode.getNodeValue());
		final Pattern pattern = Pattern.compile("Durée : (?<length>[\\dh]+)");
		final List<Node> textNodes = domUtils.getTextNodes(doc.getDocumentElement());
		final List<Matcher> matchers = domUtils.searchText(textNodes, pattern);
		assertEquals(1, matchers.size());
		final Matcher matcher = Iterables.getOnlyElement(matchers);
		final String readLength = matcher.group("length");
		assertEquals("1h15", readLength);
	}

}
