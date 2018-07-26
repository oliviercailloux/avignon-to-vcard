package io.github.oliviercailloux.avignon_to_vcard;

import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.time.Instant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;

import io.github.oliviercailloux.avignon_to_vcard.model.Show;
import io.github.oliviercailloux.avignon_to_vcard.model.Theater;
import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

public class ICalWriterTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ICalWriterTest.class);

	@Test
	public void testWriter() throws Exception {
		final Theater theater = Theater.from("theater", "the-addr", "Avignon", DomUtils.getExampleUrl(),
				DomUtils.getExampleUrl());
		final Range<Instant> slot = TestUtils.getSlot();
		final Show show = Show.from("show", ImmutableList.of(slot), DomUtils.getExampleUrl(), theater);
		final IcalWriter writer = new IcalWriter();
//		writer.write(show, slot, new FileOutputStream("out.ics"));
		final StringWriter dest = new StringWriter();
		writer.write(show, slot, dest);
		final String written = dest.toString();
		LOGGER.info(written);
		assertTrue(written.contains("DTSTART:20180706T080000Z"));
	}

}
