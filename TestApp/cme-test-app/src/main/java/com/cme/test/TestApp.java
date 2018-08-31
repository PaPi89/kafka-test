package com.cme.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.cme.test.config.Config;
import com.cme.test.config.PropertiesConfigLoader;
import com.cme.test.config.beans.ConfigDetail;
import com.cme.test.config.beans.ConsumerConfig;
import com.cme.test.config.beans.RecoveryConfig;
import com.cme.test.constants.AppConstants;
import com.cme.test.consumers.Consumer;
import com.cme.test.consumers.factory.ConsumerFactory;
import com.cme.test.consumers.kafka.KfkConsumer;
import com.cme.test.consumers.kafka.listeners.CustomRebalanceListener;
import com.cme.test.disruptor.impls.MessageMetadataDisruptor;
import com.cme.test.enums.AppInstanceType;
import com.cme.test.enums.TransportType;
import com.cme.test.producers.Producer;
import com.cme.test.producers.factory.ProducerFactory;

public class TestApp {

	private static ConfigDetail configurations;

	private static Consumer consumer;

	private static Producer producer;

	public static void main(String[] args) {

		try {
			/** Load properties. **/
			loadProperties();

			if (configurations.getCommonProperties()
					.getProperty(AppConstants.APP_INSTANCE_TYPE)
					.equals(AppInstanceType.CONSUMER.getValue())) {
				createAndExecuteConsumer();
			}

			if (configurations.getCommonProperties()
					.getProperty(AppConstants.APP_INSTANCE_TYPE)
					.equals(AppInstanceType.PRODUCER.getValue())) {
				createAndExecuteProducer();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	private static void loadProperties() throws IOException {

		System.out.println("Loading Configurations...");

		configurations = new ConfigDetail();

		Config config = new Config();

		config.load(new PropertiesConfigLoader().resource(
				AppConstants.COMMON_CONFIG_FILE,
				TestApp.class.getClassLoader()));
		configurations.setCommonProperties(config.getProps());

	}

	private static void createAndExecuteConsumer() throws IOException {

		configurations
				.setConsumerProperties(new PropertiesConfigLoader().resource(
						configurations.getCommonProperties().getProperty(
								AppConstants.KAFKA_CONSUMER_FILE_PATH),
						TestApp.class.getClassLoader()).load());

		ConsumerConfig consumerConfig = new ConsumerConfig();
		consumerConfig.setKafkaConsumerProperties(
				configurations.getConsumerProperties());
		consumerConfig.setKafkaTopics(
				Arrays.asList(configurations.getCommonProperties()
						.getProperty(AppConstants.SOURCE_TOPIC).split(",")));
		consumerConfig.setDisruptorService(new MessageMetadataDisruptor());

		consumer = new ConsumerFactory()
				.getConsumer(TransportType.KAFKA.getValue(), consumerConfig);

		consumer.init();

		consumerConfig
				.setKafkaConsumerRebalanceListener(createRebalanceListener());
		
		consumer.start();
		consumer.receiveMessages();
		consumer.close();

	}

	private static void createAndExecuteProducer() throws IOException {
		configurations
				.setProducerProperties(new PropertiesConfigLoader().resource(
						configurations.getCommonProperties().getProperty(
								AppConstants.KAFKA_PRODUCER_FILE_PATH),
						TestApp.class.getClassLoader()).load());

		List<Integer> partitions = Arrays
				.asList(AppConstants.DEFAULT_TOPIC_PARTITIONS);

		if ((configurations.getCommonProperties()
				.getProperty(AppConstants.TOPIC_PARTITIONS) != null)
				&& (!configurations.getCommonProperties()
						.getProperty(AppConstants.TOPIC_PARTITIONS)
						.isEmpty())) {
			partitions = Arrays
					.asList(configurations.getCommonProperties()
							.getProperty(AppConstants.TOPIC_PARTITIONS)
							.split(","))
					.stream().map(m -> Integer.parseInt(m))
					.collect(Collectors.toList());
		}

		producer = new ProducerFactory().getProducer(
				TransportType.KAFKA.getValue(), configurations,
				AppConstants.SOURCE_TOPIC, partitions);
		producer.executeProducer();
	}

	@SuppressWarnings("unchecked")
	private static CustomRebalanceListener createRebalanceListener() {

		ConsumerConfig consumerConfig = new ConsumerConfig();
		consumerConfig.setKafkaConsumerProperties(
				configurations.getConsumerProperties());
		consumerConfig.setKafkaTopics(
				Arrays.asList(configurations.getCommonProperties()
						.getProperty(AppConstants.TARGET_TOPIC).split(",")));

		RecoveryConfig recoveryDetail = new RecoveryConfig();
		recoveryDetail
				.setSourceConsumer(((KfkConsumer) consumer).getConsumer());
		Consumer targetConsumer = new ConsumerFactory()
				.getConsumer(TransportType.KAFKA.getValue(),
						consumerConfig);
		targetConsumer.init();
		recoveryDetail.setTargetConsumer(((KfkConsumer) targetConsumer).getConsumer());
		recoveryDetail.setPartitions(Arrays
				.asList(configurations.getCommonProperties()
						.getProperty(AppConstants.TOPIC_PARTITIONS).split(","))
				.stream().map(m -> Integer.parseInt(m))
				.collect(Collectors.toList()));

		// need to map source topic with target topic

		CustomRebalanceListener listener = new CustomRebalanceListener(
				recoveryDetail);

		return listener;

	}

}
