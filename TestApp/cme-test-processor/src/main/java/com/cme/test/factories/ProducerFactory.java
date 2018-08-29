package com.cme.test.factories;

import java.util.List;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.enums.TransportType;
import com.cme.test.producers.FTLProducer;
import com.cme.test.producers.KfkProducer;
import com.cme.test.producers.Producer;

public class ProducerFactory {

	public Producer getProducer(String type, ConfigDetail configurations,
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
