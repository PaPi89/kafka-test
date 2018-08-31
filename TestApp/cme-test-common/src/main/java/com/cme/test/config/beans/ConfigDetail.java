package com.cme.test.config.beans;

import java.util.Properties;

public class ConfigDetail {

	private Properties consumerProperties;
	
	private Properties producerProperties;
	
	private Properties commonProperties;

	public Properties getConsumerProperties() {
		return consumerProperties;
	}

	public void setConsumerProperties(Properties consumerProperties) {
		this.consumerProperties = consumerProperties;
	}

	public Properties getProducerProperties() {
		return producerProperties;
	}

	public void setProducerProperties(Properties producerProperties) {
		this.producerProperties = producerProperties;
	}

	public Properties getCommonProperties() {
		return commonProperties;
	}

	public void setCommonProperties(Properties commonProperties) {
		this.commonProperties = commonProperties;
	}
	
}
