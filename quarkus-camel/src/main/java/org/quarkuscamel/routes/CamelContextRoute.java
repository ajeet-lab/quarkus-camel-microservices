package org.quarkuscamel.routes;


import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import jakarta.inject.Inject;

public class CamelContextRoute extends RouteBuilder {

    @Inject
    CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        // Set up the ActiveMQ connection factory
        restConfiguration().component("netty-http").host("localhost").port(8081).bindingMode(RestBindingMode.auto);

        rest("/api/v1").get("/getAllData").to("direct:getAllData");
        rest("/api/v1").post("/create").to("direct:create");
        rest("/api/v1").get("/getById/{id}").to("direct:getById");
        rest("/api/v1").put("/updateById/{id}").to("direct:updateById");
        rest("/api/v1").delete("/deleteById/{id}").to("direct:deleteById");

    }

}

