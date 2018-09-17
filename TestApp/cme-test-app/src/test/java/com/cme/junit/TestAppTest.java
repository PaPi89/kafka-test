package com.cme.junit;

import org.testng.annotations.Test;

import com.cme.test.TestApp;
import com.cme.test.consumers.kafka.KfkConsumer;

import mockit.Expectations;
import mockit.Mocked;

public class TestAppTest {

	@Mocked
	private KfkConsumer kafkaConsumer;
	
	@Test
	public void testMain() {

		new Expectations() {
			{
				kafkaConsumer.init();
				kafkaConsumer.start();
				kafkaConsumer.receiveMessages();
				kafkaConsumer.close();
			}
		};

		TestApp.main(null);
	}
}
