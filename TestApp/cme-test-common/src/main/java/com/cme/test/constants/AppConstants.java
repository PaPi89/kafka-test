package com.cme.test.constants;

public interface AppConstants {

	String KAFKA_CONSUMER_FILE_PATH = "kafka.consumer.file.path";
	
	String KAFKA_PRODUCER_FILE_PATH = "kafka.producer.file.path";
	
	String COMMON_CONFIG_FILE_PATH = "common.config.file.path";
	
	String SOURCE_TOPIC = "source.topic";
	
	String TARGET_TOPIC = "target.topic";
	
	String CREATE_CONSUMER = "consumer";
	
	String CREATE_PRODUCER = "producer";
	
	String CONFIGURATION_FILE = "app.properties";
	
	int RING_BUFFER_SIZE = 4096;
}
