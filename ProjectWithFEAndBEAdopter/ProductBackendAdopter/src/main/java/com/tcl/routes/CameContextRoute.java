package com.tcl.routes;

import org.apache.camel.builder.RouteBuilder;

public class CameContextRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        
        restConfiguration().component("netty-http").host("localhost").port(9902).enableCORS(true);

        rest("/api/v1/backendadopter")
           .get("/products").to("direct:products")
           .post("/product/search").to("direct:searchproduct")
           .post("/product/limitskipandselectedfield").to("direct:limitskipandselectedfield");

           
               
    }
    
}
