package de.shimplify.config.wrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final record Filter(
	String productNameSelector,
	String containerSelector,
	String entrySelector,
	String keySelector,
	String valueSelector
) {
	public static Filter load(String name) {
		if (name == null) {
			name = "teletarif";
		}

		try {
			final BufferedReader reader = new BufferedReader(new FileReader("./config/filter/" + name + ".properties"));
			final Properties filter = new Properties();

			filter.load(reader);
			reader.close();

			return new Filter(
				filter.getProperty("product-name-selector", "h1"),
				filter.getProperty("container-selector", "table"),
				filter.getProperty("entry-selector", "tr"),
				filter.getProperty("key-selector", "th"),
				filter.getProperty("value-selector", "td")
			);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}