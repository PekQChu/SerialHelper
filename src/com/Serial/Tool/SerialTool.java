package com.Serial.Tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;


import com.Serial.Exception.*;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;


public class SerialTool {
  private static SerialTool serialTool=null;
  
  static{
	  if(serialTool==null){
		  serialTool=new SerialTool();
	  }
  }
  //此类不能被实例化
  private SerialTool(){
	  
  }
  
  public static SerialTool getSerialTool(){
	  if(serialTool==null){
		 serialTool=new SerialTool();
	  }
	  return serialTool;
  }
  /*
   * 获取所有的端口
   * 
   */
  public static ArrayList<String>findPort(){
	  Enumeration<CommPortIdentifier>portList=CommPortIdentifier.getPortIdentifiers();
	  ArrayList<String>portNameList=new ArrayList<>();
	  while(portList.hasMoreElements()){
		  String portName=portList.nextElement().getName();
		  portNameList.add(portName);
	  }
	  return portNameList;
  }
  /*
   * 打开串口
   */
  public static final SerialPort openPort(String portName,int baudrate)throws 
  SerialPortParameterFailure,NotASerialPort,NoSuchPort,PortInUse{
	  try {
		//通过端口名识别端口
		  CommPortIdentifier portIdentifier=CommPortIdentifier.getPortIdentifier(portName);
		//打开端口并且给端口名字和一个timeout
		 CommPort commPort=portIdentifier.open(portName,2000);
		 //判断是不是串口
		 if(commPort instanceof SerialPort){
			 SerialPort serialPort=(SerialPort)commPort;
			 try {
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				// TODO: handle exception
				throw new SerialPortParameterFailure();
			}
			 return serialPort;
		 }
		 else{
			 throw new NotASerialPort();
		 }
	} catch (NoSuchPortException e1) {
		// TODO: handle exception
		throw new NoSuchPort();
	}catch(PortInUseException e2){
		throw new PortInUse();
	}
  }
  /*
   * 关闭串口
   */
  public static final void closePort(SerialPort serialPort){
	  if(serialPort!=null){
		  serialPort.close();
		  serialPort=null;
	  }
  }
  /*
   * 串口读取数据
   * 
   * 
   */
  public static byte[] readFromPort(SerialPort serialPort)throws 
  ReadDateFromSerialPortfailure,SerialPortInputStreamCloseFailure{
	  InputStream in=null;
	  byte[]bytes=null;
	  try {
		in=serialPort.getInputStream();
		int bufflength=in.available();
		while(bufflength!=0){
			bytes=new byte[bufflength];
			in.read(bytes);
			bufflength=in.available();
		}
	} catch (IOException e) {
		// TODO: handle exception
		throw new ReadDateFromSerialPortfailure();
	}finally{
		try {
			if(in!=null){
				in.close();
				in=null;
			}
		} catch (Exception e2) {
			throw new SerialPortInputStreamCloseFailure();
		}
	}
	  return bytes;
  }
  /*
   * 串口发送数据
   */
  public static void sendToPort(SerialPort serialPort,byte[]order)throws 
  SendDateToSerialPortFailure,SerialPortOutputStreamCloseFailure{
	  OutputStream out=null;
	  try {
		out=serialPort.getOutputStream();
		out.write(order);
		out.flush();
	} catch ( IOException e) {
		// TODO: handle exception
		throw new SendDateToSerialPortFailure();
	}finally{
		try{
			if(out!=null){
				out.close();
				out=null;
			}
		}catch(IOException e){
			throw new SerialPortOutputStreamCloseFailure();
		}
	}
  }
  
  /*
   * 添加一个监听器
   */
  public static void addListener(SerialPort port,SerialPortEventListener listener)throws 
  TooManyListeners{
	  try {
		  //添加监听
		port.addEventListener(listener);
		//设置数据到达时唤醒监听
		port.notifyOnDataAvailable(true);
		//设置当通信中断时唤醒中断线程
		port.notifyOnBreakInterrupt(true);
	} catch (Exception e) {
		// TODO: handle exception
		throw new TooManyListeners();
	}
  }
}


