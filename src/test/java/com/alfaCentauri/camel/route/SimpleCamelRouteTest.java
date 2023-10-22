package com.alfaCentauri.camel.route;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.impl.engine.SimpleCamelContext;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author ricardopresilla@gmail.com
 * @version 1.0
 **/
//@ActiveProfiles("mock")
//@RunWith(CamelSpringBootRunner.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = { "camel.springboot.name=customName" }
)
public class SimpleCamelRouteTest {

    @Autowired
    CamelContext context;

    @Autowired
    Environment environment;

    @EndpointInject("mock:test")
    MockEndpoint mockEndpoint;

    @Autowired
    ProducerTemplate producerTemplate;

    //Spring context fixtures
    @Configuration
    static class TestConfig {

        @Bean
        RoutesBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:input").to("mock:test");
                }
            };
        }
    }

    @Before
    public void setUp() throws Exception {
        mockEndpoint = new MockEndpoint();
        context = new SimpleCamelContext();
        producerTemplate = new DefaultProducerTemplate(context);
        environment = new StandardEnvironment();
    }

    @Test
    public void shouldAutowireProducerTemplate() {
        Assert.assertNotNull(producerTemplate);
    }

    @Test
    public void shouldSetCustomName() {
        String expected = "camel-1";
        String result = producerTemplate.getCamelContext().getName();
        Assert.assertEquals(expected, result);
        System.out.println("Fin");
    }

    @Test
    public void testMoveFilesMock() throws InterruptedException {
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Samsung TV,500\n" +
                "ADD,101,LG TV,500";
        MockEndpoint mockEndpoint = new MockEndpoint();
        mockEndpoint.setEndpointUriIfNotSpecified ("mock/output");
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);
        producerTemplate.sendBodyAndHeader( "direct:input", message, "env", "mock" );
        mockEndpoint.assertIsSatisfied();
    }
}