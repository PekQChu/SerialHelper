package com.Serial.Exception;

public class SerialPortInputStreamCloseFailure extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public SerialPortInputStreamCloseFailure(){}
    public String toString(){
    	return "关闭串口输入流错误！";
    }
}
