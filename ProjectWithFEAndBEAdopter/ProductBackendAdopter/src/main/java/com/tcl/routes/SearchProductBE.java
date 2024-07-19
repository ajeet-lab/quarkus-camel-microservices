package com.tcl.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;

public class SearchProductBE extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
            .useOriginalMessage()
            .handled(true)
            .setProperty("ExpMsg", simple("${exception.message}"))
            .log("In ${in.header.ConversationID} ${exchangeProperty.ConversationID} ${routeId}, Exception Occurred :: ${exception.message}")
            .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpMsg")
            .setProperty("stacktrace", simple("${exception.stacktrace}"))
            .setHeader("exceptionName", simple("${exception.getClass().getCanonicalName()}"))
            .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpName")
            .setBody(simple("{\"retStatus\": \"ERROR\",\"errorMessage\" : \"${exchangeProperty.ExpMsg}\"}"))
            .convertBodyTo(String.class)
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader("ESBExceptionOccuredAndCatchedFlag", constant("Yes"))
            .setProperty("MessageType", constant("TargetResponse"))
            .setProperty("MaskFields", simple("{{route.searchProduct_MaskFields}}"))
            .setHeader("Responsestatus", constant("Failure"))
            .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
            .wireTap("seda:loggerprocess");


        from("direct:searchproduct")
                .log("Hits recieved at ${date:now}")
                .convertBodyTo(String.class)
                .setProperty("processName", simple("{{route.searchProduct_ProcessName}}"))
                .setProperty("serviceName", simple("{{route.ServiceName}}"))
                .setProperty("transactionId", simple("${header.transactionId}"))
                .setProperty("sourceName", simple("${header.SourceName}"))
                .setProperty("conversationId", simple("${header.ConversationID}"))
                .setProperty("targetSystem", constant("DUMMYJSON"))
                .setProperty("MaskFields", simple("{{route.searchProduct_MaskFields}}"))
                .setProperty("messageType", constant("TargetRequest"))
                .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                .wireTap("seda:loggerprocess")
                .removeHeaders("*")
                .setProperty("body", simple("${body}"))

                .to("direct:OAuthTokenGen")
                .log("Token is :: ${header.access_token}")

                .choice()
                    .when(simple("${header.access_token} != '' && ${header.access_token} != null"))
                        .setBody(simple("${exchangeProperty.body}"))
                        .unmarshal().json(JsonLibrary.Jackson)
                        .setHeader("title", simple("${body[title]}"))
                    .doTry()
                    .log("In ${routeId} and conservation_id ${in.header.ConversationID} ${exchangeProperty.ConversationID} Calling target URL searchProduct_TargetURL with Request :: ${body}")
                    .toD("{{route.searchProduct_TargetURL}}&q=${header.title}")
                    .log("In ${routeId} and conservation_id ${in.header.ConversationID} ${exchangeProperty.ConversationID} Response from searchProduct_TargetURL :: ${body}")
                    .doCatch(HttpOperationFailedException.class)
                    .setProperty("ExpMsg", simple("${exception.message}"))
                    .log("In ${routeId}, ${in.header.ConversationID} ${exchangeProperty.ConversationID} HttpOperationFailedException Occurred :: ${exception.message}")
                    .setProperty("errorAt", constant("Target"))
                    .setProperty("TargetSystemName", constant("DUMMYJSON"))
                   // .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpMsg")
                    .setProperty("ErrorMessage", simple("${exchangeProperty.ExpMsg}"))
                   // .bean("org.tatacap.exception.HttpErrors", "failedMessages")
                    .bean("com.tcl.ResponseProcessor", "simplifyErrorMessage")
                    .convertBodyTo(String.class)
                    .setHeader("Responsestatus", constant("Failure"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .setProperty("stacktrace", simple("${exception.stacktrace}"))
                    .setHeader("exceptionName", simple("${exception.getClass().getCanonicalName()}"))
                    .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpName")
                    .setHeader("TargetExceptionOccuredAndCatchedFlag", constant("Yes"))
                    .doCatch(Exception.class)
                        .setProperty("ExpMsg", simple("${exception.message}"))
                        .log("In ${routeId},${in.header.ConversationID} ${exchangeProperty.ConversationID} HttpHostConnectException Occurred :: ${exception.message}")
                        .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpMsg")
                        .setBody(simple("{\"retStatus\": \"ERROR\",\"errorMessage\" : \"${exchangeProperty.ExpMsg}\"}"))
                        .convertBodyTo(String.class)
                        .setHeader("Responsestatus", constant("Failure"))
                        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .setProperty("stacktrace", simple("${exception.stacktrace}"))
                        .setHeader("exceptionName", simple("${exception.getClass().getCanonicalName()}"))
                        .bean("RemoveNewLinesFromExpMsgAndName", "removeNewLinesFromExpName")
                        .setHeader("TargetExceptionOccuredAndCatchedFlag", constant("Yes"))                     
                    .endDoTry()
                .endChoice()
                .otherwise()
                    .setBody(simple(
                            "{\"retStatus\":\"ERROR\",\"errorMessage\":\"Token Generation Failed\", \"sysErrorCode\":\"ERRDUMMYJSON500\",\"sysErrorMessage\":\"Technical error occurred at Token Generation Target\"}"))
                    .setHeader("TokenFailedFlag", constant("Yes"))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .end()

                .convertBodyTo(String.class)
                .choice()
                    .when(simple("${body} == '' || ${body} == null"))
                        .setBody(constant("{}"))
                    .end()
                .setProperty("OriginalTargetResponse", simple("${body}"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(simple("${body}"))
                .setProperty("messageType", constant("TargetResponse"))
                .setProperty("MaskFields", simple("{{route.searchProduct_MaskFields}}"))
                .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
                .wireTap("seda:loggerprocess");
    }
    
}