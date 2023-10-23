package com.alfaCentauri.camel.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = { "camel.springboot.name=customName" }
)
class MyRouterTest {

    @EndpointInject("mock:file:output")
    MockEndpoint mockEndpoint;

    @Autowired
    ProducerTemplate template;

    @Test
    void configure() throws InterruptedException {
        mockEndpoint.expectedBodiesReceived("Goodbye");
        template.sendBody("direct:input", null);
        mockEndpoint.assertIsSatisfied();
    }
}