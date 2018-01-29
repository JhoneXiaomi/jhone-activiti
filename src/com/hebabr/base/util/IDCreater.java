package com.hebabr.base.util;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IDCreater {
	private static AtomicInteger uniqueId = new 

	AtomicInteger(0);
	private static  Lock LOCK = new ReentrantLock();
	private static int lastTime = (int) 

	System.currentTimeMillis();
	private static int time;

	public static Long createLongID(){
		Random r = new Random();
		return Math.abs(r.nextLong());
	}
	/**
	 * 功能描述：返回int型唯一ID，为自增整数与当前应用服

务时间整型数字之和
	 * 
	 * @author hh
	 */
	@SuppressWarnings("static-access")
	public static synchronized int createIntID() {
		LOCK.lock();
		try{
			boolean done = false;
			while (!done){
				int now = (int) System.currentTimeMillis();
				if (now == lastTime) {
					try {
						Thread.currentThread().sleep(1);
					}catch (java.lang.InterruptedException e){
						e.printStackTrace();
					}
					continue;
				}else {
					lastTime = now;
					done = true;
				}
			}
			time = lastTime;
		}finally {

		}
		LOCK.unlock();
		int s = Integer.valueOf(time+uniqueId.incrementAndGet()) ;
		return s;

	}
	/**
	 * 功能描述：返回32位的唯一ID
	 * 
	 * @author zhangyy
	 */
	public static synchronized String creatID() {

		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		s = s.substring(0, 8) + s.substring(9, 13) + 

		s.substring(14, 18)
		+ s.substring(19, 23) + 

		s.substring(24, 36);

		return s;

	}
}
