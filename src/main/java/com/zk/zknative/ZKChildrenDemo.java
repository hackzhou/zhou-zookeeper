package com.zk.zknative;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 继承Watcher接口用来时间监听
 */
public class ZKChildrenDemo implements Watcher {
	private static final CountDownLatch cdl = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	private static Stat stat = new Stat();

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		zk = new ZooKeeper("10.211.55.5:2181", 5000, new ZKChildrenDemo());
		cdl.await();

		zk.create("/zk-test", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		String zkData = new String(zk.getData("/zk-test", true, stat));
		System.out.println(zkData + ", " + stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
		zk.setData("/zk-test", "321".getBytes(), -1);

		zk.create("/zk-test/c1", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

		List<String> list = zk.getChildren("/zk-test", true);
		for (String str : list)
			System.out.println(str);

		zk.create("/zk-test/c2", "789".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		String path3 = zk.create("/zk-test/c3", "789".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("打印节点路径： " + path3);

		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()){
			if (EventType.None == event.getType() && null == event.getPath()) {
				System.out.println("ZK已连接");
				cdl.countDown();
			} else if (event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("节点： " + zk.getChildren(event.getPath(), true));
				} catch (Exception e) {
				}
			}
		}
	}
}
