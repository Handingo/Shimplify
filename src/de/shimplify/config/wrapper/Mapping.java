package de.shimplify.config.wrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public final class Mapping {

	private final HashMap<String, MappingEntry> map;

	public Mapping(final JsonObject mapping) {
		map = new HashMap<>();

		final Gson gson = new Gson();
		mapping.entrySet().forEach(entry -> map.put(entry.getKey(), gson.fromJson(entry.getValue().getAsJsonObject(), MappingEntry.class)));
	}

	public HashMap<String, MappingEntry> get() {
		return map;
	}

	public static Mapping load(String name) {
		if (name == null) {
			name = "teletarif";
		}

		final String path = "./config/mapping/" + name + ".json";

		try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
			return new Mapping(JsonParser.parseReader(reader).getAsJsonObject());
		} catch (IOException | JsonSyntaxException | JsonIOException e) {
			throw new RuntimeException("Could not load " + path);
		}
	}
}