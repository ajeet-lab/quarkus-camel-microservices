package org.quarkuscamel.soapconfig;

import org.quarkuscamel.entities.Person;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapBody {
    @JacksonXmlProperty(localName = "Person")
    //private YourElementClass yourElement;
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
}
