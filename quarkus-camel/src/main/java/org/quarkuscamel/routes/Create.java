package org.quarkuscamel.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class Create extends RouteBuilder {

    @Override
    public void configure() throws Exception {
       from("direct:create")
                .log("Recieved body is ss: ${body}")          
                .choice()
                    .when(simple("${body[0]} == null")) // Check data is an array or not
                    .bean("utils", "objectToArray")                                  
                .end()
                .log("Before spliting body >> ${body}")
                .split().simple("${body}") // split data and push into queue
                    .setExchangePattern(ExchangePattern.InOnly)
                    .to("activemq:queue:create-user")
                    .log("Data pushed into create-user queue successfully !!")
                .end()
                .bean("utils", "pushIntoQueueSuccessMSG");

        from("activemq:queue:create-user")
                .doTry()
                .setProperty("OriginalBody", simple("${body}"))  
                .setHeader("name", simple("${body[name]}"))
                .setHeader("position", simple("${body[position]}"))
                .setHeader("salary", simple("${body[salary]}"))
                .setBody(simple("{{sql.insertData}}"))
                .toD("sql:${body}") // Insert data  into database
                .log("Data inserted successfully !!")
                
                .doCatch(Exception.class)
                    .log(LoggingLevel.ERROR, "Inside exception block : ${exception.message}")
                    .setProperty("ExpMsg", simple("${exception.message}"))
                    .setBody(simple("${exchangeProperty.OriginalBody}"))
                    .log(LoggingLevel.ERROR, "exceptoin body : ${body}") 
                    .setExchangePattern(ExchangePattern.InOnly)              
                    .to("activemq:queue:create-user.DLQ")
                    .log(LoggingLevel.ERROR, "Data pushed into create-user.DLQ successfully !!")
                .endDoTry();
    }

}
