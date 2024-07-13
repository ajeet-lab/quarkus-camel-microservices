package org.quarkuscamel.routes.csvroutes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.quarkuscamel.entities.Employee;

public class CsvToJsonWithBindy extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        BindyCsvDataFormat bindy = new BindyCsvDataFormat(Employee.class);

        from("direct:csvtojsonwithbindy")
                .routeId("csvToJsonWithBindyRoute")
                .pollEnrich().simple("file:work/output?fileName=user_bindy.csv&noop=true")
                .log("Data received from CSV file >>> ${body}")
                .unmarshal(bindy)
                .log("Data successfully converted into json !!");
    }

}
