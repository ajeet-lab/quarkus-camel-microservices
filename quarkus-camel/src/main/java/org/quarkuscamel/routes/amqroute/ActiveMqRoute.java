package org.quarkuscamel.routes.amqroute;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class ActiveMqRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
       from("rest:get:amq/getAllData")
                .setBody(simple("{{sql.getAllData}}"))
                .log("Before calling database, Request : ${body}")
                .toD("sql:${body}") 
                .split().simple("${body}")
                .setExchangePattern(ExchangePattern.InOnly)
                .to("activemq:queue:amq-users")
                .end()
                .bean("utils", "pushIntoQueueSuccessMSG")
                .marshal().json(JsonLibrary.Jackson);
    }

}
