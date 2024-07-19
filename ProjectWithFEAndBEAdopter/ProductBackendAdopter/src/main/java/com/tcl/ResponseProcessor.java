package com.tcl;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.*;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;


@ApplicationScoped
@Named("ResponseProcessor")
public class ResponseProcessor implements Processor{

    @Override
    public void process(Exchange ex) throws Exception {


    }

    public void simplifyErrorMessage(Exchange ex) throws Exception {

        String jsonBody = ex.getIn().getBody(String.class);
        JSONObject modifiedBody = new JSONObject(jsonBody);
        String errorBody = modifiedBody.get("errorMessage").toString();
        modifiedBody.put("errorMessage",errorBody);
        ex.getIn().setBody(modifiedBody);

    }


}
