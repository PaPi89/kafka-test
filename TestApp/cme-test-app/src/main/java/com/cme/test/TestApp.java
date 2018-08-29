package com.cme.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.config.Config;
import com.cme.test.config.PropertiesConfigLoader;
import com.cme.test.constants.AppConstants;
import com.cme.test.consumers.Consumer;
import com.cme.test.enums.TransportType;
import com.cme.test.factories.ConsumerFactory;
import com.cme.test.factories.ProducerFactory;
import com.cme.test.producers.Producer;

public class TestApp {

	private static ConfigDetail configurations;

	private static Consumer consumer;

	private static Producer producer;

	public static void main(String[] args) {

		/** Load properties. **/
		loadProperties();

		/** Create consumer instance based on command line argument. **/
		if (args[0].equals(AppConstants.CREATE_CONSUMER)) {
			createAndExecuteConsumer();
		}

		/** Create producer instance based on command line argument. **/
		if (args[0].equals(AppConstants.CREATE_PRODUCER)) {
			createAndExecuteProducer();
		}

	}

	private static void loadProperties() {

		System.out.println("Loading Configurations...");

		configurations = new ConfigDetail();

		Config config = new Config();

		try {
			config.load(new PropertiesConfigLoader().resource(
					AppConstants.CONFIGURATION_FILE,
					TestApp.class.getClassLoader()));

			configurations
					.setCommonProperties(new PropertiesConfigLoader().resource(
							config.getString(
									AppConstants.COMMON_CONFIG_FILE_PATH),
							TestApp.class.getClassLoader()).load());

			configurations.setConsumerProperties(
					new PropertiesConfigLoader().resource(
							configurations.getCommonProperties().getProperty(
									AppConstants.KAFKA_CONSUMER_FILE_PATH),
							TestApp.class.getClassLoader()).load());

			configurations.setProducerProperties(
					new PropertiesConfigLoader().resource(
							configurations.getCommonProperties().getProperty(
									AppConstants.KAFKA_PRODUCER_FILE_PATH),
							TestApp.class.getClassLoader()).load());
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	private static void createAndExecuteConsumer() {
		consumer = new ConsumerFactory().getConsumer(
				TransportType.KAFKA.getValue(), configurations,
				configurations.getCommonProperties()
						.getProperty(AppConstants.SOURCE_TOPIC),
				true, true);
		consumer.executeConsumer();
	}

	private static void createAndExecuteProducer() {

		List<Integer> partitions = Arrays
				.asList(AppConstants.DEFAULT_TOPIC_PARTITIONS);

		if ((configurations.getCommonProperties()
				.getProperty(AppConstants.SOURCE_TOPIC_PARTITIONS) != null)
				&& (!configurations.getCommonProperties()
						.getProperty(AppConstants.SOURCE_TOPIC_PARTITIONS)
						.isEmpty())) {
			partitions = Arrays
					.asList(configurations.getCommonProperties()
							.getProperty(AppConstants.SOURCE_TOPIC_PARTITIONS)
							.split(","))
					.stream().map(m -> Integer.parseInt(m))
					.collect(Collectors.toList());
		}

		producer = new ProducerFactory().getProducer(
				TransportType.KAFKA.getValue(), configurations,
				AppConstants.SOURCE_TOPIC, partitions);
		producer.executeProducer();
	}

}
