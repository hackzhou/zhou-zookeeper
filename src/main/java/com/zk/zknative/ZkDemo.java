package com.zk.zknative;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 原生ZK客户端使用
 */
public class ZkDemo implements Watcher {
    private static final CountDownLatch cdl = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zk = new ZooKeeper("10.211.55.5:2181", 5000, new ZkDemo());
        System.out.println(zk.getState());

        try {
            cdl.await();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("监听节点事件： " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            cdl.countDown();
        }
    }
}
