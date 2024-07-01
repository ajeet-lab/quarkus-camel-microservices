package org.quarkuscamel.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.quarkuscamel.entities.Employee;

import jakarta.enterprise.context.ApplicationScoped;



public class GetById extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        BindyCsvDataFormat bindy = new BindyCsvDataFormat(Employee.class);
        bindy.setLocale("en");
        from("direct:getById")
            .setBody().simple("{{sql.getDataById}}") // Set SQL query
            .toD("sql:${body}?outputType=SelectOne"); // Execute SQL query
        }
}
