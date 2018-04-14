package com.zk.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁实现
 */
public class DistributeLock implements Lock {
	private static Logger logger = LoggerFactory.getLogger(DistributeLock.class);

	private static final String ZK_IP_PORT = "10.211.55.5:2181";
	private static final String LOCK_NODE = "/lock";

	private ZkClient client = new ZkClient(ZK_IP_PORT);
	private CountDownLatch cdl = null;

	@Override
	public void lock() { // 实现阻塞式的加锁
		if (tryLock()) {
			return;
		}
		waitForLock();
		lock();
	}

	private void waitForLock() { // 阻塞时的实现
		IZkDataListener listener = new IZkDataListener() { // 给节点加监听
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				if (cdl != null) { //监听到删除节点（释放锁）事件
					cdl.countDown();
				}
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
			}
		};

		client.subscribeDataChanges(LOCK_NODE, listener);
		if (client.exists(LOCK_NODE)) {
			try {
				cdl = new CountDownLatch(1);
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		client.unsubscribeDataChanges(LOCK_NODE, listener);
	}


	@Override
	public boolean tryLock() { // 实现非阻塞式的加锁
		try {
			client.createPersistent(LOCK_NODE);
			return true;
		} catch (ZkNodeExistsException e) {
			return false;
		}
	}

	@Override
	public void unlock() { // 释放锁
		client.delete(LOCK_NODE);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public Condition newCondition() {
		return null;
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}
}
