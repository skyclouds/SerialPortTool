package com.biapp.serialport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.biapp.util.PrintfUtil;

import aura.data.Bytes;
import purejavacomm.CommPortIdentifier;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

/**
 * @author Yun
 */
public class SerialPortClient {

    /**
     * 串口
     */
    private SerialPort serialPort;

    /**
     * 设备名称 
     * 常见设备名: 
     * COM3 
     * COM5
     */
    protected String devName;

    /**
     * 波特率 
     * 常见波特率: 
     * 1200 
     * 2400 
     * 4800 
     * 9600 
     * 14400 
     * 28800 
     * 38400 
     * 19200 
     * 57600 
     * 115200
     */
    protected int baudRate = 9600;

    /**
     * 奇偶性
     * 
     * @see SerialPort 
     * SerialPort.PARITY_NONE = 0 
     * SerialPort.PARITY_ODD = 1
     * SerialPort.PARITY_EVEN = 2 
     * SerialPort.PARITY_MARK = 3
     * SerialPort.PARITY_SPACE = 4
     */
    protected int parity = SerialPort.PARITY_NONE;

    /**
     * 数据位
     * 
     * @see SerialPort 
     * SerialPort.DATABITS_5 = 5 
     * SerialPort.DATABITS_6 = 6
     * SerialPort.DATABITS_7 = 7 
     * SerialPort.DATABITS_8 = 8
     */
    protected int dataBits = SerialPort.DATABITS_8;

    /**
     * 停止数据位
     * 
     * @see SerialPort 
     * SerialPort.STOPBITS_1 = 1 
     * SerialPort.STOPBITS_2 = 2
     * SerialPort.STOPBITS_1_5 = 3
     */
    protected int stopBits = SerialPort.STOPBITS_1;

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 输出流
     */
    private OutputStream outputStream;

    /**
     * 连接超时
     */
    protected int connectTimeOut = 2000;

    public SerialPortClient(String devName, int baudRate, int parity, int dataBits, int stopBits) {
        this.devName = devName;
        this.baudRate = baudRate;
        this.parity = parity;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    /**
     * 打开串口
     * 
     * @return
     */
    public boolean open() {
        boolean open = false;
        try {
            serialPort = (SerialPort) getCommPortIdentifier().open(this.getClass().getName() + "@" + this.hashCode(),connectTimeOut);
            serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            open = true;
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return open;
    }

    /**
     * 发送数据
     *
     * @param sendData
     * @return
     */
    public boolean sendData(byte[] sendData) throws IOException {
        boolean send = false;
        PrintfUtil.d(devName + "-Send", Bytes.toHexString(sendData));
        if (outputStream != null) {
            outputStream.write(sendData);
            outputStream.flush();
            send = true;
        } else {
            PrintfUtil.e(devName + "-Send", "outputStream is null");
        }
        return send;
    }

    /***
     * 读取数据
     * 
     * @param len
     * @param timeOut
     * @return
     */
    public byte[] readData(int len, long timeOut) throws TimeoutException {
        if (len <= 0) {
            return null;
        }
        byte[] read = new byte[len];
        try {
            int index = 0;
            long start = System.currentTimeMillis();
            do {
                if (inputStream != null) {
                    if (inputStream.available() > 0) {
                        inputStream.read(read, index, 1);
                        index++;
                    } else {
                        // PrintfUtil.e(deviceName + "-Read", "available=" + inputStream.available());
                    }
                } else {
                    PrintfUtil.e(devName + "-Read", "inputStream is null");
                }
                if ((System.currentTimeMillis() - start) > timeOut) {
                    if (index == 0) {
                        throw new TimeoutException(devName + "read " + "time out");
                    } else {
                        PrintfUtil.e(devName + "-Read", "read buffer=" + index + "(" + "request=" + len + ")");
                        read = Bytes.subBytes(read, 0, index);
                        break;
                    }
                }
            } while (index < len && inputStream != null);
        } catch (IOException e) {
            e.printStackTrace();
            read = null;
        }
        if (read != null) {
            PrintfUtil.d(devName + "-Read", Bytes.toHexString(read));
        }
        if (inputStream == null) {
            PrintfUtil.e(devName + "-Read", "inputStream is null");
        }
        return read;
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            if (serialPort != null) {
                serialPort.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否存在
     * 
     * @return
     */
    public boolean exists() {
        return getSerialPortList().contains(devName);
    }

    /**
     * 获得串口列表
     * 
     * @return
     */
    public static List<String> getSerialPortList() {
        List<String> list = new ArrayList<String>();
        Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
        while (enumeration.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) enumeration.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                list.add(portId.getName());
            }
        }
        return list;
    }

    /**
     * 获得串口
     * 
     * @return
     */
    private CommPortIdentifier getCommPortIdentifier() {
        CommPortIdentifier commPortIdentifier = null;
        Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
        while (enumeration.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) enumeration.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(devName)) {
                    commPortIdentifier = portId;
                }
            }
        }
        return commPortIdentifier;
    }
}
