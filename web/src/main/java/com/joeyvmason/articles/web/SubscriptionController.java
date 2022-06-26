package com.joeyvmason.articles.web;

import com.joeyvmason.articles.core.ExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/subscriptions")
public class SubscriptionController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);


    private final ExampleService exampleService;

    @Autowired
    public SubscriptionController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping
    public void getSubscriptions(@RequestHeader String authorization) {
        // todo: How to get the identity of the caller?
        LOG.info("Authorization: {}", authorization);



    }
}
