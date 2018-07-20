package io.github.oliviercailloux.avignon_to_vcard;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

import com.google.common.collect.Range;

public class Show {
	public static Show from(String title, Range<Instant> timeSlot, Theater theater) {
		return new Show(title, timeSlot, theater);
	}

	private Theater theater;

	private Range<Instant> timeSlot;

	private String title;

	private Show(String title, Range<Instant> timeSlot, Theater theater) {
		this.title = requireNonNull(title);
		this.timeSlot = requireNonNull(timeSlot);
		this.theater = requireNonNull(theater);
	}

	public Theater getTheater() {
		return theater;
	}

	public Range<Instant> getTimeSlot() {
		return timeSlot;
	}

	public String getTitle() {
		return title;
	}
}
