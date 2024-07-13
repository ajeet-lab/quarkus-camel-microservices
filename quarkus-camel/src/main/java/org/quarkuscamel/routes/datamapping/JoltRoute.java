package org.quarkuscamel.routes.datamapping;

import org.apache.camel.builder.RouteBuilder;

public class JoltRoute extends RouteBuilder{

    @Override
    public void configure() throws Exception {
       from("direct:jolttransformation")
               .routeId("jsonToJsonMappingWithJolt")
               .setBody(simple("{{sql.getAllData}}"))
               .toD("sql:${body}")
               .to("jolt:dataMapping.json?contentCache=true")
               .log("JoltTransformationImpl_001 :: After transformation data >>> ${body.size()}");
    }
    
}
