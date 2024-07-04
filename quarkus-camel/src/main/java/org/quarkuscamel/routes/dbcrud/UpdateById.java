package org.quarkuscamel.routes.dbcrud;

import org.apache.camel.builder.RouteBuilder;

public class UpdateById extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:updateById")
                .setHeader("name", simple("${body[name]}"))
                .setHeader("position", simple("${body[position]}"))
                .setHeader("salary", simple("${body[salary]}"))
                .setBody().simple("{{sql.updateDataById}}")
                .toD("sql:${body}")
                .log("headers >>>>>>>>>>>>> ${in.headers}")
                .setHeader("message", simple("UPDATED"))
                .bean("utils", "message");
        ;
    }

}
