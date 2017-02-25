package com.Serial.Exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionWriter {
public static String getErrorInfoFromException(Exception e){
	StringWriter sw=null;
	PrintWriter pw=null;
	try {
		sw=new StringWriter();
		pw=new PrintWriter(sw);
		e.printStackTrace(pw);
		return "\r\n"+sw.toString()+"\r\n";
	} catch (Exception e2) {
		return "出错了，未捕获错误信息，请检查重试";
	}finally{
		try {
			if(pw!=null){
				pw.close();
			}
			if(sw!=null){
				sw.close();
			}
		} catch (IOException e3) {
			// TODO: handle exception
			e3.printStackTrace();
		}
	}
}
}
