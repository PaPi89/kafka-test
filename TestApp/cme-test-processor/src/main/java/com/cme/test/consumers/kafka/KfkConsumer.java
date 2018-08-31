package com.cme.test.consumers.kafka;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.cme.test.config.beans.ConsumerConfig;
import com.cme.test.config.beans.DisruptorConfig;
import com.cme.test.consumers.Consumer;

public class KfkConsumer implements Consumer {

	private ConsumerConfig configurations;

	private KafkaConsumer<String, String> consumer;
	
	public KafkaConsumer<String, String> getConsumer(){
		return this.consumer;
	}

	public KfkConsumer() {
	}

	public KfkConsumer(ConsumerConfig consumerConfig) {

		this.configurations = consumerConfig;

	}

	@Override
	public void init() {
		consumer = new KafkaConsumer<>(
				configurations.getKafkaConsumerProperties());
		if(configurations.getDisruptorService() != null) {
			configurations.getDisruptorService().init();
			configurations.getDisruptorService().start();
		}
	}

	@Override
	public void start() {

		if (configurations.getKafkaConsumerRebalanceListener() != null) {
			consumer.subscribe(configurations.getKafkaTopics(),
					configurations.getKafkaConsumerRebalanceListener());
		} else {
			consumer.subscribe(configurations.getKafkaTopics());
		}
	}

	@Override
	public void close() {
		consumer.close();
	}

	@Override
	public void receiveMessages() {

		int timeouts = 0;

		while (true) {

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
				
				if(configurations.getDisruptorService() != null) {
					
					DisruptorConfig config = new DisruptorConfig();
					config.setMessage(record.value());
					config.setPartition(record.partition());
					configurations.getDisruptorService().process(config);
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

}
