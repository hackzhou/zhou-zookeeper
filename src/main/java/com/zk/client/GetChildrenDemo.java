package com.zk.client;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * 监听子节点变化
 */
public class GetChildrenDemo {

    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-client";
        ZkClient client = new ZkClient("10.211.55.5", 2181);
        client.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + "的子节点： " + currentChilds);
            }
        });

        client.createPersistent(path);
        Thread.sleep(1000);
        System.out.println(client.getChildren(path));

        client.createPersistent(path + "/test1");
        Thread.sleep(1000);
        System.out.println(client.getChildren(path));

        client.delete(path + "/test1");
        Thread.sleep(1000);

        client.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}

