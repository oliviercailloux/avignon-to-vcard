package io.github.oliviercailloux.avignon_to_vcard;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.w3c.dom.Document;

import com.google.common.collect.Iterables;

import io.github.oliviercailloux.avignon_to_vcard.utils.DomUtils;

public class DomSearchTest {

	private final DomUtils domUtils;

	public DomSearchTest() {
		domUtils = new DomUtils();
	}

	@Test
	public void readPattern() throws Exception {
		final Document doc = new TestUtils().getDocFromResource();
		final Pattern pattern = Pattern.compile("Durée : (?<length>[\\dh]+)");
		final List<Matcher> matchers = domUtils.searchText(domUtils.getTextNodes(doc), pattern);
		assertEquals(1, matchers.size());
		final Matcher matcher = Iterables.getOnlyElement(matchers);
		final String readLength = matcher.group("length");
		assertEquals("1h", readLength);
	}

}
