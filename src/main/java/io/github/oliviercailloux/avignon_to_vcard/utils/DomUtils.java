package io.github.oliviercailloux.avignon_to_vcard.utils;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.google.common.collect.Lists;

public class DomUtils {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DomUtils.class);

	public static Document getDoc(URL showUrl) {
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

	public static URL getExampleUrl() {
		return getURL("http://www.example.com");
	}

	public static URL getURL(String urlStr) {
		try {
			return new URL(urlStr);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	public static URL getURL(URL context, String spec) {
		try {
			return new URL(context, spec);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	public List<Element> getElements(Node node) {
		return getSubNodes(node, Node.ELEMENT_NODE).stream().map(n -> (Element) n).collect(Collectors.toList());
	}

	public List<Node> getNodesByUrl(List<Element> nodes, Pattern pattern) {
		final List<Node> matching = Lists.newArrayList();
		for (Element node : nodes) {
			final NamedNodeMap attributes = node.getAttributes();
			final Node hrefNode = attributes.getNamedItem("href");
			if (hrefNode == null) {
				continue;
			}
			final String text = hrefNode.getTextContent();
			LOGGER.debug("Href text: {}.", text);
			final Matcher matcher = pattern.matcher(text);
			if (matcher.matches()) {
				matching.add(node);
			}
		}
		return matching;
	}

	public List<Node> getSubNodes(Node node, short nodeType) {
		final List<Node> allSubNodes = Lists.newLinkedList();
		final NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); ++i) {
			final Node child = childs.item(i);
			if (child.getNodeType() == nodeType) {
				allSubNodes.add(child);
			}
			allSubNodes.addAll(getSubNodes(child, nodeType));
		}
		return allSubNodes;
	}

	public List<Node> getTextNodes(Node node) {
		return getSubNodes(node, Node.TEXT_NODE);
	}

	public List<Matcher> searchText(List<Node> textNodes, Pattern pattern) {
		final List<Matcher> matchers = Lists.newArrayList();
		for (Node node : textNodes) {
			final String text = node.getTextContent();
			LOGGER.debug("Node text: {}.", text);
			final Matcher matcher = pattern.matcher(text);
			if (matcher.matches()) {
				matchers.add(matcher);
			}
		}
		return matchers;
	}

	public String serialize(Node node) {
		requireNonNull(node);
		DOMImplementationRegistry registry;
		try {
			registry = DOMImplementationRegistry.newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (ClassCastException e) {
			throw new IllegalStateException(e);
		}
		DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
		LSSerializer ser = impl.createLSSerializer();
		return ser.writeToString(node);
	}

}
