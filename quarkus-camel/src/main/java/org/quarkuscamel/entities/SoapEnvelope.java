package org.quarkuscamel.entities;

import org.quarkuscamel.soapconfig.SoapBody;
import org.quarkuscamel.soapconfig.SoapHeader;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapEnvelope {
     @JacksonXmlProperty(localName = "Header")
    private SoapHeader header;

    @JacksonXmlProperty(localName = "Body")
    private SoapBody body;

    public SoapHeader getHeader() {
        return header;
    }

    public void setHeader(SoapHeader header) {
        this.header = header;
    }

    public SoapBody getBody() {
        return body;
    }

    public void setBody(SoapBody body) {
        this.body = body;
    }

}


