package io.github.oliviercailloux.avignon_to_vcard.servlets;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MoreCollectors;
import com.google.common.collect.Range;

import io.github.oliviercailloux.avignon_to_vcard.IcalWriter;
import io.github.oliviercailloux.avignon_to_vcard.model.Show;

@Path("ical/{show}/{day}")
public class IcalProvider {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(IcalProvider.class);
	private final Descriptor descriptor;
	private final IcalWriter icalWriter;

	public IcalProvider() {
		descriptor = new Descriptor();
		icalWriter = new IcalWriter();
	}

	@GET
	@Produces("text/calendar")
	public Response getIcal(@PathParam("show") String relativeShowUrl, @PathParam("day") int day)
			throws WebApplicationException {
		final Show show = descriptor.getShow(relativeShowUrl);
		final Range<Instant> requestedRange = getRange(day);
		final Range<Instant> matchingSlot = show.getSlots().stream()
				.filter(r -> requestedRange.contains(r.lowerEndpoint())).collect(MoreCollectors.onlyElement());
		final String icalString = icalWriter.toIcalString(show, matchingSlot);
		return Response.ok(icalString).header("Content-Disposition", "attachment; filename=\"" + "show.ics" + "\"") // optional
				.build();
	}

	public Range<Instant> getRange(int day) {
		final LocalDate requestedDate = LocalDate.of(2018, Month.JULY, day);
		final ZoneId paris = ZoneId.of("Europe/Paris");
		final ZonedDateTime startReq = ZonedDateTime.of(requestedDate, LocalTime.MIN, paris);
		final ZonedDateTime endReq = ZonedDateTime.of(requestedDate, LocalTime.MAX, paris);
		final Range<Instant> requestedRange = Range.closed(startReq.toInstant(), endReq.toInstant());
		return requestedRange;
	}

}
