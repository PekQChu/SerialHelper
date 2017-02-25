package com.Serial.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.Serial.Exception.SendDateToSerialPortFailure;
import com.Serial.Exception.SerialPortOutputStreamCloseFailure;
import com.Serial.Tool.SerialTool;

import gnu.io.SerialPort;

public class SenderListener implements ActionListener{
    JTextArea inputArea;
	JTextField inputField;
	SerialPort serialPort;

	public void setInputArea(JTextArea inputArea) {
		this.inputArea = inputArea;
	}

	public void setInputField(JTextField inputField) {
		this.inputField = inputField;
	}
    
    public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}
    
	public SenderListener() {
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String sendString="";
		//获取发送的字符串
		sendString=inputField.getText();
		System.out.println(sendString);
		//展示到发送区
		inputArea.append("\r\n"+sendString+"\r\n");
		byte data[]=sendString.getBytes();
		inputField.setText("");
		//串口发送
//		try {
//			SerialTool.sendToPort(serialPort, data);
//		} catch (SendDateToSerialPortFailure e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SerialPortOutputStreamCloseFailure e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}

}
