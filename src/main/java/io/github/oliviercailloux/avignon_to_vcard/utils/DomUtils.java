package io.github.oliviercailloux.avignon_to_vcard.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

public class DomUtils {

	public List<Node> getTextNodes(Node node) {
		final List<Node> allTextNodes = Lists.newLinkedList();
		final NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); ++i) {
			final Node child = childs.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				allTextNodes.add(child);
			}
			allTextNodes.addAll(getTextNodes(child));
		}
		return allTextNodes;
	}

	public List<Matcher> searchText(List<Node> textNodes, Pattern pattern) {
		final List<Matcher> matchers = Lists.newLinkedList();
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
