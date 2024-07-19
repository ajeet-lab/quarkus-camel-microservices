package com.tcl;

import org.apache.camel.Exchange;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.commons.lang3.StringUtils;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("FaultMessage")
@RegisterForReflection
public class FaultMessage {

    public void httpFailedException(Exchange ex) throws Exception {

        Exception e = ex.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (e instanceof HttpOperationFailedException) {
            HttpOperationFailedException exception = (HttpOperationFailedException) e;
            String responseBody = exception.getResponseBody();
            ex.getIn().setBody(responseBody);
        } else {
            ex.getIn().setBody(e.getMessage());
        }

    }

    public void globalException(Exchange ex) throws Exception {

        Exception e = ex.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        String responseBody = e.getMessage();

        if (responseBody.contains("java.net.UnknownHostException")) {
            responseBody = StringUtils.substringBefore(responseBody, "\r\n" +
                    "	at");
        }

        ex.getIn().setBody(responseBody);
    }

}
