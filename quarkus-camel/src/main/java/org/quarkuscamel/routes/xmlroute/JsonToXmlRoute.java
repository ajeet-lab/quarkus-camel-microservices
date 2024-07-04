package org.quarkuscamel.routes.xmlroute;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;
import org.quarkuscamel.entities.Person;




public class JsonToXmlRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JacksonDataFormat jsonDataFormat = new JacksonDataFormat();
        jsonDataFormat.setUnmarshalType(Person.class);

        JacksonXMLDataFormat jacksonXmlDataFormat = new JacksonXMLDataFormat();
        jacksonXmlDataFormat.setUnmarshalType(Person.class);

       from("direct:jsontoxml")
     //  from("rest:post:api/v1/jacksonxml/jsontoxml")
       .marshal(jsonDataFormat)
        .unmarshal(jsonDataFormat) // Unmarshal JSON to Data object
        .log("After marshling >> ${body}")
       .marshal(jacksonXmlDataFormat)
       .convertBodyTo(String.class)
        ;


        
    }
    
}
