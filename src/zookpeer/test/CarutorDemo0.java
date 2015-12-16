package zookpeer.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CarutorDemo0 {

    public static void main(String[] args) throws Exception {
    	String path = "/test_path";
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("localhost:2181")
            .sessionTimeoutMs(5000)
            .connectionTimeoutMs(3000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
        
        Object state=client.checkExists().forPath(path);
        if(state==null){
        	 client.create()
             .creatingParentsIfNeeded()
             .forPath(path, "hello".getBytes());
        }
       
        
        /**
         * ��ע���������ʱ���������˲��������¼�����ʱ���߼����̳߳ش���
         */
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        /**
         * �������ݽڵ�ı仯���
         */
		final NodeCache nodeCache = new NodeCache(client,path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(
            new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    System.out.println("Node data is changed, new data: " + 
                        new String(nodeCache.getCurrentData().getData()));
                }
            }, 
            pool
        );
       // nodeCache.close();
        /**
         * �����ӽڵ�ı仯���
         */
        final PathChildrenCache childrenCache = new PathChildrenCache(client,path, true);
        childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
            new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                        throws Exception {
                        switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("CHILD_ADDED: " + event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            System.out.println("CHILD_REMOVED: " + event.getData().getPath());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("CHILD_UPDATED: " + event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                }
            },
            pool
        );
        
        client.setData().forPath(path, "world".getBytes());
        
        Thread.sleep(10 * 1000);
        System.in.read();
       // pool.shutdown();
       // client.close();
    }
}