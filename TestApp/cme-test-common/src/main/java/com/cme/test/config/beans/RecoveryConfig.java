package com.cme.test.config.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class RecoveryConfig {

	private Map<String, String> topics;

	private List<Integer> partitions;

	private KafkaConsumer<String, String> sourceConsumer;

	private KafkaConsumer<String, String> targetConsumer;

	public Map<String, String> getTopics() {
		if (topics == null) {
			topics = new HashMap<>(0);
		}
		return topics;
	}

	public void setTopics(Map<String, String> topics) {
		this.topics = topics;
	}

	public List<Integer> getPartitions() {
		if (partitions == null) {
			partitions = new ArrayList<>(0);
		}
		return partitions;
	}

	public void setPartitions(List<Integer> partitions) {
		this.partitions = partitions;
	}

	public KafkaConsumer<String, String> getSourceConsumer() {
		return sourceConsumer;
	}

	public void setSourceConsumer(
			KafkaConsumer<String, String> sourceConsumer) {
		this.sourceConsumer = sourceConsumer;
	}

	public KafkaConsumer<String, String> getTargetConsumer() {
		return targetConsumer;
	}

	public void setTargetConsumer(
			KafkaConsumer<String, String> targetConsumer) {
		this.targetConsumer = targetConsumer;
	}

}
