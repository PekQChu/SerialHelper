package com.Serial.Exception;

public class NotASerialPort extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public  NotASerialPort(){}
    public String toString(){
    	return "这不是一个串口!";
    }
}
