package com.Serial.Main;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.Serial.Exception.ExceptionWriter;
import com.Serial.Exception.NoSuchPort;
import com.Serial.Exception.NotASerialPort;
import com.Serial.Exception.PortInUse;
import com.Serial.Exception.ReadDateFromSerialPortfailure;
import com.Serial.Exception.SendDateToSerialPortFailure;
import com.Serial.Exception.SerialPortInputStreamCloseFailure;
import com.Serial.Exception.SerialPortOutputStreamCloseFailure;
import com.Serial.Exception.SerialPortParameterFailure;
import com.Serial.Exception.TooManyListeners;
import com.Serial.Tool.SerialTool;
//import com.Serial.Tool.DataView.SerialListener;
//import com.Serial.Tool.DataView.RepaintThread;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class MainGUI extends JFrame{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private JTextField inputText; //底部文本输入框
private JTextArea  showInput,showOutput; //输入显示 输出显示
private JButton send;  //发送按钮
private Font font=new Font("微软雅黑",Font.BOLD,15);
private JLabel version=new JLabel("version-1.0 ©chuchengE");
//private JLabel inputArea=new JLabel("发送数据区");
//private JLabel outputArea=new JLabel("接收数据区");
private JLabel bpsArea=new JLabel("波特率");
private JLabel commArea=new JLabel("端口");
private JPanel commPanel=new JPanel();
private JPanel sendPanel=new JPanel();
private JPanel inputPanel=new JPanel();
private JPanel outputPanel=new JPanel();
private JPanel basePanel=new JPanel();
private Choice commChoice=new Choice(); //串口选择
private Choice bpsChoice=new Choice(); //波特率选择
private JButton openSerialButton=new JButton("打开串口"); 
private SenderListener senderListener;
//未加监视器

//保存可用端口号
private List<String> commList=null;
//保存串口对象
private SerialPort serialPort=null;


public MainGUI(){
	init();
	setVisible(true);
	setBackground(Color.white);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	commList=SerialTool.findPort(); //程序初始化就扫描一下有效串口
	this.addWindowListener(new WindowAdapter(){
	      public void windowClosing(WindowEvent arg0){
	    	  if(serialPort!=null){
	    		  SerialTool.closePort(serialPort);
	    	  }
	    	  System.exit(0);
	      }
	});
}

