package com.Serial.Exception;

public class SerialPortOutputStreamCloseFailure extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SerialPortOutputStreamCloseFailure(){}
	public String toString(){
		return "关闭串口输出流发生错误！";
	}

}
