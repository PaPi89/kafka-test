package com.cme.test.constants;

public interface AppConstants {

	String KAFKA_CONSUMER_FILE_PATH = "kafka.consumer.file.path";
	
	String KAFKA_PRODUCER_FILE_PATH = "kafka.producer.file.path";
	
	String COMMON_CONFIG_FILE_PATH = "common.config.file.path";
	
	String SOURCE_TOPIC = "source.topic";
	
	String SOURCE_TOPIC_PARTITIONS = "source.topic.partitions";
	
	int DEFAULT_TOPIC_PARTITIONS = 0;
	
	String TARGET_TOPIC = "target.topic";
	
	String TARGET_TOPIC_PARTITIONS = "target.topic.partitions";
	
	String CREATE_CONSUMER = "consumer";
	
	String CREATE_PRODUCER = "producer";
	
	String CONFIGURATION_FILE = "app.properties";
	
	int RING_BUFFER_SIZE = 4096;
}
