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
	public void testManual() throws Exception {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);

		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
		cal.set(java.util.Calendar.DAY_OF_MONTH, 25);

		VEvent christmas = new VEvent(new Date(cal.getTime()), "Christmas Day");
		christmas.getProperties().add(new Uid("-//Ben Fortuna//iCal4j 1.0//EN"));
//		christmas.getProperties().getProperty(Property.UID).getParameters().add(Value.DATE);

		// initialise as an all-day event..
//		christmas.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
		FileOutputStream fout = new FileOutputStream("mycalendar.ics");

		calendar.getComponents().add(christmas);

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, fout);
//		fail();
	}

	@Test
	public void testWriter() throws Exception {
		final Theater theater = Theater.from("theater", "the-addr", "Avignon", DomUtils.getExample(),
				DomUtils.getExample());
		final Range<Instant> slot = TestUtils.getSlot();
		final Show show = Show.from("show", ImmutableList.of(slot), DomUtils.getExample(), theater);
		final IcalWriter writer = new IcalWriter();
//		writer.write(show, slot, new FileOutputStream("out.ics"));
		final StringWriter dest = new StringWriter();
		writer.write(show, slot, dest);
		final String written = dest.toString();
		LOGGER.info(written);
		assertTrue(written.contains("DTSTART:20180706T080000Z"));
	}

}
