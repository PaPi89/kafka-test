package com.cme.test.recovery;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.constants.AppConstants;

public class RecoveryService {

	private KafkaConsumer<String, String> targetConsumer;

	private ConfigDetail configurations;

	public RecoveryService() {
	}

	public RecoveryService(ConfigDetail configurations) {
		this.configurations = configurations;
		this.targetConsumer = new KafkaConsumer<>(
				configurations.getConsumerProperties());
	}

	public void recover(KafkaConsumer<String, String> sourceConsumer) {
		TopicPartition tp = new TopicPartition(configurations
				.getCommonProperties().getProperty(AppConstants.TARGET_TOPIC),
				0);
		targetConsumer.assign(Arrays.asList(tp));
		targetConsumer.seekToEnd(Arrays.asList(tp));
		TopicPartition tp1 = new TopicPartition(configurations
				.getCommonProperties().getProperty(AppConstants.SOURCE_TOPIC),
				0);
		sourceConsumer.seek(tp1, targetConsumer.position(tp) + 1);
		targetConsumer.close();
	}
}
