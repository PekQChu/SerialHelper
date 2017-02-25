package com.Serial.Exception;

public class NoSuchPort extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	public NoSuchPort(){}
	public String toString(){
		return "没有这样的串口！";
	}
} 
