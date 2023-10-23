package com.alfaCentauri.camel.route;

import org.apache.camel.builder.RouteBuilder;

public class MyRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start")
                .setBody(constant("Goodbye"))
                .to("file:mock/output");
    }
}
