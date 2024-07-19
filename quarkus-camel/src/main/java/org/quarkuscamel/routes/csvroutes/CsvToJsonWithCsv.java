package org.quarkuscamel.routes.csvroutes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;

public class CsvToJsonWithCsv extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        CsvDataFormat csvDataFormat = new CsvDataFormat();
        csvDataFormat.setUseMaps(true);

        from("direct:csvtojsonwithcsv")
                .routeId("csvToJsonWithCsvRoute")
                .pollEnrich().simple("file:work/output?fileName=user_csv.csv&noop=true")
                .log("Data received from CSV file >>> ${body}")
                .unmarshal(csvDataFormat)
                .log("Data successfully converted into json !!");
    }

}
