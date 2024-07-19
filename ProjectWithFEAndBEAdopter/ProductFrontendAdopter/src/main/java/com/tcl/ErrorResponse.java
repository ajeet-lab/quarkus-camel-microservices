package com.tcl;

public class ErrorResponse {

	String retStatus;
	String errorMessage;
	String sysErrorCode;
	String sysErrorMessage;
	
	public String getRetStatus() {
		return retStatus;
	}
	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSysErrorCode() {
		return sysErrorCode;
	}
	public void setSysErrorCode(String sysErrorCode) {
		this.sysErrorCode = sysErrorCode;
	}
	public String getSysErrorMessage() {
		return sysErrorMessage;
	}
	public void setSysErrorMessage(String sysErrorMessage) {
		this.sysErrorMessage = sysErrorMessage;
	}
	@Override
	public String toString() {
		return "ErrorResponse [retStatus=" + retStatus + ", errorMessage=" + errorMessage + ", sysErrorCode="
				+ sysErrorCode + ", sysErrorMessage=" + sysErrorMessage + "]";
	}
}
