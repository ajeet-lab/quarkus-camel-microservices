package org.quarkuscamel.routes.csvroutes;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;

public class CreateCsvWithCsv extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        List<String> headers = new ArrayList<>();
        headers.add("Id");
        headers.add("Name");
        headers.add("Position");
        headers.add("Salary");

        CsvDataFormat csvFormat = new CsvDataFormat();
        csvFormat.setDelimiter(',');
        csvFormat.setHeader(new String[]{"id", "name", "position", "salary"}); // Enable maps format for CSV

        from("direct:createcsvwithcsv")
                .setBody(simple("{{sql.getAllData}}"))
                .toD("sql:${body}")         
                .log("Get data by id >>> TotalSize : ${body.size()}  >>>>>> and body: ${body}")
                .marshal(csvFormat)
                .to("file:work/output?fileName=CreateCsvWithCsv.csv&fileExist=Override")
                .setHeader("message", simple("CSV_CREATED"))
                .bean("utils", "csvJsonAmqMessage");
    }

}
