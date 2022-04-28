package com.biapp.util;

import aura.data.Bytes;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author Yun
 */
public class PrintfUtil {

    private static boolean showCompileInfo = false;

    public static void setShowCompileInfo(boolean showCompileInfo) {
        PrintfUtil.showCompileInfo = showCompileInfo;
    }

    /**
     * 打印
     *
     * @param tag
     * @param hex
     */
    public static void hex(String tag, String hex) {
        int max = 16;
        int round = hex.length() / max;
        // 小于16
        if (round == 0) {
            debug(tag, hex);
        } else {
            for (int i = 0; i < round; i++) {
                debug(tag,
                        "(" + String.format("%04d", i + 1) + ")"
                                + FormatUtil.addAppend(' ', 32,
                                        Bytes.toHexString(hex.substring(i * max, (i + 1) * max).getBytes()))
                                + " |/*" + hex.substring(i * max, (i + 1) * max) + "*/|");
            }
            if (hex.length() % max != 0) {
                debug(tag,
                        "(" + String.format("%04d", round + 1) + ")"
                                + FormatUtil.addAppend(' ', 32,
                                        Bytes.toHexString(hex.substring(round * max).getBytes()))
                                + " |/*" + hex.substring(round * max) + "*/|");
            }
        }
    }

    /**
     * 打印(超过2K)
     *
     * @param tag
     * @param log
     */
    public static void d(String tag, String log) {
        int max = 2048;
        int round = log.length() / max;
        // 小于1024
        if (round == 0) {
            debug(tag, log);
        } else {
            for (int i = 0; i < round; i++) {
                debug(tag, "(" + (i + 1) + ")" + log.substring(i * max, (i + 1) * max));
            }
            if (log.length() % max != 0) {
                debug(tag, "(" + (round + 1) + ")" + log.substring(round * max));
            }
        }
    }

    /**
     * 打印(超过2K)
     *
     * @param tag
     * @param err
     */
    public static void e(String tag, String err) {
        int max = 2048;
        int round = err.length() / max;
        // 小于1024
        if (round == 0) {
            err(tag, err);
        } else {
            for (int i = 0; i < round; i++) {
                err(tag + "(" + (round + 1) + ")", err.substring(i * max, (i + 1) * max));
            }
            if (err.length() % max != 0) {
                err(tag + "(" + (round + 1) + ")", err.substring(round * max));
            }
        }
    }

    private static void debug(String tag, String log) {
        debug(tag, log, getCaller()[2]);
    }

    private static void err(String tag, String log) {
        err(tag, log, getCaller()[2]);
    }

    private static void debug(String tag, String log, StackTraceElement caller) {
        String printString = "";
        if (showCompileInfo) {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String processName = runtime.getName();
            processName = processName.substring(0, processName.indexOf("@"));
            printString = "D" + " " + DateUtil.getDateString("YYYY-MM-dd HH:mm:ss.SSS", System.currentTimeMillis())
                    + " P:" + processName + " T:" + Thread.currentThread().getId() + "("
                    + Thread.currentThread().getName() + ")" + caller.getClassName() + "." + caller.getMethodName()
                    + "(" + caller.getFileName() + ":" + caller.getLineNumber() + ")" + ":";
        }
        printString += " [" + tag + "]" + log;
        System.out.println(printString);
    }

    private static void err(String tag, String log, StackTraceElement caller) {
        String printString = "";
        if (showCompileInfo) {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String processName = runtime.getName();
            processName = processName.substring(0, processName.indexOf("@"));
            printString = "E" + " " + DateUtil.getDateString("YYYY-MM-dd HH:mm:ss.SSS", System.currentTimeMillis())
                    + " P:" + processName + " T:" + Thread.currentThread().getId() + "("
                    + Thread.currentThread().getName() + ")" + caller.getClassName() + "." + caller.getMethodName()
                    + "(" + caller.getFileName() + ":" + caller.getLineNumber() + ")" + ":";
        }
        printString += " [" + tag + "]" + log;
        System.err.println(printString);
    }

    private static StackTraceElement[] getCaller() {
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        return stack;
    }
}
