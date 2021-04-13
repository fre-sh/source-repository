package com.fresh.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author guowenyu
 * @since 2021/4/2
 */
public class ZkCuratorDemo {

    public static final String CONNECT_STRING = "192.168.13.131:2181";
    public static final String POLICY_NODE = "/ddm";

    private CuratorFramework client;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    
    @Before
    public void init() {
        client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        System.out.println(client.getState());
        client.getConnectionStateListenable().addListener((client, newState) -> {
            if (ConnectionState.CONNECTED == newState) {
                System.out.println("connected");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
            System.out.println("get data...");
            byte[] bytes = client.getData().forPath(POLICY_NODE);
            System.out.println("initial value:" + new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试监听节点数据变化，支持节点和子节点 创建、修改、删除（节点删除后重建依然可以监听到）...
     * @throws Exception
     */
    @Test
    public void testTreeCache() throws Exception {
        TreeCache treeCache1 = new TreeCache(client, POLICY_NODE);
        treeCache1.start();
        treeCache1.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
                    // 节点被删除时，childData为删除前的数据
                }
                System.out.println("event = " + event);
                byte[] data = childData.getData();
                String value = new String(data);
                System.out.println("data value = " + value);
                System.out.println("---------------------------------------");
            }
        }, executor);
    }

    /**
     * 测试监听节点数据变化，支持节点创建、修改、删除（节点删除后重建依然可以监听到）
     * @throws Exception
     */
    @Test
    public void testNodeCache() throws Exception {
        NodeCache nodeCache = new NodeCache(client, "/ddm/a", false);
        nodeCache.getListenable().addListener(() -> {
            // 节点被删除时，nodeCache.getCurrentData() = null
            System.out.println("路径为：" + nodeCache.getCurrentData().getPath());
            System.out.println("数据为：" + new String(nodeCache.getCurrentData().getData()));
            System.out.println("状态为：" + nodeCache.getCurrentData().getStat());
            System.out.println("---------------------------------------");
        }, executor);
        System.out.println("nodeCache start...");
        nodeCache.start(true);
    }


    /**
     * 测试监听子节点变化
     */
    @Test
    public void testPathChildrenCache() throws Exception {
        //4 建立一个PathChildrenCache缓存,第三个参数为是否接受节点数据内容 如果为false则不接受
        PathChildrenCache cache = new PathChildrenCache(client, POLICY_NODE, true, false, executor);
        //5 在初始化的时候就进行缓存监听
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            /**
             * <B>方法名称：</B>监听子节点变更<BR>
             * <B>概要说明：</B>新建、修改、删除<BR>
             * @see PathChildrenCacheListener#childEvent(CuratorFramework, PathChildrenCacheEvent)
             */
            @Override
            public void childEvent(CuratorFramework cf, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED :" + event.getData().getPath());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED :" + event.getData().getPath());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED :" + event.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @After
    public void waitForEnd() throws InterruptedException {
        Thread.sleep(Integer.MAX_VALUE);
    }
}
