package io.github.oliviercailloux.avignon_to_vcard;

import java.io.FileOutputStream;

import org.junit.Test;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

public class VCard {

	@Test
	public void test() throws Exception {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);

		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
		cal.set(java.util.Calendar.DAY_OF_MONTH, 25);

		VEvent christmas = new VEvent(new Date(cal.getTime()), "Christmas Day");
		christmas.getProperties().add(new Uid("-//Ben Fortuna//iCal4j 1.0//EN"));
		christmas.getProperties().getProperty(Property.UID).getParameters().add(Value.DATE);

		// initialise as an all-day event..
//		christmas.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
		FileOutputStream fout = new FileOutputStream("mycalendar.ics");

		calendar.getComponents().add(christmas);

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, fout);
//		fail();
	}

}
