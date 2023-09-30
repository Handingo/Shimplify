package de.shimplify.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.shimplify.Shimplify;
import de.shimplify.config.wrapper.CustomInputCSV;

public enum Config {

	INSTANCE;

	public static Config get() {
		return INSTANCE;
	}

	private final Properties defaults;
	private final File csvInputFile, htmlTemplate;

	private Config() {
		copyConfigs("config", "./config");

		defaults = new Properties();

		try (BufferedReader reader = new BufferedReader(new FileReader(loadFile("./config", "defaults.properties")))) {
			defaults.load(reader);
		} catch (IOException e) {
			throw new RuntimeException("The config file defaults.properties is probably missing in ./config/");
		}

		csvInputFile = loadFile("./config/csv", "custom_input.properties");
		htmlTemplate = loadFile("./config/html", "template.html");
	}

	private File loadFile(final String directory, final String fileName) {
		final File dir = new File(directory);
		final File file = new File(dir, fileName);

		if (file.exists()) {
			return file;
		}

		try {
			if (!dir.mkdirs() || !file.createNewFile()) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not find or create specified file '" + directory + "/" + fileName + "'");
		}

		return file;
	}

	private void copyConfigs(final String defaultsPath, final String destinationPath) {
		final File destination = new File(destinationPath);

		if (destination.exists()) {
			return;
		}

		destination.mkdirs();

		final ZipFile jar;

		try {
			jar = new ZipFile(Shimplify.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		final Enumeration<? extends ZipEntry> jarEntries = jar.entries();

		while (jarEntries.hasMoreElements()) {
			final ZipEntry entry = jarEntries.nextElement();

			if (!entry.getName().startsWith(defaultsPath)) {
				continue;
			}

			final String relativePath = entry.getName().substring(defaultsPath.length());
			final File entryDestination = new File(destination, relativePath);

			if (entry.isDirectory()) {
				entryDestination.mkdirs();
				continue;
			}

			try (InputStream input = jar.getInputStream(entry)) {
				Files.copy(input, entryDestination.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			jar.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getDefaults() {
		return defaults;
	}

	public CustomInputCSV loadCustomInputCSV() {
		final Properties props = new Properties();

		try {
			props.load(new BufferedReader(new FileReader(csvInputFile)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new CustomInputCSV(
			props.getProperty("tag", "Smartphone"),
			props.getProperty("tag2", ""),
			Integer.parseInt(props.getProperty("active", "1")),
			Integer.parseInt(props.getProperty("main-variant", "0")),
			Integer.parseInt(props.getProperty("item-id", "15000")),
			Integer.parseInt(props.getProperty("variant-id", "46000")),
			props.getProperty("language", "de")
		);
	}

	public File getTemplateHTML() {
		return htmlTemplate;
	}
}