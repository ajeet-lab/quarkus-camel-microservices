package org.quarkuscamel.routes;


import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import jakarta.inject.Inject;

public class CamelContextRoute extends RouteBuilder {

    @Inject
    CamelContext camelContext;

    @Override
    public void configure() throws Exception {

        // Set up the ActiveMQ connection factory
        restConfiguration()
                .component("netty-http")
                .host("localhost")
                .port(8081);


        // START CRUD OPERATION ROUTE
        rest("/api/v1").get("/users")
                .bindingMode(RestBindingMode.auto).to("direct:getAllData");

        rest("/api/v1").post("/users")
                .bindingMode(RestBindingMode.auto).to("direct:create");

        rest("/api/v1").get("/users/{id}")
                .bindingMode(RestBindingMode.auto).to("direct:getById");

        rest("/api/v1").put("/users/{id}")
                .bindingMode(RestBindingMode.auto).to("direct:updateById");

        rest("/api/v1").delete("/users/{id}")
                .bindingMode(RestBindingMode.auto).to("direct:deleteById");
        // END CRUD OPERATION ROUTE


        // START CREATE CSV ROUTE
        rest("/api/v1").get("/csv/json-to-csv")
                .bindingMode(RestBindingMode.auto).to("direct:jsontocsvwithcsv");

        rest("/api/v1").get("/csv/csv-to-json")
                .bindingMode(RestBindingMode.auto).to("direct:csvtojsonwithcsv");

        rest("/api/v1").get("/bindy/json-to-csv")
                .bindingMode(RestBindingMode.auto).to("direct:jsontocsvwithbindy");

        rest("/api/v1").get("/bindy/csv-to-json")
                .bindingMode(RestBindingMode.auto).to("direct:csvtojsonwithbindy");
        // END CREATE CSV ROUTE


        // START ACTIVE MQ ROUTE
        rest("/api/v1").get("/amq/pushdataintoqueue")
                .bindingMode(RestBindingMode.auto).to("direct:pushdataintoqueue");
        // END ACTIVE MQ ROUTE


        // START XML-JSON ROUTE
        rest("/api/v1").post("/jacksonxml/xml-to-json").to("direct:xmltojson");

        rest("/api/v1").post("/jacksonxml/json-to-xml")
                .bindingMode(RestBindingMode.auto).to("direct:jsontoxml");
        // END XML-JSON ROUTE

        // START XML-SOAP ROUTE
        rest("/api/v1").post("/jacksonxml/xml-to-soap").to("direct:xmltosoap");

        rest("/api/v1").post("calculator/{id}").to("direct:calculator");
        // END XML-SOAP ROUTE

        //START JOLT TRANSFORATION ROUTE
        rest("/api/v1").post("/jolt/json-to-json")
                .bindingMode(RestBindingMode.auto).to("direct:jolttransformation");
        //END JOLT TRANSFORATION ROUTE
    }

}

