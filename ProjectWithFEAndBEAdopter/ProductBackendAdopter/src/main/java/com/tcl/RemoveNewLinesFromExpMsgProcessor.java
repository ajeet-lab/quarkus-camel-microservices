package com.tcl;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("RemoveNewLinesFromExpMsgProcessor")
public class RemoveNewLinesFromExpMsgProcessor implements Processor {

	@Override
	public void process(Exchange ex) throws Exception {

		String ExpMsg = ex.getProperty("ExpMsg", String.class);
		if(ExpMsg!=null) {
			ExpMsg = ExpMsg.replace("\n", "").replace("\r", "");
			ex.setProperty("ExpMsg", ExpMsg);
		}
	}
}
