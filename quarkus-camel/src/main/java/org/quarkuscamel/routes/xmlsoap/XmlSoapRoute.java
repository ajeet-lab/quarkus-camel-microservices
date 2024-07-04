package org.quarkuscamel.routes.xmlsoap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;
import org.quarkuscamel.entities.Person;
import org.quarkuscamel.entities.SoapEnvelope;
import org.quarkuscamel.soapconfig.SoapBody;
import org.quarkuscamel.soapconfig.SoapHeader;

public class XmlSoapRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JacksonXMLDataFormat jacksonXmlDataFormat = new JacksonXMLDataFormat(SoapEnvelope.class);
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(Person.class);
        from("direct:xmltosoap")
                .unmarshal().jacksonXml()
                .marshal(jacksonDataFormat)
                .unmarshal(jacksonDataFormat)

                .process(exchange -> {
                    Person person = exchange.getIn().getBody(Person.class);          
                    // Create a new instance of SoapEnvelope
                    SoapEnvelope soapEnvelope = new SoapEnvelope();

                    // Create a new instance of SoapBody and set the unmarshalled element
                    SoapBody soapBody = new SoapBody();
                    soapBody.setPerson(person);
                    soapEnvelope.setBody(soapBody);

                    // Create a new instance of SoapHeader and add any necessary header elements
                     SoapHeader soapHeader = new SoapHeader();
                     soapHeader.setUsername(exchange.getIn().getHeader("username", String.class));
                     soapHeader.setPassword(exchange.getIn().getHeader("password", String.class));
                     soapEnvelope.setHeader(soapHeader);

                    exchange.getIn().setBody(soapEnvelope);
                })
                .marshal(jacksonXmlDataFormat)
                .log("After SOAP marshalling :: ${body}");
    }

}
