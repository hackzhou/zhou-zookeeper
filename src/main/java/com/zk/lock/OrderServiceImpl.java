package com.zk.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁案例
 */
public class OrderServiceImpl implements Runnable {
	private static OrderCodeGenerator ong = new OrderCodeGenerator();

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	private static final int NUM = 100; // 同时并发的线程数

	private static CountDownLatch cdl = new CountDownLatch(NUM); // 按照线程数初始化倒计数器,倒计数器

	private Lock lock = new ImproveLock();


	public void createOrder() { // 创建订单接口
		String orderCode = null;
		lock.lock();
		try {
			orderCode = ong.getOrderCode(); // 获取订单编号
			System.out.println("insert into DB使用id：=======================>" + orderCode);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}

		// ……业务代码，此处省略100行代码

		logger.info("insert into DB使用id：=======================>" + orderCode);
	}

	@Override
	public void run() {
		try {
			cdl.await(); // 等待其他线程初始化
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createOrder(); // 创建订单
	}

	public static void main(String[] args) {
		for (int i = 1; i <= NUM; i++) {
			new Thread(new OrderServiceImpl()).start(); // 按照线程数迭代实例化线程
			cdl.countDown(); // 创建一个线程，倒计数器减1
		}
	}
}
