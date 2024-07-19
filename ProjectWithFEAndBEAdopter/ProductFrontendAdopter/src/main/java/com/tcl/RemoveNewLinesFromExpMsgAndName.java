package com.tcl;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;

@ApplicationScoped
@Named("RemoveNewLinesFromExpMsgAndName")
@RegisterForReflection
public class RemoveNewLinesFromExpMsgAndName {

    public void removeNewLinesFromExpMsg(Exchange ex) throws Exception {

        String ExpMsg = ex.getProperty("ExpMsg", String.class);
        if (ExpMsg != null) {
            ExpMsg = ExpMsg.replace("\n", "").replace("\r", "");
            ex.setProperty("ExpMsg", ExpMsg);
        }

    }

    public void removeNewLinesFromExpName(Exchange ex) throws Exception {

        String exceptionName = ex.getIn().getHeader("exceptionName", String.class);

        if (exceptionName != null && exceptionName != "") {

            exceptionName = exceptionName.replace("\n", "").replace("\r", "");
            ex.getIn().setHeader(exceptionName, exceptionName);
        }

    }
}
