package org.quarkuscamel.routes.xmlroute;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;
import org.quarkuscamel.entities.Person;

public class XmlToJsonRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Person.class);

        JacksonXMLDataFormat jacksonXmlDataFormat = new JacksonXMLDataFormat(Person.class);

             from("direct:xmltojson")
              .unmarshal(jacksonXmlDataFormat)
             .marshal(jsonDataFormat)
             .log("After JSON marshalling :: ${body}");
    }
    
}
