package com.cme.test.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesConfigLoader implements ConfigLoader {

	private Properties props = new Properties();

	public PropertiesConfigLoader resource(String resource,
			ClassLoader classLoader) throws IOException {
		
		Properties tempProps = new Properties();
		try (InputStream in = load(resource, classLoader)) {
			tempProps.load(in);
		}

		props.putAll(tempProps.keySet().stream().filter(
				f -> (!"null".equalsIgnoreCase(tempProps.get(f).toString())
						|| !tempProps.get(f).toString().isEmpty()))
				.collect(Collectors.toMap(k -> k,
						k -> tempProps.getProperty(k.toString()))));

		return this;
	}

	@Override
	public Properties load() throws IOException {
		return props;
	}

	@SuppressWarnings("static-access")
	private InputStream load(String resource, ClassLoader classLoader) {
		
		InputStream inputStream = null;

		if (classLoader != null) {
			inputStream = classLoader.getResourceAsStream(resource);
		}

		if (inputStream == null) {
			inputStream = classLoader.getSystemResourceAsStream(resource);
		}

		return inputStream;

	}

}
