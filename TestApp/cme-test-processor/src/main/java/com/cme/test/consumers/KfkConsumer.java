package com.cme.test.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.constants.AppConstants;
import com.cme.test.disruptors.DisruptorFactory;
import com.cme.test.events.MessageMetadata;
import com.cme.test.processors.MessageProcessor;
import com.cme.test.recovery.RecoveryService;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class KfkConsumer implements Consumer {

	//private ConfigDetail configurations;

	private KafkaConsumer<String, String> consumer;

	private Disruptor<MessageMetadata> disruptor;
	
	private RingBuffer<MessageMetadata> ringBuffer;
	
	private boolean disruptorFlag;
	
	private RecoveryService recoveryService;
	

	public KfkConsumer() {
	}

	public KfkConsumer(ConfigDetail configDetail, String topic, boolean disruptorFlag, boolean rebalanceFlag) {

		//this.configurations = configDetail;
		this.consumer = new KafkaConsumer<>(
				configDetail.getConsumerProperties());
		this.disruptorFlag = disruptorFlag;
		if(disruptorFlag) {
			DisruptorFactory<MessageMetadata> factory = new DisruptorFactory<>();
			this.disruptor = factory.getDisruptor(MessageMetadata.class,
					AppConstants.RING_BUFFER_SIZE, DaemonThreadFactory.INSTANCE);
			this.disruptor.handleEventsWith(new MessageProcessor(configDetail));
		}
		if(rebalanceFlag) {
			this.consumer.subscribe(Arrays.asList(topic), consumerRebalanceListener);
			this.recoveryService = new RecoveryService(configDetail);
		} else {
			this.consumer.subscribe(Arrays.asList(topic));
		}

	}

	@Override
	public void executeConsumer() {
		if(disruptorFlag) {
			ringBuffer = disruptor.start();
		}
		receiveMessage();
	}

	public void receiveMessage() {
		int timeouts = 0;

		while (true) {
			// read records with a short timeout. If we time out, we don't
			// really care.
			/*
			 * ConsumerRecords<String, String> records = consumer.poll(
			 * Duration.ofMillis(configurations.getKafkaPollInterval()));
			 */
			ConsumerRecords<String, String> records = consumer
					.poll(Duration.ofMillis(1000));

			if (records.count() == 0) {
				timeouts++;
			}
			else {
				System.out.printf("Got %d records after %d timeouts\n",
						records.count(), timeouts);
				timeouts = 0;
			}

			for (ConsumerRecord<String, String> record : records) {

				System.out.println(record.value());

				/**
				 * Disruptor code to read message from kafka and publish it to
				 * ring buffer.
				 **/
				if(disruptorFlag) {
					long seq = ringBuffer.next();
					MessageMetadata messageMetadata = ringBuffer.get(seq);
					messageMetadata.setMessage(record.value());
					messageMetadata.setPartition(record.partition());
					ringBuffer.publish(seq);
				}

			}
			try {
				Thread.sleep(10000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	private final ConsumerRebalanceListener consumerRebalanceListener = new ConsumerRebalanceListener() {

		@Override
		public void onPartitionsRevoked(Collection<TopicPartition> arg0) {
			System.out.println("Inside onPartitionsRevoked...");
		}

		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> arg0) {
			System.out.println("Inside onPartitionsAssigned...");
			recoveryService.recover(consumer);
		}
	};

}
