package com.cme.test.consumers.kafka.listeners;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import com.cme.test.config.beans.RecoveryConfig;
import com.cme.test.consumers.recovery.RecoveryService;
import com.cme.test.consumers.recovery.impls.KafkaRecoveryService;

public class CustomRebalanceListener implements ConsumerRebalanceListener {

	private RecoveryService recoveryService;
	
	public CustomRebalanceListener() {
	}
	
	public CustomRebalanceListener(RecoveryConfig recoveryDetail) {
		recoveryService = new KafkaRecoveryService(recoveryDetail);
	}
	
	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		// TODO:- Not clear as of now
		System.out.println("Inside onPartitionsRevoked...");
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		System.out.println("Inside onPartitionsAssigned...");
		recoveryService.recover();
	}

}
