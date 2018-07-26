package io.github.oliviercailloux.avignon_to_vcard;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

import io.github.oliviercailloux.avignon_to_vcard.model.Base;
import io.github.oliviercailloux.avignon_to_vcard.model.Show;
import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

public class ShowCrawler {
	private static URL getStartURL() {
		try {
			return new URL("http://www.avignonleoff.com/programme/2018/0,0,0,0,0,0,99,99,24,5,0,0,1,20,1/");
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	private final DomUtils domUtils;

	private final ShowReader showReader;

	public ShowCrawler() {
		domUtils = new DomUtils();
		showReader = new ShowReader(Base.getBaseUrl());
	}

	public void crawl() {
		List<Show> allShows = Lists.newArrayList();
		for (int pageNumber = 1; pageNumber <= 54; ++pageNumber) {
			List<Show> shows = crawl(pageNumber);
			allShows.addAll(shows);
		}
	}

	public List<Show> crawl(int pageNumber) {
		checkArgument(1 <= pageNumber);
		checkArgument(pageNumber <= 54);
		final URL pageURL = getPageURL(pageNumber);
		List<Show> shows = crawl(pageURL);
		return shows;
	}

	public List<Show> crawl(URL pageURL) {
		final Document doc = DomUtils.getDoc(pageURL);
		final List<Node> showRelativeURLNodes = domUtils.getNodesByUrl(domUtils.getElements(doc),
				Pattern.compile("/programme/2018/.*-s[0-9]+/"));
		List<Show> shows = Lists.newArrayList();
		for (Node showRelativeURLNode : showRelativeURLNodes) {
			final String showRelativeURLStr = showRelativeURLNode.getAttributes().getNamedItem("href").getTextContent();
			final URL showURL = Base.getShowUrl(showRelativeURLStr);
			final Show show = showReader.readShow(DomUtils.getDoc(showURL), showURL);
			shows.add(show);
		}
		checkState(shows.size() == 20, shows.size());
		return shows;
	}

	public URL getPageURL(int pageNumber) {
		return DomUtils.getURL(getStartURL(), "page" + String.valueOf(pageNumber));
	}
}
