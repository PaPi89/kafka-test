package com.cme.test.factories;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.consumers.Consumer;
import com.cme.test.consumers.KfkConsumer;
import com.cme.test.enums.TransportType;

public class ConsumerFactory {

	public Consumer getConsumer(String type, ConfigDetail configurations, String topic, boolean disruptorFlag, boolean reBalanceFlag) {

		Consumer consumer = null;

		if (type.equals(TransportType.KAFKA.getValue())) {
			consumer = new KfkConsumer(configurations, topic, disruptorFlag, reBalanceFlag);
		}

		return consumer;
	}
}
