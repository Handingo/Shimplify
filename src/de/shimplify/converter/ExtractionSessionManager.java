package de.shimplify.converter;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import de.shimplify.config.Config;
import de.shimplify.config.wrapper.CustomInputCSV;
import de.shimplify.config.wrapper.Filter;
import de.shimplify.config.wrapper.Mapping;

public final class ExtractionSessionManager {

	private final Filter filter;
	private final Mapping mapping;
	private final boolean convertToCSV, convertToHTML;
	private final CustomInputCSV customInputCSV;
	private final File destinationDirectory;

	public ExtractionSessionManager(final String[] links, final AtomicInteger finishedSessions, final File destinationDirectory, final Callback callback) {
		final Config config = Config.get();
		final Properties defaults = config.getDefaults();

		filter = Filter.load(defaults.getProperty("filter", "teletarif"));
		mapping = Mapping.load(defaults.getProperty("mapping", "teletarif"));
		convertToCSV = Boolean.parseBoolean(defaults.getProperty("convert-to-csv", "true"));
		convertToHTML = Boolean.parseBoolean(defaults.getProperty("convert-to-html", "true"));
		customInputCSV = config.loadCustomInputCSV();
		this.destinationDirectory = destinationDirectory;

		final int maxConcurrentConnections = Integer.parseInt(defaults.getProperty("max-concurrent-connections", "4"));
		final int threads = Math.max(Math.min(maxConcurrentConnections, Runtime.getRuntime().availableProcessors() - 1), 1);
		final ExecutorService threadPool = Executors.newFixedThreadPool(threads);

		for (final String link : links) {
			threadPool.execute(() -> new ExtractionSession(this, link, (error) -> {
				if (error != null) {
					callback.execute(error);
				}

				if (finishedSessions.incrementAndGet() >= links.length) {
					callback.execute(null);
				}
			}));
		}
	}

	public Filter getFilter() {
		return filter;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public boolean shouldConvertToCSV() {
		return convertToCSV;
	}

	public boolean shouldConvertToHTML() {
		return convertToHTML;
	}

	public CustomInputCSV getCustomInputCSV() {
		return customInputCSV;
	}

	public File getDestinationDirectory() {
		return destinationDirectory;
	}
}