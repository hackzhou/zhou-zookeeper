package com.zk.client;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 监听节点内容变化
 */
public class GetDataDemo {

    public static void main(String[] args) throws InterruptedException {
        ZkClient client = new ZkClient("10.211.55.5", 2181);
        String path = "/zk-client";
        client.createEphemeral(path, "123");
        System.out.println(client.readData(path).toString());

        client.subscribeDataChanges(path, new IZkDataListener(){

            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath + " changed: " + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + " deleted");
            }
        });

        System.out.println(client.readData(path).toString());
        client.writeData(path, "456");
        Thread.sleep(1000);
        client.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
