package com.hebabr.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MapToObject {

	/**
	 * 
	 * map转为Object工具类
	 * 
	 * create by jhone create time 2017/06/24 PM7:21:26
	 * 
	 * @param map
	 *            包含信息的map集合
	 * @param className
	 *            要转化的bean名称
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public static Object MapToOject(Map<String, Object> map, String className)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		SimpleDateFormat sdf_two = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
		Class<?> c = Class.forName(className);
		Object obj = c.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();
		String fileType = "";
		String fileName = "";
		String mapVal = "";
		for (Field field : fields) {
			System.out.println("the value of field is:" + field.getType().toString());
			fileType = field.getType().toString();
			if (fileType.contains("boolean")) {
				fileName = field.getName().toString();
				mapVal = (String) map.get(fileName);
				if (mapVal.equals("1") || mapVal == "1")
					map.put(fileName, false);
				else
					map.put(fileName, true);

			} else if (fileType.contains("Date")) {
				fileName = field.getName().toString();
				mapVal = (String) map.get(fileName);
				if (StringUtils.isBlank(mapVal)) {
					map.put(fileName, new Date());
				} else {
					if (mapVal.length() == 10) {
						Date date = sdf_two.parse(mapVal);
						map.put(fileName, date);
					} else {
						Date date = sdf.parse(mapVal);
						map.put(fileName, date);
					}
				}
			} else if (fileType.contains("Byte")) {
				fileName = field.getName().toString();
				mapVal = (String) map.get(fileName);
				Byte b = Byte.valueOf(mapVal);// String转换为byte[]
				map.put(fileName, b);
			} else if (fileType.contains("Boolean")) {
				fileName = field.getName().toString();
				mapVal = (String) map.get(fileName);
				boolean b = false;
				if (mapVal == "" || mapVal == null)
					b = Boolean.parseBoolean(mapVal);
				else if (mapVal.equals("1") || mapVal == "1")
					b = true;
				map.put(fileName, b);

			} else if (fileType.contains("BigDecimal")) {
				fileName = field.getName().toString();
				mapVal = (String) map.get(fileName);
				if (StringUtils.isBlank(mapVal))
					mapVal = "0";
				BigDecimal bVal = new BigDecimal(mapVal);
				map.put(fileName, bVal);
			} else if (fileType.contains("Integer")) {
				fileName = field.getName().toString();
				mapVal = map.get(fileName).toString();
				if (mapVal=="" || mapVal == null)
					mapVal = "0";
				map.put(fileName, Integer.valueOf(mapVal));
			}
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}
		return obj;
	}

	/**
	 * 
	 * 
	 * create by jhone create time 2017/07/11 am3:15:12
	 * 
	 * @param assignees
	 *            形如：[one;two;three,fore;five]
	 * @return
	 */
	public static Map<String, Object> extracted(String[] assignees) {
		Map<String, Object> params = new HashMap<String, Object>();// 保存选择的人员信息
		for (String str : assignees) {
			String[] ss = str.split(";");
			String myStr = "";
			for (int i = 1; i < ss.length; i++) {
				if (i == 1)
					myStr = ss[i];
				else
					myStr = myStr + "," + ss[i];
			}
			params.put(ss[0], myStr);
		}
		return params;
	}

	/**
	 * 
	 * 
	 * create by jhone create time 2017/07/11 am3:15:12
	 * 
	 * @param assignees
	 *            形如：[one;two;three,fore;five]
	 * @return
	 */
	public static Map<String, Object> arrayListExtractedToMap(ArrayList<String> assignees) {
		Map<String, Object> params = new HashMap<String, Object>();// 保存选择的人员信息
		for (String str : assignees) {
			String[] ss = str.split(";");
			String myStr = "";
			for (int i = 1; i < ss.length; i++) {
				if (i == 1)
					myStr = ss[i];
				else
					myStr = myStr + "," + ss[i];
			}
			params.put(ss[0], myStr);
		}
		return params;
	}
}
