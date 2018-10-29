package com.ifree.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifree.service.controller.RequestController;
import com.ifree.service.model.RequestType;
import com.ifree.service.model.RequestWithDuration;
import com.ifree.service.model.RequestWithoutDuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestServiceIntegrationTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RequestController requestController;

	private RequestWithDuration expectedRequestWithDuration = new RequestWithDuration();
	private String requestWithDurationMessage;

	@Before
	public void setUp() throws JsonProcessingException {
		expectedRequestWithDuration.setMessage("First request");
		expectedRequestWithDuration.setDuration(10L);
		requestWithDurationMessage = objectMapper.writeValueAsString(expectedRequestWithDuration);
		expectedRequestWithDuration.setId(1L);
	}

	@Test
	public void testGetRequest() throws IOException, InterruptedException {
		requestController.receiveRequestTask(requestWithDurationMessage, RequestType.REQUEST_WITH_DURATION);
		TimeUnit.SECONDS.sleep(15L);
		RequestWithDuration requestWithDuration = (RequestWithDuration) requestController.getRequestById(1L);

		assertThat( expectedRequestWithDuration, is(requestWithDuration));
	}
}
