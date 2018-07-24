package io.github.oliviercailloux.avignon_to_vcard;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.MoreCollectors;
import com.google.common.collect.Range;

import io.github.oliviercailloux.avignon_to_vcard.model.Show;
import io.github.oliviercailloux.avignon_to_vcard.model.Theater;
import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

public class ShowReader {
	private URL baseUrl;

	private final DomUtils domUtils;

	public ShowReader(URL baseUrl) {
		this.baseUrl = requireNonNull(baseUrl);
		domUtils = new DomUtils();
	}

	public Show readShow(Document source, URL url) {
		final NodeList h1s = source.getElementsByTagName("h1");
		checkArgument(h1s.getLength() == 1);
		final Node h1 = h1s.item(0);
		final String title = h1.getFirstChild().getNodeValue();
		final NodeList h2s = source.getElementsByTagName("h2");
		checkArgument(h2s.getLength() == 1, h2s.getLength());
		final Node h2 = h2s.item(0);
		final NodeList h2childs = h2.getChildNodes();
		checkArgument(h2childs.getLength() == 1);
		final Node h2span = h2childs.item(0);
		final NodeList h2spanChilds = h2span.getChildNodes();
		checkArgument(h2spanChilds.getLength() == 2);
		final Node timeNode = h2spanChilds.item(1);
		final String timeFullStr = timeNode.getNodeValue();
		checkArgument(timeFullStr.startsWith(" À "));
		final String timeStr = timeFullStr.substring(3);
//		final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
//				.withLocale(Locale.FRENCH).withZone(ZoneId.of("Europe/Paris"));
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H'h'[mm]");
		final LocalTime startTime = formatter.parse(timeStr, LocalTime::from);
		final List<Node> textNodes = domUtils.getTextNodes(source.getDocumentElement());
		final int firstDay;
		final int lastDay;
		{
			final Pattern pattern = Pattern.compile("du (?<first>[\\d]+) au (?<last>[\\d]+) juillet");
			final List<Matcher> matchers = domUtils.searchText(textNodes, pattern);
			final Matcher matcher = Iterables.getOnlyElement(matchers);
			final String firstDayTxt = matcher.group("first");
			firstDay = Integer.parseInt(firstDayTxt);
			final String lastDayTxt = matcher.group("last");
			lastDay = Integer.parseInt(lastDayTxt);
		}
		final List<Integer> relâcheDays;
		{
			final Pattern pattern = Pattern.compile("Relâches.*: ");
			final Node relâcheNode = textNodes.stream().filter((n) -> pattern.matcher(n.getTextContent()).matches())
					.collect(MoreCollectors.onlyElement());
			final String relâcheValue = relâcheNode.getParentNode().getNextSibling().getTextContent();
			checkArgument(relâcheValue.endsWith(" juillet"));
			final String relâcheDaysTxt = relâcheValue.substring(0, relâcheValue.length() - 8);
			final String[] split = relâcheDaysTxt.split(", ");
			relâcheDays = Lists.newArrayList();
			for (String relâcheDayTxt : split) {
				final int relâcheDay = Integer.parseInt(relâcheDayTxt);
				relâcheDays.add(relâcheDay);
			}
		}
		final Duration duration;
		{
			final Pattern patternLength = Pattern.compile("Durée : (?<duration>[\\dh]+)");
			final List<Matcher> matchers = domUtils.searchText(textNodes, patternLength);
			final Matcher matcher = Iterables.getOnlyElement(matchers);
			final String durationTxt = matcher.group("duration");
			final LocalTime durationAsTime = formatter.parse(durationTxt, LocalTime::from);
			duration = Duration.between(LocalTime.MIN, durationAsTime);
		}

		final List<Range<Instant>> slots = Lists.newArrayList();
		for (int day = firstDay; day <= lastDay; ++day) {
			if (relâcheDays.contains(day)) {
				continue;
			}
			final LocalDate localDate = LocalDate.of(2018, Month.JULY, day);
			final Instant start = localDate.atTime(startTime).atZone(ZoneId.of("Europe/Paris")).toInstant();
			final Instant end = start.plus(duration);
			final Range<Instant> slot = Range.closed(start, end);
			slots.add(slot);
		}

		return Show.from(title, slots, url, readTheater(source));
	}

	public Theater readTheater(Document source) {
		final Pattern patternMap = Pattern.compile("https://maps.google.fr/.*");
		final List<Node> nodes = domUtils.getSubNodes(source.getDocumentElement(), Node.ELEMENT_NODE);
		final List<Node> matching = domUtils.getNodesByUrl(nodes, patternMap);
		final Node mapNode = Iterables.getOnlyElement(matching);
		final String mapUrlTxt = mapNode.getAttributes().getNamedItem("href").getNodeValue();
		final URL mapUrl;
		try {
			mapUrl = new URL(mapUrlTxt);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		final Node addressNode = mapNode.getParentNode();
		final NodeList subAddressNodes = addressNode.getChildNodes();
		final Node address1TextNode = subAddressNodes.item(0);
		final String address = address1TextNode.getTextContent();
		final Node address2TextNode = subAddressNodes.item(2);
		final String address2 = address2TextNode.getTextContent().replace("Avignon - ", "Avignon");

		final Node theaterParNode = addressNode.getPreviousSibling().getPreviousSibling();
		final Node theaterContentNode = theaterParNode.getFirstChild();
		final String theaterUrlTxt = theaterContentNode.getAttributes().getNamedItem("href").getNodeValue();
		final URL theaterUrl;
		try {
			theaterUrl = new URL(baseUrl, theaterUrlTxt);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		final Node theaterNameTextNode = theaterContentNode.getChildNodes().item(1);
		final String rawName = domUtils.serialize(theaterNameTextNode);
		final String name = rawName.trim();
		return Theater.from(name, address, address2, theaterUrl, mapUrl);
	}

}
