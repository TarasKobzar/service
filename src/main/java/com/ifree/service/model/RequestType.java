package com.ifree.service.model;

public enum RequestType {

    REQUEST_WITHOUT_DURATION(RequestWithoutDuration.class),
    REQUEST_WITH_DURATION(RequestWithDuration.class);

    Class classType;

    RequestType(Class classType) {
        this.classType = classType;
    }

    public Class getClassType() {
        return classType;
    }
}