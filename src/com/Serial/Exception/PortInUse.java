package com.Serial.Exception;

public class PortInUse extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public PortInUse(){}
    
    public String toString(){
    	return "端口被占用";
    }
}
