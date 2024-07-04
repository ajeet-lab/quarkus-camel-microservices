package org.quarkuscamel.routes.datamapping;

import org.apache.camel.builder.RouteBuilder;

public class JoltRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
       from("direct:jolttransformation")
       .setBody(simple("{{sql.getAllData}}"))
       .toD("sql:${body}")  
       .to("jolt:dataMapping.json")
       .log("After transformation data :: ${body.size()}");
    }
    
}
