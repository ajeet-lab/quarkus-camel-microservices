package com.tcl;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RemoveNewLinesFromExpMsgProcessor implements Processor {

	@Override
	public void process(Exchange ex) throws Exception {

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
