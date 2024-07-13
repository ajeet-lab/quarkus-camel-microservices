package org.quarkuscamel.routes.amqroute;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class ActiveMqRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:pushdataintoqueue")
                .routeId("pushDataIntoQueueRoute")
                .setBody(simple("{{sql.getAllData}}"))
                .log("Before calling database, Request : ${body}")
                .toD("sql:${body}")
                .split().simple("${body}")
                .setExchangePattern(ExchangePattern.InOnly)
                .to("activemq:queue:amq-users")
                .end()
                .setHeader("message", simple("PUSHEDINTOQUEUE"))
                .bean("utils", "csvJsonAmqMessage");
    }

}
