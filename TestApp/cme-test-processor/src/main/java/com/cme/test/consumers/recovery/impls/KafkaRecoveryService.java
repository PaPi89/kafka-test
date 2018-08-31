package com.cme.test.consumers.recovery.impls;

import java.util.Arrays;

import org.apache.kafka.common.TopicPartition;

import com.cme.test.config.beans.RecoveryConfig;
import com.cme.test.constants.AppConstants;
import com.cme.test.consumers.recovery.RecoveryService;

public class KafkaRecoveryService implements RecoveryService {

	private RecoveryConfig recoveryDetail;

	public KafkaRecoveryService(RecoveryConfig recoveryDetail) {
		this.recoveryDetail = recoveryDetail;
	}

	@Override
	public void recover() {

		recoveryDetail.getTopics().keySet().forEach(sourceTopic -> {

			recoveryDetail.getPartitions().forEach(partition -> {
				
				TopicPartition tp = new TopicPartition(
						recoveryDetail.getTopics().get(sourceTopic), partition);
				recoveryDetail.getTargetConsumer().assign(Arrays.asList(tp));
				recoveryDetail.getTargetConsumer().seekToEnd(Arrays.asList(tp));
				TopicPartition tp1 = new TopicPartition(sourceTopic, partition);
				recoveryDetail.getSourceConsumer().seek(tp1,
						recoveryDetail.getTargetConsumer().position(tp) + 1);

			});
		});
		
		recoveryDetail.getTargetConsumer().close();
	}

}
