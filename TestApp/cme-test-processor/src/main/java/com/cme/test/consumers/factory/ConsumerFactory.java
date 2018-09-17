package com.cme.test.consumers.factory;

import com.cme.test.config.beans.ConsumerConfig;
import com.cme.test.consumers.Consumer;
import com.cme.test.consumers.kafka.KfkConsumer;
import com.cme.test.enums.TransportType;

public class ConsumerFactory {

	public static Consumer getConsumer(String type, ConsumerConfig configurations) {

		Consumer consumer = null;

		if (type.equals(TransportType.KAFKA.getValue())) {
			consumer = new KfkConsumer(configurations);
		}

		return consumer;
	}
}
