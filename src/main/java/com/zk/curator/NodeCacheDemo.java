package com.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 缓存
 */
public class NodeCacheDemo {
	public static void main(String[] args) throws Exception {
		String path = "/zk-client/nodecache";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("10.211.55.5:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "test".getBytes());

		NodeCache nc = new NodeCache(client, path, false);
		nc.start();
		nc.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("更新当前节点内容： " + new String(nc.getCurrentData().getData()));
			}
		});
		
		client.setData().forPath(path, "test123".getBytes());
		Thread.sleep(1000);

		client.delete().deletingChildrenIfNeeded().forPath(path);
		Thread.sleep(5000);
		nc.close();
	}
}
