package com.cme.test.config;

import java.io.IOException;
import java.util.Properties;

public interface ConfigLoader {

	Properties load() throws IOException;
	
}
