package io.github.oliviercailloux.avignon_to_vcard.model;

import static java.util.Objects.requireNonNull;

import java.net.URL;

public class Theater {
	public static Theater from(String name, String addressFirstLine, String addressSecondLine, URL theaterUrl,
			URL mapUrl) {
		return new Theater(name, addressFirstLine, addressSecondLine, theaterUrl, mapUrl);
	}

	private String addressFirstLine;

	private String addressSecondLine;

	private URL mapUrl;

	private String name;

	private URL theaterUrl;

	private Theater(String name, String addressFirstLine, String addressSecondLine, URL theaterUrl, URL mapUrl) {
		this.name = requireNonNull(name);
		this.addressFirstLine = requireNonNull(addressFirstLine);
		this.addressSecondLine = requireNonNull(addressSecondLine);
		this.theaterUrl = requireNonNull(theaterUrl);
		this.mapUrl = requireNonNull(mapUrl);
	}

	public String getAddress() {
		return addressFirstLine + "\n" + addressSecondLine;
	}

	public String getAddressFirstLine() {
		return addressFirstLine;
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
