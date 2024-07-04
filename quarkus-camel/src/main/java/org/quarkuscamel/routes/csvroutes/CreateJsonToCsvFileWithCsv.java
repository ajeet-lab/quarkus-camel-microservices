package org.quarkuscamel.routes.csvroutes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;

public class CreateJsonToCsvFileWithCsv extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        CsvDataFormat csvDataFormat = new CsvDataFormat();
        csvDataFormat.setUseMaps(true);

        from("direct:createjsontocsvfilewithcsv")
                .pollEnrich().simple("file:work/output?fileName=CreateCsvWithCsv.csv&noop=true")
                .log("Data received from CSV file >>> ${body}")
                .unmarshal(csvDataFormat)
                .log("Data successfully converted into json !!");
    }

}
