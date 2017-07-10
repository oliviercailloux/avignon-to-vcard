package io.github.oliviercailloux.avignon_to_vcard;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadHtml {
	private static final String INPUT_URL = "http://www.avignonleoff.com/programme/2017/mangeront-ils-s18777/";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadHtml.class);

	private Document doc;

	public void getDocFromResource() throws IOException {
		try (InputStream htmlStream = this.getClass().getResourceAsStream("out.html")) {
			org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlStream, StandardCharsets.UTF_8.name(), INPUT_URL);
			LOGGER.debug("Getting doc.");
			doc = new W3CDom().fromJsoup(jsoupDoc);
		}
	}

	@Test
	public void testReadData() throws Exception {
		getDocFromResource();
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
		getTextNodes(doc.getDocumentElement());
		textNodesUnder(node){
			  var all = [];
			  for (node=node.firstChild;node;node=node.nextSibling){
			    if (node.nodeType==3) {
					all.push(node);
				} else {
					all = all.concat(textNodesUnder(node));
				}
			  }
			  return all;
			}
		docE.cr
		LOGGER.info("Text content:\n", docE.getTextContent());
		assertEquals("Durée : 1h15",
				h2.getParentNode().getNextSibling().getFirstChild().getFirstChild().getNodeValue());

	}

	private List<Node> getTextNodes(Node node) {
		final Element docE = doc.getDocumentElement();

	}

}
