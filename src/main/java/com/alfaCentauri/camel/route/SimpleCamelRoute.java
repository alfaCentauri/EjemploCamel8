package com.alfaCentauri.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {
    @Autowired
    Environment environment;

    @Override
    public void configure() throws Exception {

        from("direct:input")
                .log("Timer Invoked and the body" + environment.getProperty("message"))
                .choice()
                .when((header("env").isNotEqualTo("mock")))
                .pollEnrich("file:data/input?delete=true&readLock=none")
                .otherwise()
                .log("mock env flow and the body is ${body}")
                .end()
                .to("file:data/output");


    }
}
