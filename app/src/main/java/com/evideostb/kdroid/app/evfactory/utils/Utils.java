package com.evideostb.kdroid.app.evfactory.utils;

import android.content.Context;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class Utils {

    public static String getMAC(Context context){
        String mac = "";
        try {
            InetAddress ip = InetAddress.getByName(getIp(context));
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                buffer.append(String.format("%02x", b[i]));
                if (i< b.length-1){
                    buffer.append(':');
                }
            }
            mac = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    public static String getIp(Context context){
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        //过滤ipv6
                        if (inetAddress.getHostAddress().contains("::")) {
                            continue;
                        }
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        return ip;
    }
}
