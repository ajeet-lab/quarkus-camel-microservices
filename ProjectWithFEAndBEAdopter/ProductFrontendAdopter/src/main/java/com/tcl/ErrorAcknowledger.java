package com.tcl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@ApplicationScoped
@Named("ErrorAckProcessor")
public class ErrorAcknowledger  implements Processor{

	public void process (Exchange ex)throws Exception{

		ErrorResponse ErrResp = new ErrorResponse();

		String ErrorDesc = "Invalid Request. Missing ESB Headers. Please send the Request with proper ESB Headers.";

		String ErrorCode = "ERRESB205";

		ErrResp.setRetStatus("ERROR");
		ErrResp.setErrorMessage(ErrorDesc);
		ErrResp.setSysErrorCode(ErrorCode);
		ErrResp.setSysErrorMessage("The header has mandatory field blank");
		
		ex.getIn().setBody(ErrResp);
	}
}
