package com.tcl.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class CamelContextRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
       restConfiguration().component("netty-http").host("localhost").port(9092).enableCORS(true);

       rest("/api/v1/frontend")
                .get("/products").to("direct:products")
                .post("/product/search").to("direct:searchproduct")
                .post("/product/limitskipandselectedfield").to("direct:limitskipandselectedfield");


                from("direct:processHeader").routeId("HeaderValidationRoute")
                    .choice()
                        .when(simple("${in.header.SourceName} == null || ${in.header.SourceName} == '' || ${in.header.ConversationID} == null || ${in.header.ConversationID} == '' || ${in.header.Authorization} == null || ${in.header.Authorization} == ''"))
                            .setHeader("MissingHeader", simple("MissingHeader"))
                            .process("ErrorAckProcessor")
                            .marshal().json(JsonLibrary.Jackson)
                            .convertBodyTo(String.class)
                    .end();
    }
    
}
