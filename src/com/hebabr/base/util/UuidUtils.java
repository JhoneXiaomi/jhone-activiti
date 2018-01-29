package com.hebabr.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class UuidUtils {
	
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
        "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
        "W", "X", "Y", "Z" };  

	/**
	 * 
	 * 获取32未得UUId随机数
	 */
	public static String generateLongUuid(){
    	UUID uuid = UUID.randomUUID();
		String str = uuid.toString().replaceAll("-", "");
		return str;
    }
	
	/**
	 * 获取8位的UUID随机数（测试十万级数据未重复）
	 * @return
	 */
	public static String generateShortUuid() {  
	StringBuffer shortBuffer = new StringBuffer();  
	String uuid = UUID.randomUUID().toString().replace("-", "");  
	for (int i = 0; i < 8; i++) {  
	    String str = uuid.substring(i * 4, i * 4 + 4);  
	    int x = Integer.parseInt(str, 16);  
	    shortBuffer.append(chars[x % 0x3E]);  
	}  
		return shortBuffer.toString();  
	
	}
	/**定义的新增随机数的内容*/
	public static String getStochastic(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		int number;
		for (int i = 0; i < length; i++) {
			number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	/**
	 * 获取H开头的派单系统的UUID随机数   例子：H151*111*102*936
	 * @return
	 */
	public static String genUUID(){
		StringBuffer result = new StringBuffer();
		result.append("H");
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMddHHmmss");
		String strTime = dateFmt.format(new Date());
		char[] charTime = strTime.toCharArray();
		for (int i = 0; i < charTime.length; i++) {
			if(i%3==2){
				result.append(charTime[i]);
				if(i+1!=charTime.length){
					result.append(getStochastic(1));
				}
			}else{
				result.append(charTime[i]);
			}
		}
		return result.toString();
	}

	/**
	 * 生成原件的UUID
	 * 
	 * create by zhangzhiwei 2017/03/26 AM9:43:12
	 * @return
	 */
	public static String yGenUUId(){
		StringBuffer result = new StringBuffer();
		result.append("Y");
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyymmss");
		String strTime = dateFmt.format(new Date());
		char[] charTime = strTime.toCharArray();
		for (int i = 0; i < charTime.length; i++) {
			if(i%3==2){
				result.append(charTime[i]);
				if(i+1!=charTime.length){
					result.append(getStochastic(1));
				}
			}else{
				result.append(charTime[i]);
			}
		}
		return result.toString();
	}
	
	/**
	 * 生成复印件的UUID
	 * 
	 * create by zhangzhiwei 2017/03/26 AM9:43:50
	 * @return
	 */
	public static String fGenUUId(){
		StringBuffer result = new StringBuffer();
		result.append("F");
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyymmss");
		String strTime = dateFmt.format(new Date());
		char[] charTime = strTime.toCharArray();
		for (int i = 0; i < charTime.length; i++) {
			if(i%3==2){
				result.append(charTime[i]);
				if(i+1!=charTime.length){
					result.append(getStochastic(1));
				}
			}else{
				result.append(charTime[i]);
			}
		}
		return result.toString();
	}
	
}
