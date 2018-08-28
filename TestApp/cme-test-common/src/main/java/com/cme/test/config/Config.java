package com.cme.test.config;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {

	private final Properties props = new Properties();

	private final static String PASSWORD_KEY = "password";
	private final static String HIDE_PASSWORD = "******";
	private final static String EQUAL_OPERATOR = "=";
	private final static String NEW_LINE_CHAR = "\n";
	
	public void load(ConfigLoader... configLoaders) throws IOException {
		for(ConfigLoader cf : configLoaders) {
			props.putAll(cf.load());
		}
	}

	public Properties getProps() {
		return props;
	}

	public String getString(String key) {
		return props.getProperty(key);
	}

	public Number getNumber(String key) throws NumberFormatException {

		String value = props.getProperty(key);

		if (value == null) {
			return null;
		}

		if (value.contains(".")) {
			return Double.parseDouble(value);
		} else {
			return Long.parseLong(value);
		}

	}

	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(props.getProperty(key));
	}

	public Set<String> keys() {
		return props.keySet().stream().map(m -> m.toString())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(NEW_LINE_CHAR);
		
		props.keySet().forEach(k -> {
			sb.append(k.toString());
			sb.append(EQUAL_OPERATOR);
			sb.append(k.toString().equalsIgnoreCase(PASSWORD_KEY) ? HIDE_PASSWORD
					: props.getProperty(k.toString()).toString());
			sb.append(NEW_LINE_CHAR);
		});

		return sb.toString();
	}

}
