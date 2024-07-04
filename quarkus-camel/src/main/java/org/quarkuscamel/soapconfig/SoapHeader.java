package org.quarkuscamel.soapconfig;



import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapHeader {
    // Define any necessary header fields here

    @JacksonXmlProperty
    private String  username;

    @JacksonXmlProperty
    private String  password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   


    
}

