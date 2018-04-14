package com.zk.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简单地分布式自增序列
 */
public class OrderCodeGenerator {
	private static int i = 0; // 自增长序列

	public String getOrderCode() { // 按照“年-月-日-小时-分钟-秒-自增长序列”的规则生成订单编号
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(now) + ++i;
	}

	public static void main(String[] args) {
		OrderCodeGenerator ong = new OrderCodeGenerator();
		for (int i = 0; i < 10; i++) {
			System.out.println(ong.getOrderCode());
		}
	}
}
