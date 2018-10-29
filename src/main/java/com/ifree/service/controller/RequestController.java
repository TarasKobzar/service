package com.ifree.service.controller;

import com.ifree.service.model.Request;
import com.ifree.service.model.RequestType;
import com.ifree.service.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    private RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/{requestType}")
    public ResponseEntity receiveRequestTask(@RequestBody String request, @PathVariable(name = "requestType") RequestType requestType) throws IOException {
        requestService.addRequestToHandlingQueue(request, requestType);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public Request getRequestById(@PathVariable(name = "id") Long id) {
        return requestService.getRequest(id);
    }

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }
}