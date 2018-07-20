package io.github.oliviercailloux.avignon_to_vcard;

import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Iterables;

import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

public class ShowReader {
	private URL baseUrl;

	private final DomUtils domUtils;

	public ShowReader(URL baseUrl) {
		this.baseUrl = requireNonNull(baseUrl);
		domUtils = new DomUtils();
	}

	public Theater readTheater(Document source) {
		final Pattern patternMap = Pattern.compile("https://maps.google.fr/.*");
		final List<Node> nodes = domUtils.getSubNodes(source.getDocumentElement(), Node.ELEMENT_NODE);
		final List<Node> matching = domUtils.getNodesByUrl(nodes, patternMap);
		final Node mapNode = Iterables.getOnlyElement(matching);
		final String mapUrlTxt = mapNode.getAttributes().getNamedItem("href").getNodeValue();
		final URL mapUrl;
		try {
			mapUrl = new URL(mapUrlTxt);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		final Node addressNode = mapNode.getParentNode();
		final NodeList subAddressNodes = addressNode.getChildNodes();
		final Node address1TextNode = subAddressNodes.item(0);
		final String address = address1TextNode.getTextContent();
		final Node address2TextNode = subAddressNodes.item(2);
		final String address2 = address2TextNode.getTextContent().replace("Avignon - ", "Avignon");

		final Node theaterParNode = addressNode.getPreviousSibling().getPreviousSibling();
		final Node theaterContentNode = theaterParNode.getFirstChild();
		final String theaterUrlTxt = theaterContentNode.getAttributes().getNamedItem("href").getNodeValue();
		final URL theaterUrl;
		try {
			theaterUrl = new URL(baseUrl, theaterUrlTxt);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		final Node theaterNameTextNode = theaterContentNode.getChildNodes().item(1);
		final String rawName = domUtils.save(theaterNameTextNode);
		final String name = rawName.trim();
		return Theater.from(name, address + "\n" + address2, theaterUrl, mapUrl);
	}
}
