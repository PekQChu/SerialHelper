package com.Serial.Exception;

public class ReadDateFromSerialPortfailure extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public ReadDateFromSerialPortfailure(){}
    public String toString(){
    	return "从串口读取数据发生错误";
    }
}
