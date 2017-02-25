package com.Serial.Exception;

public class TooManyListeners extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public TooManyListeners(){}
    public String toString(){
    	return "监听端口过多！";
    }
}
