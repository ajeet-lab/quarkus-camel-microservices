package org.quarkuscamel.routes.email;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class SendEmail extends RouteBuilder{

    @Override
    public void configure() throws Exception {
       from("direct:sendemail")
       .removeHeaders("*", "Content-Type")
            .setHeader("To").simple("${body[to]}")
            .setHeader("Cc").simple("${body[cc]}")
            .setHeader("Subject").simple("${body[subject]}")         
            .setHeader("Content-Type", simple("text/html"))
            .to("velocity:configfiles/email.vm") 
            .to("{{smtp}}?username={{from}}&password={{password}}")
            .setHeader("Content-Type", simple("application/json"))
            .setBody(simple("{\"message\": \"Mail sent successfully !!\"}"))
            .unmarshal().json(JsonLibrary.Jackson)   
            .log("Mail sent successfully");
    }
    
}
