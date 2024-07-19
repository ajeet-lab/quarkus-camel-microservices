package com.tcl.routes;

import org.apache.camel.builder.RouteBuilder;

public class ProductFE  extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        onException(org.apache.camel.http.base.HttpOperationFailedException.class, java.lang.Exception.class)
                .useOriginalMessage()
                .handled(true)
                .removeHeader("org.restlet.http.headers")
                .setProperty("ErrorOccured", constant("yes"))
                .setProperty("ExpMsg", simple("${exception.message}"))
                .log("In ${routeId},${in.header.ConversationID} ${exchangeProperty.ConversationID} Exception Occurred :: ${exception.message}")
                .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpMsg")
                .setProperty("stacktrace", simple("${exception.stacktrace}"))
                .setHeader("exceptionName", simple("${exception.getClass().getCanonicalName()}"))
                .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpName")
                .setBody(simple("{\"retStatus\": \"ERROR\", \"errorMessage\" : \"${exchangeProperty.ExpMsg}\"}"))
                .convertBodyTo(String.class)
                .process("ResponseProcessor")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("TransactionId", simple("${exchangeProperty.TransactionId}"))
                .setHeader("ConversationID", simple("${exchangeProperty.ConversationID}"))
                .setHeader("ProcessName", simple("${exchangeProperty.ProcessName}"))
                .setProperty("targetSystem", simple("${header.SourceName}"))
                .setHeader("targetSystem", simple("${exchangeProperty.targetSystem}"))
                .setProperty("MessageType", simple("SourceResponse"))
                .setHeader("responseStatus", simple("Failure"))
                .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                .wireTap("seda:loggerprocess")
                .removeHeaders("*", "CurrentTimestamp|TransactionId|Sourcename|Conversationid|Content-type|CamelHttpMethod")
                .removeHeaders("Esbexceptionoccuredandcatchedflag|Targetexceptionoccuredandcatchedflag|stacktrace|Exceptionname|exceptionName|targetSystem|Processname|ProcessName|Sourcename|Access_token|Tokenfailedflag");

        from("direct:products")
                .setProperty("ProcessName", simple("{{route.product_ProcessName}}"))
                .setProperty("ServiceName", simple("{{route.ServiceName}}"))
                .setProperty("MaskFields", simple("{{route.product_MaskFields}}"))
                .setProperty("TransactionId", simple("${exchangeId}"))
                .setProperty("ConversationID", simple("${header.ConversationID}"))
                .setProperty("authorization", simple("${header.Authorization}"))
                .setProperty("SourceName", simple("${header.SourceName}"))
                .setProperty("targetSystem", simple("BACKEND_ADAPTER"))
                .setHeader("targetSystem", simple("${exchangeProperty.targetSystem}"))
                .setProperty("MessageType", simple("SourceRequest"))
                .setHeader("ProcessName", simple("${exchangeProperty.ProcessName}"))
                .setHeader("TransactionId", simple("${exchangeProperty.TransactionId}"))
                .setProperty("OriginalRequest", simple("${body}"))
                .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                // Logging Source Request
                .wireTap("seda:loggerprocess")
                .to("direct:processHeader")
                .choice()
                    .when(simple("${header.MissingHeader} == 'MissingHeader'"))
                        .setProperty("MessageType", simple("SourceResponse"))
                        .setProperty("targetSystem", simple("${in.header.SourceName}"))
                        .setHeader("targetSystem", simple("${exchangeProperty.targetSystem}"))
                        .setHeader("CamelHttpResponseCode", simple("400"))
                        .setHeader("responseStatus", simple("Failure"))
                        .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                        // Logging Source Response when Missing Header
                        .wireTap("seda:loggerprocess")
                    .endChoice()
                    .otherwise()
                        .setBody(simple("${exchangeProperty.OriginalRequest}"))
                        .convertBodyTo(String.class)
                        .setProperty("MessageType", simple("BERequest"))
                        .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                        // Logging BE Request
                        .wireTap("seda:loggerprocess")
                        .removeHeaders("*", "transactionId|TransactionId|TransactionID|ConversationID|Conversationid|ConversationId|conversationId|SourceName|Sourcename")
                        .log("In ${routeId}, ${in.header.ConversationID} ${exchangeProperty.ConversationID} Calling BE URL product_BEAdapterEndpoint with Request :: ${body}")
                        .to("{{route.product_BEAdapterEndpoint}}")
                        .log("In ${routeId}, ${in.header.ConversationID} ${exchangeProperty.ConversationID} Response from product_BEAdapterEndpoint :: ${body}")
                        .convertBodyTo(String.class)
                        .setProperty("MessageType", simple("BEResponse"))
                        .setProperty("targetSystem", simple("BACKEND_ADAPTER"))
                        .setHeader("targetSystem", simple("${exchangeProperty.targetSystem}"))
                        .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                        // Logging BE Response
                        .wireTap("seda:loggerprocess")
                        .setProperty("product_Body", simple("${body}"))
                        .setProperty("ErrorOccured", constant("no"))
                        .process("ResponseProcessor")
                        .setProperty("MessageType", simple("SourceResponse"))
                        .setProperty("targetSystem", simple("${header.SourceName}"))
                        .setHeader("targetSystem", simple("${exchangeProperty.targetSystem}"))
                        .convertBodyTo(String.class)
                        .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                        // Logging Source Response
                        .wireTap("seda:loggerprocess")
                        // Removed extra headers that were going from ESB
                        .removeHeaders("*", "CurrentTimestamp|TransactionId|Sourcename|Conversationid|Content-type|CamelHttpMethod|CamelHttpResponseCode")
                        .removeHeaders("Esbexceptionoccuredandcatchedflag|Targetexceptionoccuredandcatchedflag|stacktrace|Exceptionname|targetSystem|Processname|Sourcename|Access_token|Tokenfailedflag")
                .end();
    }
    
}
