package io.github.oliviercailloux.avignon_to_vcard.servlets;

import static org.junit.Assert.assertEquals;

import javax.json.JsonObject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.avignon_to_vcard.utils.JsonUtils;

public class DescriptorTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DescriptorTest.class);

	@Test
	public void testShow() {
		final JsonObject jsonShow = new Descriptor().getDescriptionAsJson("je-t-aime-papa-mais-merci-d-etre-mort-s23286");
		final JsonObject expected = JsonUtils.read(getClass().getResourceAsStream("aime.json"));
		LOGGER.debug("Res: \n{}\n.", JsonUtils.asPrettyString(jsonShow));
		assertEquals(expected, jsonShow);
	}

}
