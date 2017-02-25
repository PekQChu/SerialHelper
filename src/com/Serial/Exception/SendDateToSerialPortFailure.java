package com.Serial.Exception;

public class SendDateToSerialPortFailure extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public SendDateToSerialPortFailure(){}
    public String toString(){
    	return "串口发送数据发送错误";
    }
}
