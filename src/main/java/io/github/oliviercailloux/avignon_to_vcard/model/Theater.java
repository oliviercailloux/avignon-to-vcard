package io.github.oliviercailloux.avignon_to_vcard.model;

import static java.util.Objects.requireNonNull;

import java.net.URL;

public class Theater {
	public static Theater from(String name, String address, URL theaterUrl, URL mapUrl) {
		return new Theater(name, address, theaterUrl, mapUrl);
	}

	private String address;

	private URL mapUrl;

	private String name;

	private URL theaterUrl;

	private Theater(String name, String address, URL theaterUrl, URL mapUrl) {
		this.name = requireNonNull(name);
		this.address = requireNonNull(address);
		this.theaterUrl = requireNonNull(theaterUrl);
		this.mapUrl = requireNonNull(mapUrl);
	}

	public String getAddress() {
		return address;
	}

	public URL getMapUrl() {
		return mapUrl;
	}

	public String getName() {
		return name;
	}

	public URL getTheaterUrl() {
		return theaterUrl;
	}
}
