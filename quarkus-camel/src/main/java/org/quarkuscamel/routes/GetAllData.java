package org.quarkuscamel.routes;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;


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
