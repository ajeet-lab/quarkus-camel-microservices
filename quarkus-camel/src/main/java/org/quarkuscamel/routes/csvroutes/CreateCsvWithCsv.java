package org.quarkuscamel.routes.csvroutes;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

public class CreateCsvWithCsv extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        List<String> headers = new ArrayList<>();
        headers.add("Id");
        headers.add("Name");
        headers.add("Position");
        headers.add("Salary");

        CsvDataFormat csvFormat = new CsvDataFormat();
        csvFormat.setDelimiter(",");
        csvFormat.setHeader(headers); // Enable maps format for CSV

        from("rest:get:csv/getAllData")
                .setBody(simple("{{sql.getAllData}}"))
                .toD("sql:${body}")
                .log("Get data by id >>> ${body.size()}")
                .marshal(csvFormat)
                .to("file:work/output?fileName=CreateCsvWithCsv.csv&fileExist=Override")
                .bean("utils", "csvCreatedSuccessMSG")
                .marshal().json(JsonLibrary.Jackson);
    }

}
