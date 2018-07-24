package io.github.oliviercailloux.avignon_to_vcard;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.time.Instant;
import java.util.Date;

import com.google.common.collect.Range;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import io.github.oliviercailloux.avignon_to_vcard.model.Show;

public class IcalWriter {
	public ICalendar toIcal(Show show, Range<Instant> slot) {
		requireNonNull(show);
		requireNonNull(slot);
		final ICalendar ical = new ICalendar();
		final VEvent event = new VEvent();
		event.setSummary(show.getTitle());
		event.setDateStart(Date.from(slot.lowerEndpoint()));
		event.setDateEnd(Date.from(slot.upperEndpoint()));
		event.setLocation(show.getTheater().getName() + " : " + show.getTheater().getAddressFirstLine());
		event.setDescription(show.getUrl().toString() + "\n" + show.getTheater().getTheaterUrl().toString() + "\n"
				+ show.getTheater().getMapUrl().toString());
		ical.addEvent(event);
		return ical;
	}

	public String toIcalString(Show show, Range<Instant> slot) {
		return Biweekly.write(toIcal(show, slot)).go();
	}

	public void write(Show show, Range<Instant> slot, OutputStream dest) throws IOException {
		Biweekly.write(toIcal(show, slot)).go(dest);
	}

	public void write(Show show, Range<Instant> slot, Writer dest) throws IOException {
		Biweekly.write(toIcal(show, slot)).go(dest);
	}
}
