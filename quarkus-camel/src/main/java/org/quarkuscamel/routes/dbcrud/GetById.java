package org.quarkuscamel.routes.dbcrud;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.quarkuscamel.entities.Employee;



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
