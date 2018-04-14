package com.zk.client;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 监听节点内容变化
 */
public class GetDataDemo {

    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-client";
        ZkClient client = new ZkClient("10.211.55.5", 2181);
        client.createEphemeral(path, "123");

        client.subscribeDataChanges(path, new IZkDataListener(){ //节点内容变化监听事件
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
        client.writeData(path, "456"); //节点变化触发事件
        System.out.println(client.readData(path).toString());
        Thread.sleep(1000);

        client.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
