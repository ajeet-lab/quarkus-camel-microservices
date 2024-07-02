package org.quarkuscamel.routes.dbcrud;

import org.apache.camel.builder.RouteBuilder;


public class GetAllData extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:getAllData").routeId("getAllData")
                .setBody(simple("{{sql.getAllData}}"))
                .log("Before calling database, Request : ${body}")
                .toD("sql:${body}")  
                ;
    }

}
