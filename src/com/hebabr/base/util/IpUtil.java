package com.hebabr.base.util;
  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.LineNumberReader;  
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;  

import javax.servlet.http.HttpServletRequest;  
  
public class IpUtil {   
    /** 
      * 获取客户端IP  
      * @param request 
      * @return 
    */  
    public static String getClientIp(HttpServletRequest request){  
          
          String ip = request.getHeader("X-Forwarded-For");  
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
              ip = request.getHeader("Proxy-Client-IP");  
          }  
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
              ip = request.getHeader("WL-Proxy-Client-IP");  
          }  
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
              ip = request.getHeader("HTTP_CLIENT_IP");  
          }  
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
              ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
          }  
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
              ip = request.getRemoteAddr();  
          }  
          if(ip == null){  
              ip = "192.168.66.146";  
          }  
          return ip;  
    }  
    
    @SuppressWarnings("rawtypes")
	public static String  getServerIp(){
    	String SERVER_IP = "";
        try {  
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();  
            InetAddress ip = null;  
            while (netInterfaces.hasMoreElements()) {  
                NetworkInterface ni = (NetworkInterface) netInterfaces  
                        .nextElement();  
                ip = (InetAddress) ni.getInetAddresses().nextElement();  
                SERVER_IP = ip.getHostAddress();  
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()  
                        && ip.getHostAddress().indexOf(":") == -1) {  
                    SERVER_IP = ip.getHostAddress();  
                    break;  
                } else {  
                    ip = null;  
                }  
            }  
        } catch (SocketException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
         
         return SERVER_IP;  
       }  
 
    
    
    /** 
     * 是否为外网 【 true内false外】
     * @param ipAddress 
     * @return 
     */  
    public static boolean getIpType(String ipAddress){  
        Pattern p = Pattern.compile("^((192.168.)|(10.)|(172.16)|(127.0))+[0-9.]+$");         
        if(p.matcher(ipAddress).matches()) {  
        	return  true;
        }else {  
        	return  false;
        }  
    }  
      
    /** 
     * 获取客户端IP对应网卡的MAC地址 
     * @param ipAddress 
     * @return 
     */  
     public static String getMACAddress(String ipAddress) {   
            String str = "", strMAC = "", macAddress = "";   
            try {   
                Process pp = Runtime.getRuntime().exec("nbtstat -a " + ipAddress);   
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());   
                LineNumberReader input = new LineNumberReader(ir);   
                for (int i = 1; i < 100; i++) {   
                    str = input.readLine();   
                    if (str != null) {   
                        if (str.indexOf("MAC") > 1) {   
                         strMAC = str.substring(str.indexOf("=")+1);  
                            break;   
                        }   
                    }   
                }   
            } catch (IOException ex) {   
                return "Can't Get MAC Address!";   
            }   
            if (strMAC.length() < 17) {   
                return "Error!";   
            }   
              
            macAddress = strMAC.replaceAll("\\s", "");  
            return macAddress;   
        }  
     
     /** 
      * main 
      * @param args 
      */  
     public static void main(String[] args) {  
         /* boolean ty = getIpType("127.0.0.1");  
           // Integer ty = getIpType("119.75.217.56");  
            if(ty){  
                System.out.println("内网");  
            }else{  
                System.out.println("外网");  
            }   
            System.out.println(getMACAddress("127.0.0.1")); */ 
            
    	 Date date = new Date();
    	 String a = DateUtils.getByTime(date);
    	 String s = "10-60-4B-7D-9B-DD:10-61-4B-7D-9B-DD";
    	 System.out.println(s.contains("10-60-4B-7D-9B-DD")+"=========="+a+"==="+a.compareTo("10:50")+"==="+a.compareTo("12:50")+"==="+a.compareTo("11:43"));
        }  
}