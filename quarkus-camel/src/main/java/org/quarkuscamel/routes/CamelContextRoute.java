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


        // START CRUD OPERATION ROUTE
        rest("/api/v1").get("/users").routeId("getAllUserRoute").to("direct:getAllData");
        rest("/api/v1").post("/users").routeId("createUserRoute").to("direct:create");
        rest("/api/v1").get("/users/{id}").routeId("getUserByIdRoute").to("direct:getById");
        rest("/api/v1").put("/users/{id}").routeId("updateUserByIdRoute").to("direct:updateById");
        rest("/api/v1").delete("/users/{id}").routeId("deleteUserByIdRoute").to("direct:deleteById");
        // END CRUD OPERATION ROUTE


        // START CREATE CSV ROUTE
        rest("/api/v1").get("/csv/createcsv").routeId("createCsvFileWithCsvRoute").to("direct:createcsvwithcsv"); // Create CSV using camel-qaurkus-csv dependency
        rest("/api/v1").get("/csv/createbindy").routeId("createCsvFileWithBindyRoute").to("direct:createcsvwithbindy"); // Create CSV using camel-qaurkus-bindy dependency
        // END CREATE CSV ROUTE


        // START ACTIVE MQ ROUTE
            rest("/api/v1").get("/amq/pushdataintoqueue").routeId("pushDataIntoQueueRoute").to("direct:pushdataintoqueue"); // Push data into amq queue using camel-qaurkus-activemq and pooled-jms dependency
        // END ACTIVE MQ ROUTE
    }

}

