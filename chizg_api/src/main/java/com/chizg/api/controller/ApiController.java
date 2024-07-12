package com.chizg.api.controller;

import com.ypt.pool.common.constant.ServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Value("${name}")
    private String name;

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServiceResponse getCoinInfo() {
        return ServiceResponse.ok(name);
    }
}
