package com.cme.test.factories;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.enums.TransportType;
import com.cme.test.producers.FTLProducer;
import com.cme.test.producers.KfkProducer;
import com.cme.test.producers.Producer;

public class ProducerFactory {

	public Producer getProducer(String type, ConfigDetail configurations,
			String topic) {
		Producer producer = null;

		if (type.equals(TransportType.FTL.getValue())) {
			producer = new FTLProducer();
		}

		if (type.equals(TransportType.KAFKA.getValue())) {
			producer = new KfkProducer(configurations,
					configurations.getCommonProperties().getProperty(topic));
		}

		return producer;
	}
}
