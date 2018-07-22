package io.github.oliviercailloux.avignon_to_vcard.utils;

import java.io.InputStream;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import com.google.common.collect.ImmutableMap;

public class JsonUtils {
	public static String asPrettyString(JsonStructure json) {
		StringWriter stringWriter = new StringWriter();
		JsonWriterFactory writerFactory = Json
				.createWriterFactory(ImmutableMap.of(JsonGenerator.PRETTY_PRINTING, true));
		try (JsonWriter jsonWriter = writerFactory.createWriter(stringWriter)) {
			jsonWriter.write(json);
		}
		return stringWriter.toString();
	}

	public static JsonObject read(InputStream inputStream) {
		JsonObject json;
		try (JsonReader jr = Json.createReader(inputStream)) {
			json = jr.readObject();
		}
		return json;
	}
}
