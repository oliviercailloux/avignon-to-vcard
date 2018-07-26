package io.github.oliviercailloux.avignon_to_vcard;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.avignon_to_vcard.model.Show;

public class ShowCrawlerTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowCrawlerTest.class);

	@Test
	public void test() {
		final ShowCrawler crawler = new ShowCrawler();
		final List<Show> showsPage = crawler.crawl(1);
		LOGGER.info("Shows page {}: {}.", 1, showsPage);
	}

}
