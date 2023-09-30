package de.shimplify.converter;

public final class Pair {

	private final String key;
	private String value;

	public Pair(final String key, final String value) {
		this.key = key;
		this.value = value;
	}

	public String key() {
		return key;
	}

	public String value() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}