private void init(){
	setLayout(null);//绝对布局
	//右下脚
	inputText=new JTextField();
	inputText.setBounds(10, 20, 570, 40);
	send=new JButton("发送");
	send.setBounds(584, 20, 60, 40);
	//senderListener=new SenderListener();
	//senderListener.setInputArea(showInput);
	//senderListener.setInputField(inputText);
	//senderListener.setSerialPort(serialPort);
	send.addActionListener(new SendListener());
	version.setBounds(180, 55, 200, 50);
	sendPanel.setLayout(null);
	sendPanel.setBounds(310, 500, 650, 100);
	sendPanel.setBorder(new TitledBorder("发送区"));
    sendPanel.add(send);
    sendPanel.add(inputText);
    sendPanel.add(version);
    add(sendPanel);
    
	showInput=new JTextArea();
	showInput.setBounds(10, 20, 300, 460);
	inputPanel.setLayout(null);
	inputPanel.setBounds(310, 10, 320, 490);
	inputPanel.setBorder(new TitledBorder("发送数据区"));
	inputPanel.add(showInput);
	add(inputPanel);
	showOutput=new JTextArea();
	showOutput.setBounds(10, 20, 300,460);
	outputPanel.setLayout(null);
	outputPanel.setBounds(650, 10, 320, 490);
	outputPanel.setBorder(new TitledBorder("接收数据区"));
	outputPanel.add(showOutput);
	add(outputPanel);
	//左边栏
	//添加波特率选项
    bpsChoice.setBounds(100, 20, 150, 20);
    bpsArea.setBounds(40,20,50,20);
    bpsChoice.add("1200");
    bpsChoice.add("2400");
    bpsChoice.add("4800");
    bpsChoice.add("9600");
    bpsChoice.add("14400");
    bpsChoice.add("19200");
    bpsChoice.add("115200");
    //add(bpsChoice);
    //add(bpsArea);
    //串口选项 
    commChoice.setBounds(100, 45, 150, 20);
    if(commList==null||commList.size()<1){
    	JOptionPane.showMessageDialog(null,"没有搜索到有效串口！","错误",JOptionPane
    			.INFORMATION_MESSAGE);
    }
    else{
    	for(String s:commList){
    		commChoice.add(s);
    	}
    }
    commArea.setBounds(40,45,50,20);
    add(commChoice);
    openSerialButton.setBounds(60, 70, 150, 20);
    commPanel.setBounds(10,500,270,100);
    commPanel.setBorder(new TitledBorder("串口设置"));
    commPanel.setLayout(null);
    commPanel.add(bpsChoice);
    commPanel.add(bpsArea);
    commPanel.add(commChoice);
    commPanel.add(commArea);
    commPanel.add(openSerialButton);
    add(commPanel);
    //设置蓝牙通信相关的
    basePanel.setLayout(null);
    basePanel.setBackground(Color.gray);
    basePanel.setBounds(10, 10, 270, 485);
    basePanel.setBorder(new TitledBorder("AT指令设置"));
    add(basePanel);
    
    openSerialButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//获取串口名称                                            
			String commName=commChoice.getSelectedItem();
			//获取波特率
			String bpsStr=bpsChoice.getSelectedItem();
			//检查串口名称是否正确
			if(commName==null||commName.equals("")){
				JOptionPane.showMessageDialog(null, "没有搜索到有效串口！", "错误", JOptionPane.INFORMATION_MESSAGE);  
			}
			else{
				//检查波特率是否获取正确
                if (bpsStr == null || bpsStr.equals("")) {
                    JOptionPane.showMessageDialog(null, "波特率获取错误！", "错误", JOptionPane.INFORMATION_MESSAGE);
			}
                else{
					//串口名、波特率均获取正确时
                    int bps = Integer.parseInt(bpsStr);
                    try {
                    	//获取指定端口名及波特率的串口对象
                        serialPort = SerialTool.openPort(commName, bps);
                        //在该串口对象上添加监听器
                        SerialTool.addListener(serialPort, new SerialListener());
                        //监听成功进行提示
                        JOptionPane.showMessageDialog(null, "监听成功，稍后将显示监测数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
					} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
						// TODO: handle exception
						  JOptionPane.showMessageDialog(null, e1, "错误", JOptionPane.INFORMATION_MESSAGE);
					}
				}
		}
			
	  }
	});
    new Thread(new RepaintThread()).start();
}

/**
 * 以内部类形式创建一个串口监听类
 */
private class SerialListener implements SerialPortEventListener {
    
