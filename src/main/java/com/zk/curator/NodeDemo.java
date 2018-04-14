package com.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 新增/查询/删除节点
 */
public class NodeDemo {

    public static void main(String[] args) throws Exception {
        String path = "/zk-client/test1";
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("10.211.55.5:2181")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, "111".getBytes());

        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println("当前节点版本： " + stat.getVersion());
        System.out.println("更新节点版本： "
                + client.setData().withVersion(stat.getVersion()).forPath(path, "222".getBytes()).getVersion());

        client.getData().storingStatIn(stat).forPath(path);
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        //client.delete().deletingChildrenIfNeeded().forPath("/zk-client"); //递归删除
    }
}
