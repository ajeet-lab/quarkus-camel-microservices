package org.quarkuscamel.routes;

import org.apache.camel.builder.RouteBuilder;

public class UpdateById extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("direct:updateById")
        .setBody().simple("updateById")
        .to("log:${body}");;
    }
    
}