    /**
     * 处理监控到的串口事件
     */
    public void serialEvent(SerialPortEvent serialPortEvent) {
        
        switch (serialPortEvent.getEventType()) {

            case SerialPortEvent.BI: // 10 通讯中断
                JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
                break;

            case SerialPortEvent.OE: // 7 溢位（溢出）错误

            case SerialPortEvent.FE: // 9 帧错误

            case SerialPortEvent.PE: // 8 奇偶校验错误

            case SerialPortEvent.CD: // 6 载波检测

            case SerialPortEvent.CTS: // 3 清除待发送数据

            case SerialPortEvent.DSR: // 4 待发送数据准备好了

            case SerialPortEvent.RI: // 5 振铃指示

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
                break;
            
            case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
                
                //System.out.println("found data");
                byte[] data = null;
                
                try {
                    if (serialPort == null) {
                        JOptionPane.showMessageDialog(null, "串口对象为空！监听失败！", "错误", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        data = SerialTool.readFromPort(serialPort);    //读取数据，存入字节数组
                        System.out.println(new String(data));
                    	System.out.println("检查数据是否读取正确   ");
                   // 自定义解析过程，你在实际使用过程中可以按照自己的需求在接收到数据后对数据进行解析
                        if (data == null || data.length < 1) {    //检查数据是否读取正确    
                        	System.out.println("检查数据是否读取正确   ");
                            JOptionPane.showMessageDialog(null, "读取数据过程中未获取到有效数据！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
                           // System.exit(0);
                        }
                        else {
                            String dataOriginal = new String(data);    //将字节数组数据转换位为保存了原始数据的字符串
                           // String dataValid = "";    //有效数据（用来保存原始数据字符串去除最开头*号以后的字符串）
                           // String[] elements = null;    //用来保存按空格拆分原始字符串后得到的字符串数组    
                            //接收区展示
                            showOutput.append("\r\n"+dataOriginal+"\r\n");
                            //解析数据
                            System.out.println("dataOriginal");
                            System.out.println(dataOriginal);
//                            if (dataOriginal.charAt(0) == '1') {    //当数据的第一个字符是*号时表示数据接收完成，开始解析                            
//                                dataValid = dataOriginal.substring(1);
//                                elements = dataValid.split("2");
//                                System.out.println("检查数据是否解析正确");
//                                if (elements == null || elements.length < 1) {    
//                                	//检查数据是否解析正确
//                                    JOptionPane.showMessageDialog(null, "数据解析过程出错，请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
//                                    //System.exit(0);
//                                }
//                                else {
//                                    try {
//                                        //更新界面Label值
//                                        for (int i=0; i<elements.length; i++) {
//                                            System.out.println(elements[i]);
//                                        }
//                                        //System.out.println("win_dir: " + elements[5]);
////                                        tem.setText(elements[0] + " ℃");
////                                        hum.setText(elements[1] + " %");
////                                        pa.setText(elements[2] + " hPa");
////                                        rain.setText(elements[3] + " mm");
////                                        win_sp.setText(elements[4] + " m/s");
////                                        win_dir.setText(elements[5] + " °");
//                                    } catch (ArrayIndexOutOfBoundsException e) {
//                                        JOptionPane.showMessageDialog(null, "数据解析过程出错，更新界面数据失败！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
//                                        System.exit(0);
//                                    }
//                                }    
//                            }
                        }
                        
                    }                        
                    
                } catch (ReadDateFromSerialPortfailure | SerialPortInputStreamCloseFailure e) {
                    JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);    //发生读取错误时显示错误信息后退出系统
                }    
                
                break;

        }

    }

}
/*
 * 重画线程（每隔30毫秒重画一次）
 */
private class RepaintThread implements Runnable {
    public void run() {
        while(true) {
            //调用重画方法
            repaint();
            //扫描可用串口
            commList = SerialTool.findPort();
            if (commList != null && commList.size()>0) {
                //添加新扫描到的可用串口
                for (String s : commList) {
                    //该串口名是否已存在，初始默认为不存在（在commList里存在但在commChoice里不存在，则新添加）
                    boolean commExist = false;    
                    for (int i=0; i<commChoice.getItemCount(); i++) {
                        if (s.equals(commChoice.getItem(i))) {
                            //当前扫描到的串口名已经在初始扫描时存在
                            commExist = true;
                            break;
                        }                    
                    }
                    if (commExist) {
                        //当前扫描到的串口名已经在初始扫描时存在，直接进入下一次循环
                        continue;
                    }
                    else {
                        //若不存在则添加新串口名至可用串口下拉列表
                        commChoice.add(s);
                    }
                }
                
                //移除已经不可用的串口
                for (int i=0; i<commChoice.getItemCount(); i++) {
                    
                    //该串口是否已失效，初始默认为已经失效（在commChoice里存在但在commList里不存在，则已经失效）
                    boolean commNotExist = true;    
                    
                    for (String s : commList) {
                        if (s.equals(commChoice.getItem(i))) {
                            commNotExist = false;    
                            break;
                        }
                    }
                    
                    if (commNotExist) {
                        //System.out.println("remove" + commChoice.getItem(i));
                        commChoice.remove(i);
                    }
                    else {
                        continue;
                    }
                }
                
            }
            else {
                //如果扫描到的commList为空，则移除所有已有串口
                commChoice.removeAll();
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                String err = ExceptionWriter.getErrorInfoFromException(e);
                JOptionPane.showMessageDialog(null, err, "错误", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }
    
}
/*
 * 添加发送的监听事件
 */
private class SendListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String sendString="";
		//获取发送的字符串
		sendString=inputText.getText();
		System.out.println(sendString);
		//展示到发送区
		showInput.append("\r\n"+sendString+"\r\n");
		byte data[]=sendString.getBytes();
		inputText.setText("");
		//串口发送
		try {
			SerialTool.sendToPort(serialPort, data);
		} catch (SendDateToSerialPortFailure e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SerialPortOutputStreamCloseFailure e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	}
}
