package com.hebabr.base.util;

import java.text.NumberFormat;

public class StringUtils {

    /**
     * 根据输入的字符数字，获得对应的百分比
     * 
     * @param currentNum
     *            当前数
     * @param total
     *            总数
     * @param saveDigit
     *            要保留的小数位数
     * @return
     */
    public static String getPercent(String currentNum, String total, int saveDigit) {
        double y_double = Double.valueOf(total);
        double tempresult = 0.00;
        if (y_double != 0) 
            tempresult = Double.valueOf(currentNum) / y_double;
        NumberFormat nf = NumberFormat.getPercentInstance(); //
        nf.setMinimumFractionDigits(saveDigit); // 保留到小数点后几位
        return nf.format(tempresult);
    }
    
    public static String decodeUnicode(String theString) {          	   
        char aChar;      
        int len = theString.length();             
        StringBuffer outBuffer = new StringBuffer(len);            
        for (int x = 0; x < len;) {            
         aChar = theString.charAt(x++);         
         if (aChar == '\\') {            
          aChar = theString.charAt(x++);            
          if (aChar == 'u') {           
           // Read the xxxx          
           int value = 0;           
           for (int i = 0; i < 4; i++) {           
            aChar = theString.charAt(x++);            
            switch (aChar) {             
            case '0':             
            case '1':            
            case '2':             
            case '3':            
           case '4':           
            case '5':             
             case '6':      
              case '7':      
              case '8':      
              case '9':      
               value = (value << 4) + aChar - '0';      
               break;      
              case 'a':      
              case 'b':      
              case 'c':      
              case 'd':      
              case 'e':      
              case 'f':      
               value = (value << 4) + 10 + aChar - 'a';      
              break;      
              case 'A':      
              case 'B':      
              case 'C':      
              case 'D':      
              case 'E':      
              case 'F':      
               value = (value << 4) + 10 + aChar - 'A';      
               break;      
              default:      
               throw new IllegalArgumentException(      
                 "Malformed   \\uxxxx   encoding.");      
              }             
            }      
             outBuffer.append((char) value);      
            } else {      
             if (aChar == 't')      
              aChar = '\t';      
             else if (aChar == 'r')      
              aChar = '\r';             
             else if (aChar == 'n')             
              aChar = '\n';             
             else if (aChar == 'f')            
              aChar = '\f';            
             outBuffer.append(aChar);           
            }           
           } else           
           outBuffer.append(aChar);           
          }            
          return outBuffer.toString();           
         }
    
    private static int compareTwoStrs(String str, String target){
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++)
        {                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++)
        {                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++)
        {                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++)
            {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2)
                {
                    temp = 0;
                } else
                {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    public static int min(int one, int two, int three){
        return (one = one < two ? one : two) < three ? one : three;
    }
    
    /**
     * 匹配原字符串和目标字符串的相似度，1为100%，0为0%
     * @param str 原字符串
     * @param target  目标字符串
     * @return
     */

    public static float getStringSimilarityRatio(String str, String target){
        return 1 - (float) compareTwoStrs(str, target) / Math.max(str.length(), target.length());
    }
    
    public static void main(String[] args){
//    	String aa = "{'state': '\u914d\u7f6e\u6587\u4ef6\u521d\u59cb\u5316\u5931\u8d25'}";
//    	System.out.println(decodeUnicode(aa));
    	String str = "关于天山项目什么什么的检查";
        String target = "关于燕山大学项目什么什么的检查";
        System.out.println("相似度为=" + getStringSimilarityRatio(str, target));
    }

}
