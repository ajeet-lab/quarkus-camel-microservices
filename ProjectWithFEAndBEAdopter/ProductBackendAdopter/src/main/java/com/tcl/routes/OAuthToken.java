package com.tcl.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;

public class OAuthToken extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
        .id("_NewProcessAUTH__onException1")
        //.redeliveryPolicyRef("myRedeliveryPolicyConfig")
        .useOriginalMessage()
        .handled(true)
        .setProperty("ExpMsg", simple("${exception.message}"))
        .bean("FaultMessage", "globalException")
        .setHeader("Content-Type", simple("application/json"))
        .setBody(simple("{\"Status\":\"01\",\"Remarks\":\"${body}\"}"))
        .convertBodyTo(String.class);
        
        from("direct:OAuthTokenGen")
           .routeId("_NewOAuthTokenRoute1234555")
           .removeHeaders("*")
           .setHeader("CamelHttpMethod", simple("POST"))
           .setHeader("Content-Type", simple("application/json"))
           .setBody(simple("\"OAuth Request Call\""))
           .setProperty("MessageType", simple("TargetRequest"))
           .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
           .wireTap("seda:loggerprocess")
           .setBody(simple("{{OauthRequest}}"))
           .setProperty("OriginalTokenRequest", simple("${bodyAs(String).trim()}"))
           .removeHeader("org.restlet.http.headers")
           .doTry()
               .to("{{Oauth.URL}}?socketTimeout={{SocketTimeout}}&connectTimeout={{ConnectTimeout}}&maxTotalConnections=4000&connectionsPerRoute=50")
           .doCatch(HttpOperationFailedException.class)
               .bean("FaultMessage", "httpFailedException")
               .convertBodyTo(String.class)
               .setProperty("ErrormessageDtl", jsonpath("$", String.class))
               .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
               .setProperty("MessageType", simple("TargetResponse"))
               .setBody(simple("{\"Status\":\"01\",\"Response\":\"${exchangeProperty.ErrormessageDtl}\",\"Remarks\":\"HTTP operation failed invoking token generation api\"}"))
               .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
               .wireTap("seda:loggerprocess")
               .convertBodyTo(String.class)
               .stop().endDoCatch()
           .endDoTry()
           .end()
           .convertBodyTo(String.class)
           .setHeader("access_token", jsonpath("$..token", String.class))
           .setProperty("OriginalTokenResponse", simple("${bodyAs(String).trim()}"))
           .choice()
               .when(simple("${header.access_token} != \"\" && ${header.access_token} != null"))
                   .setBody(simple("\"OAuth Generated Successfully\""))
               .otherwise()
                   .setBody(simple("\"OAuth Generation Unsuccessful\""))
               .end()
           .setProperty("MessageType", simple("TargetResponse"))
           .setProperty("CurrentTimestamp", simple("${date-with-timezone:now:IST:dd-MMM-yyyy HH:mm:ss.SSS}"))
           .wireTap("seda:loggerprocess")
           .setBody(simple("${exchangeProperty.OriginalTokenResponse}"));

           

    }
    
}
