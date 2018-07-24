package io.github.oliviercailloux.avignon_to_vcard;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

public class ShowCrawler {
	private static URL getStartURL() {
		try {
			return new URL("http://www.avignonleoff.com/programme/2018/0,0,0,0,0,0,99,99,24,5,0,0,1,20,1/");
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	public void crawl() {
		final DomUtils domUtils = new DomUtils();
		final Document doc = DomUtils.getDoc(getStartURL());
		final List<Node> showUrls = domUtils.getNodesByUrl(domUtils.getTextNodes(doc),
				Pattern.compile("http://www.avignonleoff.com/programme/2018/pratt-respire-encore-s21853/"));

	}
}
