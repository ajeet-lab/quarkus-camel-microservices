package org.quarkuscamel.routes.dbcrud;

import org.apache.camel.builder.RouteBuilder;


public class GetById extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:getById")
                .routeId("getUserByIdRoute")
                .setBody().simple("{{sql.getDataById}}") // Set SQL query
                .toD("sql:${body}?outputType=SelectOne"); // Execute SQL query
    }
}
