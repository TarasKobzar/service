package com.ifree.service.model;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(callSuper = false)
public class RequestWithoutDuration extends Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    public Long id;
    @Setter
    public String message;
}
