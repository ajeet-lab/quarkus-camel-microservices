package com.tcl;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RemoveNewLinesProcessor implements Processor {

	@Override
	public void process(Exchange ex) throws Exception {
		
		String errorMessage = ex.getProperty("errorMessage",String.class);
		errorMessage = errorMessage.replace("\n", "").replace("\r", "");
		ex.setProperty("errorMessage",errorMessage);
	}
}
