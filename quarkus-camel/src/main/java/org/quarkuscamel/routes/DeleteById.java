package org.quarkuscamel.routes;

import org.apache.camel.builder.RouteBuilder;

public class DeleteById extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:deleteById")
                .setBody().simple("deleteById")
                .to("log:${body}");
    }

}
