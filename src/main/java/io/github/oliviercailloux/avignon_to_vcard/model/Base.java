package io.github.oliviercailloux.avignon_to_vcard.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base {
	private static final String BASE_URL = "http://www.avignonleoff.com";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(Base.class);

	public static URL getBaseUrl() {
		try {
			return new URL(BASE_URL);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	public static URL getShowUrl(String relativeShowUrl) {
		try {
			return new URL(new URL(getBaseUrl(), "programme/2018/"), relativeShowUrl);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

}
