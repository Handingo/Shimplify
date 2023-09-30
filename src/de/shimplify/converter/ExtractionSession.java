package de.shimplify.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.shimplify.config.Config;
import de.shimplify.config.wrapper.CustomInputCSV;
import de.shimplify.config.wrapper.Filter;
import de.shimplify.config.wrapper.MappingEntry;

public final class ExtractionSession {

	private final ExtractionSessionManager manager;

	public ExtractionSession(final ExtractionSessionManager manager, final String link, final Callback callback) {
		this.manager = manager;

		if (!link.startsWith("http")) {
			callback.execute("Found invalid link: " + link);
			return;
		}

		final Document document;

		try {
			document = Jsoup.connect(link).get();
		} catch (final IOException | IllegalArgumentException e) {
			callback.execute("Couldn't parse document from given link '" + link + "': " + String.join(".\n", e.getMessage().split("\\. ")));
			return;
		}

		final Filter filter = manager.getFilter();
		final HashMap<String, MappingEntry> mapping = manager.getMapping().get();
		final Set<String> registeredKeys = new HashSet<>();

		final ArrayList<Pair> pairs = new ArrayList<>();
		final Elements container = document.select(filter.containerSelector());

		for (final Element entry : container.select(filter.entrySelector())) {
			final Element keyElement = entry.selectFirst(filter.keySelector());

			if (keyElement == null) {
				continue;
			}

			final String key = keyElement.text();

			if (key == null || key.length() < 1 || !mapping.containsKey(key)) {
				continue;
			}

			final Element valueElement = entry.selectFirst(filter.valueSelector());

			if (valueElement == null) {
				continue;
			}

			final String value = valueElement.text();

			if (value == null) {
				continue;
			}

			if (!registeredKeys.contains(key)) {
				pairs.add(new Pair(key, value));
				registeredKeys.add(key);
				continue;
			}

			if (value.length() < 1) {
				continue;
			}

			// append the value to the entrys value whose key is already registered
			concatenation: {
				for (final Pair pair : pairs) {
					if (!pair.key().equals(key)) {
						continue;
					}

					for (final String valueEntry : pair.value().split(", ")) {
						if (valueEntry.equals(value)) {
							// in case the value entry already contains the new value part, don't append it
							break concatenation;
						}
					}

					pair.setValue(pair.value().concat(", " + value));
					break;
				}
			}
		}

		final Element productNameElement = document.selectFirst(filter.productNameSelector());
		final String productName = productNameElement != null ? productNameElement.text() : UUID.randomUUID().toString();
		final String brand = productName != null && productName.strip().length() > 0 ? productName.split(" ")[0] : "unknown";
		String error = null;

		if (manager.shouldConvertToCSV()) {
			error = convertToCSV(pairs, brand, productName, mapping);
		}

		if (manager.shouldConvertToHTML()) {
			final String htmlError = convertToHTML(pairs, brand, productName, mapping);

			if (htmlError != null) {
				error = error != null ? error + " / " + htmlError : htmlError;
			}
		}

		callback.execute(error);
	}

