package com.maaya.demo.excel.validator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);


    @RequestMapping(method = RequestMethod.GET)
    String health() {
        logger.debug("health check.");
        return "Health check OK.";
    }

}
