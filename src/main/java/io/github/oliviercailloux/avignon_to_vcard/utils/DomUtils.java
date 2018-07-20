package io.github.oliviercailloux.avignon_to_vcard.utils;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public List<Node> getNodesByUrl(List<Node> nodes, Pattern pattern) {
		final List<Node> matching = Lists.newArrayList();
		for (Node node : nodes) {
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

	public String save(Node node) {
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

	public List<Matcher> searchText(List<Node> textNodes, Pattern pattern) {
		final List<Matcher> matchers = Lists.newArrayList();
		for (Node node : textNodes) {
			final String text = node.getTextContent();
			final Matcher matcher = pattern.matcher(text);
			if (matcher.matches()) {
				matchers.add(matcher);
			}
		}
		return matchers;
	}

}
