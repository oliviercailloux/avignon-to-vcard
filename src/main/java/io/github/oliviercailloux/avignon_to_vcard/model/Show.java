package io.github.oliviercailloux.avignon_to_vcard.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.time.Instant;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

public class Show {
	public static Show from(String title, List<Range<Instant>> slots, URL url, Theater theater) {
		return new Show(title, slots, url, theater);
	}

	private List<Range<Instant>> slots;

	private Theater theater;

	private String title;

	private URL url;

	private Show(String title, List<Range<Instant>> slots, URL url, Theater theater) {
		this.title = requireNonNull(title);
		this.slots = requireNonNull(slots);
		for (Range<Instant> slot : slots) {
			checkArgument(slot.hasLowerBound());
			checkArgument(slot.lowerBoundType() == BoundType.CLOSED);
			checkArgument(slot.hasUpperBound());
			checkArgument(slot.upperBoundType() == BoundType.CLOSED);
		}
		this.url = requireNonNull(url);
		this.theater = requireNonNull(theater);
	}

	public List<Range<Instant>> getSlots() {
		return slots;
	}

	public Theater getTheater() {
		return theater;
	}

	public String getTitle() {
		return title;
	}

	public JsonObject toJson() {
		final JsonArrayBuilder slotsJsonBuilder = Json.createArrayBuilder();
		for (Range<Instant> slot : slots) {
			final JsonObjectBuilder slotBuilder = Json.createObjectBuilder();
			slotBuilder.add("start", slot.lowerEndpoint().toString());
			slotBuilder.add("end", slot.upperEndpoint().toString());
			slotsJsonBuilder.add(slotBuilder);
		}

		final JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("title", getTitle());
		builder.add("theater", theater.getName());
		builder.add("address", theater.getAddress());
		builder.add("map", theater.getMapUrl().toString());
		builder.add("theater-url", theater.getTheaterUrl().toString());
		builder.add("show-url", url.toString());
		builder.add("slots", slotsJsonBuilder);
		return builder.build();
	}
}
