package com.zk.client;

import org.I0Itec.zkclient.ZkClient;

/**
 * 创建持久/临时节点
 */
public class CreateNodeDemo {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("10.211.55.5", 2181);
        String path1 = "/test1";
        zkClient.createEphemeral(path1, "test");
        String path2 = "/zk-client/test1";
        zkClient.createPersistent(path2, true); //递归创建
    }

}
