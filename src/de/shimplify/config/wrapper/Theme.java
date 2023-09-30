package de.shimplify.config.wrapper;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public final record Theme(
	Color backgroundColor,
	Color contentColor,
	Color contentHoverColor,
	Color contentClickColor,
	Color fontColor,
	Color warnColor
) {
	public static Theme load(String name) {
		if (name == null) {
			name = "Dark";
		}

		try {
			final BufferedReader reader = new BufferedReader(new FileReader("./config/themes/" + name + ".json"));
			final JsonObject jsonTheme = JsonParser.parseReader(reader).getAsJsonObject();

			reader.close();

			if (jsonTheme == null) {
				return null;
			}

			return new Theme(
				color(jsonTheme.getAsJsonArray("backgroundColor")),
				color(jsonTheme.getAsJsonArray("contentColor")),
				color(jsonTheme.getAsJsonArray("contentHoverColor")),
				color(jsonTheme.getAsJsonArray("contentClickColor")),
				color(jsonTheme.getAsJsonArray("fontColor")),
				color(jsonTheme.getAsJsonArray("warnColor"))
			);

		} catch (IOException | JsonSyntaxException | JsonIOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Color color(final JsonArray array) {
		if (array == null || array.size() < 3) {
			return Color.black;
		}

		return new Color(
			Math.min(Math.max(array.get(0).getAsInt(), 0), 255),
			Math.min(Math.max(array.get(1).getAsInt(), 0), 255),
			Math.min(Math.max(array.get(2).getAsInt(), 0), 255)
		);
	}
}