package org.quarkuscamel.routes.dbcrud;

import org.apache.camel.builder.RouteBuilder;

public class DeleteById extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:deleteById")
                .routeId("deleteUserByIdRoute")
                .setBody().simple("{{sql.deleteDataById}}")
                .toD("sql:${body}")
                .setBody(simple("Data deleted succefully !!"))
                .setHeader("message", simple("DELETED"))
                .bean("utils", "message");
    }

}
