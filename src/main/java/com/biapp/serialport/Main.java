package com.biapp.serialport;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biapp.util.PrintfUtil;

import aura.data.Bytes;

public class Main {

    // 设备名
    public static List<String> devNames;
    // 设备名
    public static String devName;
    // 波特率
    public static int baudRate;
    // 奇偶性
    public static int parity;
    // 数据位
    public static int dataBits;
    // 停止数据位
    public static int stopBits;
    // 串口设备
    public static SerialPortClient client;

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        devNames = new ArrayList<String>();
        System.out.println("==========串口工具==========");
        System.out.println("1.显示设备名称");
        System.out.println("2.退出");
        System.out.print("请选择:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            String input = scan.next();
            if (input.equals("1")) {
                for (int i = 0; i < SerialPortClient.getSerialPortList().size(); i++) {
                    devNames.add(SerialPortClient.getSerialPortList().get(i));
                }
                setDevName(devNames);
            } else if (input.equals("2")) {
                System.out.println("谢谢使用再见!!!");
                System.exit(0);
            } else {
                mainMenu();
            }
        }
        scan.close();
    }

    private static void setDevName(List<String> devNames) {
        System.out.println("==========选择设备==========");
        List<String> devId = new ArrayList<String>();
        for (int i = 0; i < devNames.size(); i++) {
            System.out.println((i + 1) + "." + devNames.get(i));
            devId.add((i + 1) + "");
        }
        System.out.println("0.返回");
        System.out.print("请选择设备:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            String input = scan.next();
            if (input.equals("0")) {
                mainMenu();
            } else if (devId.contains(input)) {
                devName = devNames.get(Integer.parseInt(input) - 1);
                setBaudRate();
            } else {
                setDevName(devNames);
            }
        }
        scan.close();
    }

    private static void setBaudRate() {
        System.out.println("==========设置波特率==========");
        System.out.println("1." + "1200");
        System.out.println("2." + "2400");
        System.out.println("3." + "4800");
        System.out.println("4." + "9600");
        System.out.println("5." + "14400");
        System.out.println("6." + "28800");
        System.out.println("7." + "38400");
        System.out.println("8." + "19200");
        System.out.println("9." + "57600");
        System.out.println("10." + "115200");
        System.out.println("0." + "返回");
        System.out.print("请选择波特率:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            String input = scan.next();
            if (input.equals("0")) {
                setDevName(devNames);
            } else if (input.equals("1")) {
                baudRate = 1200;
                setParity();
            } else if (input.equals("2")) {
                baudRate = 2400;
                setParity();
            } else if (input.equals("3")) {
                baudRate = 4800;
                setParity();
            } else if (input.equals("4")) {
                baudRate = 9600;
                setParity();
            } else if (input.equals("5")) {
                baudRate = 14400;
                setParity();
            } else if (input.equals("6")) {
                baudRate = 28800;
                setParity();
            } else if (input.equals("7")) {
                baudRate = 38400;
                setParity();
            } else if (input.equals("8")) {
                baudRate = 19200;
                setParity();
            } else if (input.equals("9")) {
                baudRate = 57600;
                setParity();
            } else if (input.equals("10")) {
                baudRate = 115200;
                setParity();
            } else {
                setDevName(devNames);
            }
        }
        scan.close();
    }

    private static void setParity() {
        System.out.println("==========设置奇偶校验位==========");
        System.out.println("1." + "NONE");
        System.out.println("2." + "ODD");
        System.out.println("3." + "EVEN");
        System.out.println("4." + "MARK");
        System.out.println("5." + "SPACE");
        System.out.println("0." + "返回");
        System.out.print("请选择奇偶校验位:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            String input = scan.next();
            if (input.equals("0")) {
                setBaudRate();
            } else if (input.equals("1")) {
                parity = 0;
                setDataBits();
            } else if (input.equals("2")) {
                parity = 1;
                setDataBits();
            } else if (input.equals("3")) {
                parity = 2;
                setDataBits();
            } else if (input.equals("4")) {
                parity = 3;
                setDataBits();
            } else if (input.equals("5")) {
                parity = 4;
                setDataBits();
            } else {
                setParity();
            }
        }
        scan.close();
    }

    private static void setDataBits() {
        System.out.println("==========设置数据位==========");
        System.out.println("1." + "5位");
        System.out.println("2." + "6位");
        System.out.println("3." + "7位");
        System.out.println("4." + "8位");
        System.out.println("0." + "返回");
        System.out.print("请选择数据位:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            String input = scan.next();
            if (input.equals("0")) {
                setParity();
            } else if (input.equals("1")) {
                dataBits = 5;
                setStopBits();
            } else if (input.equals("2")) {
                dataBits = 6;
                setStopBits();
            } else if (input.equals("3")) {
                dataBits = 7;
                setStopBits();
            } else if (input.equals("4")) {
                dataBits = 8;
                setStopBits();
            } else {
                setDataBits();
            }
        }
        scan.close();
    }

    private static void setStopBits() {
        System.out.println("==========设置停止位==========");
        System.out.println("1." + "1位");
        System.out.println("2." + "2位");
        System.out.println("3." + "3位");
        System.out.println("0." + "返回");
        System.out.print("请选择停止位:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            String input = scan.next();
            if (input.equals("0")) {
                setDataBits();
            } else if (input.equals("1")) {
                stopBits = 1;
                openDev(true);
            } else if (input.equals("2")) {
                stopBits = 2;
                openDev(true);
            } else if (input.equals("3")) {
                stopBits = 3;
                openDev(true);
            } else {
                setStopBits();
            }
        }
        scan.close();
    }

    private static void openDev(boolean openDev) {
        boolean open=true;
        if(openDev){
            client = new SerialPortClient(devName, baudRate, parity, dataBits, stopBits);
            open=client.open();
        }
        if (open) {
            System.out.println("==========发送数据==========");
            System.out.println("16进制字符串");
            System.out.println("0." + "返回");
            System.out.print(devName+"-Send:");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNext()) {
                String input = scan.next();
                if (input.equals("0")) {
                    client.close();
                    mainMenu();
                }else if (input.matches("^[A-Fa-f0-9]+$")){
                    try{
                        if(client.sendData(Bytes.fromHexString(input))){
                            byte[] data =client.readData(1024, 1000);
                            if(Bytes.isNullOrEmpty(data)){
                                PrintfUtil.e("SerialPortTool",devName+":"+"接收数据失败");
                                openDev(false);
                            }else{
                                openDev(false);
                            }
                        }else{
                            PrintfUtil.e("SerialPortTool",devName+":"+"发送数据失败");
                            openDev(false);
                        }
                    }catch (Exception e){
                        PrintfUtil.e("SerialPortTool",e.getMessage());
                        openDev(false);
                    }
                }else {
                    client.close();
                    openDev(false);
                }
            }
            scan.close();
        } else {
            PrintfUtil.e("SerialPortTool","打开串口失败!!!");
            mainMenu();
        }
    }

}
