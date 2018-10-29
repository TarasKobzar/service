package com.ifree.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifree.service.config.RabbitConfig;
import com.ifree.service.exception.RequestNotFoundException;
import com.ifree.service.model.*;
import com.ifree.service.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RequestService {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;
    private final RequestRepository requestRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RequestService(AmqpTemplate amqpTemplate, ObjectMapper objectMapper, RequestRepository requestRepository) {
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
        this.requestRepository = requestRepository;
    }

    public void addRequestToHandlingQueue(String request, RequestType requestType) throws JsonProcessingException {
        logger.info("Sending request to RabbitMQ - {}", request);
        amqpTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, objectMapper.writeValueAsString(new RabbitRequest(request, requestType)));
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleMessageFromQueue(String message) throws IOException, InterruptedException {
        RabbitRequest rabbitRequest = objectMapper.readValue(message, RabbitRequest.class);
        logger.info("Request of type {} has been received from rabbitMQ. Body - {}", rabbitRequest.requestType.getClassType(), rabbitRequest.request);
        Request request;

        if (rabbitRequest.requestType == RequestType.REQUEST_WITH_DURATION) {
            request = (RequestWithDuration) objectMapper.readValue(rabbitRequest.request, rabbitRequest.requestType.getClassType());
            RequestWithDuration fullRequest = (RequestWithDuration) request;
            logger.info("Sleeping as requested");
            TimeUnit.SECONDS.sleep(fullRequest.duration);
            logger.info("Message - {}", Optional.ofNullable(fullRequest.message).orElse("Empty message"));
            saveRequest(request);
            logger.info("Request is saved");
        } else {
            request = (RequestWithoutDuration) objectMapper.readValue(rabbitRequest.request, rabbitRequest.requestType.getClassType());
            saveRequest(request);
            logger.info("Request is saved");
        }
    }

    public Request getRequest(Long id) {
        Optional<Request> request = requestRepository.findById(id);
        return request.orElseThrow(RequestNotFoundException::new);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    private void saveRequest(Request request) {
        requestRepository.save(request);
    }
}
