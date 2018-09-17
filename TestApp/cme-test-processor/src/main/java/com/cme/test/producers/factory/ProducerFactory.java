package com.cme.test.producers.factory;

import java.util.List;

import com.cme.test.config.beans.ConfigDetail;
import com.cme.test.enums.TransportType;
import com.cme.test.producers.Producer;
import com.cme.test.producers.ftl.FTLProducer;
import com.cme.test.producers.kafka.KfkProducer;

public class ProducerFactory {

	public static Producer getProducer(String type, ConfigDetail configurations,
			String topic, List<Integer> partitions) {
		Producer producer = null;

		if (type.equals(TransportType.FTL.getValue())) {
			producer = new FTLProducer();
		}

		if (type.equals(TransportType.KAFKA.getValue())) {
			producer = new KfkProducer(configurations,
					configurations.getCommonProperties().getProperty(topic), partitions);
		}

		return producer;
	}
}