	private String convertToCSV(final ArrayList<Pair> pairs, final String brand, final String productName, final HashMap<String, MappingEntry> mapping) {
		final File destinationFile = new File(manager.getDestinationDirectory(), productName + ".csv");

		if (!destinationFile.exists()) {
			try {
				destinationFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return "Couldn't create new CSV file '" + productName + ".csv'";
			}
		}

		final CustomInputCSV customInput = manager.getCustomInputCSV();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile, StandardCharsets.UTF_8))) {
			writer.append("Hersteller;Tag;Tag 2;Aktiv / Inaktiv;Hauptvariante;ItemID;VarID;VarName;Eigenschaftgruppe;Eigenschaft;ID;Wert;Sprache");
			writer.newLine();

			for (final Pair pair : pairs) {
				final MappingEntry mappingAttributes = mapping.get(pair.key());

				if (mappingAttributes == null) {
					continue;
				}

				addToCSV(writer, brand, productName, customInput, mappingAttributes, pair.value(), false);

				if (mappingAttributes.high() != null && mappingAttributes.high().length() > 0) {
					addToCSV(writer, brand, productName, customInput, mappingAttributes, pair.value(), true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Couldn't write to CSV file '" + productName + ".csv'.\nIs the file opened in another program?";
		}

		return null;
	}

	// This method reminds of a skyscraper, but due to the frontend not handling "(high)" on its own...
	private void addToCSV(
			final BufferedWriter writer,
			final String brand,
			final String productName,
			final CustomInputCSV customInput,
			final MappingEntry mappingAttributes,
			final String value,
			final boolean high
	) throws IOException {
		if (high) {
			writer.append(String.format("%s;%s;%s;%d;%d;%d;%d;%s;%s;%s;%d;%s;%s",
				brand,
				customInput.tag(),
				customInput.tag2(),
				customInput.active(),
				customInput.mainVariant(),
				customInput.itemID(),
				customInput.variantID(),
				productName,
				mappingAttributes.group(),
				mappingAttributes.high(),
				mappingAttributes.highID(),
				value,
				customInput.language()
			));
		} else {
			writer.append(String.format("%s;%s;%s;%d;%d;%d;%d;%s;%s;%s;%d;%s;%s",
				brand,
				customInput.tag(),
				customInput.tag2(),
				customInput.active(),
				customInput.mainVariant(),
				customInput.itemID(),
				customInput.variantID(),
				productName,
				mappingAttributes.group(),
				mappingAttributes.key(),
				mappingAttributes.id(),
				value,
				customInput.language()
			));
		}
		
		writer.newLine();
	}

	private String convertToHTML(final ArrayList<Pair> pairList, final String brand, final String productName, final HashMap<String, MappingEntry> mapping) {
		final StringBuilder htmlBuilder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(Config.get().getTemplateHTML(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				htmlBuilder.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed to read ./config/html/template.html";
		}

		final HashMap<String, String> data = new HashMap<>(pairList.size());
		pairList.forEach(pair -> data.put(pair.key(), pair.value()));

		final ArrayList<SortableEntry> sortedMapping = new ArrayList<>();
		mapping.entrySet().forEach(entry -> sortedMapping.add(new SortableEntry(entry.getKey(), entry.getValue())));
		Collections.sort(sortedMapping);

		final StringBuilder dataBuilder = new StringBuilder();

		String lastGroup = null;

		for (final SortableEntry entry : sortedMapping) {
			final String value = data.get(entry.key);

			if (value == null) {
				continue;
			}

			final MappingEntry mappingEntry = entry.mappingEntry;
			final String group = mappingEntry.group();

			if (!group.equals(lastGroup)) {
				if (lastGroup != null) {
					dataBuilder.append("\n</ul>\n<hr>");
				}

				dataBuilder.append("\n<h3>" + group + "</h3>\n<ul>");
			}

			dataBuilder.append("\n\t<li><strong>" + mappingEntry.key() + ":</strong> " + value + "</li>");
			lastGroup = group;
		}

		dataBuilder.append("\n</ul>");

		final File destination = new File(manager.getDestinationDirectory(), productName + ".html");

		if (!destination.exists()) {
			try {
				destination.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return "Couldn't create new HTML file '" + productName + ".html'";
			}
		}

		final String html = htmlBuilder.toString().replace("<!-- NAME -->", productName);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination, StandardCharsets.UTF_8))) {
			writer.write(html.replace("<!-- DATA -->", dataBuilder.toString()));
		} catch (IOException e) {
			e.printStackTrace();
			return "Couldn't write to HTML file '" + productName + ".html'";
		}

		return null;
	}

	private static final record SortableEntry(String key, MappingEntry mappingEntry) implements Comparable<SortableEntry> {
		@Override
		public int compareTo(final SortableEntry entry) {
			return mappingEntry.id() - entry.mappingEntry.id();
		}
	}
}