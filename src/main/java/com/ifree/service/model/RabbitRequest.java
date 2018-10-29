package com.ifree.service.model;

public final class RabbitRequest {
    public String request;
    public RequestType requestType;

    public RabbitRequest(String request, RequestType requestType) {
        this.request = request;
        this.requestType = requestType;
    }

    public RabbitRequest() {
    }
}
