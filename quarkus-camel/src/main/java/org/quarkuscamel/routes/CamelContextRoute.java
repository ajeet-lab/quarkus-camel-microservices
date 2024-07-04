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
        restConfiguration().component("netty-http").host("localhost").port(8081);
        

        // START CRUD OPERATION ROUTE
        rest("/api/v1").get("/users").bindingMode(RestBindingMode.auto).routeId("getAllUserRoute").to("direct:getAllData");
        rest("/api/v1").post("/users").bindingMode(RestBindingMode.auto).routeId("createUserRoute").to("direct:create");
        rest("/api/v1").get("/users/{id}").bindingMode(RestBindingMode.auto).routeId("getUserByIdRoute").to("direct:getById");
        rest("/api/v1").put("/users/{id}").bindingMode(RestBindingMode.auto).routeId("updateUserByIdRoute").to("direct:updateById");
        rest("/api/v1").delete("/users/{id}").bindingMode(RestBindingMode.auto).routeId("deleteUserByIdRoute").to("direct:deleteById");
        // END CRUD OPERATION ROUTE


        // START CREATE CSV ROUTE
        rest("/api/v1").get("/csv/createcsv").bindingMode(RestBindingMode.auto).routeId("createCsvFileWithCsvRoute").to("direct:createcsvwithcsv"); // Create CSV using camel-qaurkus-csv dependency
        rest("/api/v1").get("/csv/createbindy").bindingMode(RestBindingMode.auto).routeId("createCsvFileWithBindyRoute").to("direct:createcsvwithbindy"); // Create CSV using camel-qaurkus-bindy dependency
        rest("/api/v1").get("/csv/createjsonwithcsv").bindingMode(RestBindingMode.auto).routeId("createJsonFileWithCsvRoute").to("direct:createjsontocsvfilewithcsv"); // Create CSV using camel-qaurkus-csv dependency
        rest("/api/v1").get("/csv/createjsonwithbindy").bindingMode(RestBindingMode.auto).routeId("createJsonFileWithBindyRoute").to("direct:createjsontocsvfilewithbindy"); // Create CSV using camel-qaurkus-bindy dependency
        // END CREATE CSV ROUTE


        // START ACTIVE MQ ROUTE
            rest("/api/v1").get("/amq/pushdataintoqueue").bindingMode(RestBindingMode.auto).routeId("pushDataIntoQueueRoute").to("direct:pushdataintoqueue"); // Push data into amq queue using camel-qaurkus-activemq and pooled-jms dependency
        // END ACTIVE MQ ROUTE


        // START XML-JSON ROUTE
        rest("/api/v1").post("/jacksonxml/xml-to-json").routeId("xmlToJson").to("direct:xmltojson");
         rest("/api/v1").post("/jacksonxml/json-to-xml").bindingMode(RestBindingMode.auto).routeId("jsonToXml").to("direct:jsontoxml");
        // END XML-JSON ROUTE

        // START XML-SOAP ROUTE
        rest("/api/v1").post("/jacksonxml/xml-to-soap").routeId("xmlToSoap").to("direct:xmltosoap");
        // END XML-SOAP ROUTE


        // START JOLT TRANSFORATION ROUTE
        rest("/api/v1").post("/jolt/json-to-json").bindingMode(RestBindingMode.auto).routeId("jsonToJson").to("direct:jolttransformation");
        
// END JOLT TRANSFORATION ROUTE
    }

}

