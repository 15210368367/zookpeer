package zookpeer.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class CrudExampleMain0 {

	public static void main(String[] args) throws Exception {
		String path = "/test_path";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
		// ���� �����namespace����Ϊһ������Ľڵ���ʹ��ʱ�Զ�����
		client.start();
		//
		// if(client.checkExists().forPath(path).){
		//
		// }
		int count=0;
		while(true){
			Object state = client.checkExists().forPath(path);
			if (state == null) {
				client.create().creatingParentsIfNeeded().forPath(path, "hello".getBytes());
			}
			// // ����һ���ڵ�
			//client.create().forPath(path, "haha0".getBytes());

			client.setData().inBackground().forPath(path, ("haha1"+count).getBytes());
			count++;
			Thread.sleep(1000);

		}

				//
		// // �첽��ɾ��һ���ڵ�
		// client.delete().inBackground().forPath("/head");
		//
		// // ����һ����ʱ�ڵ�
		// client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child",
		// new byte[0]);
		//
		// // ȡ����
		// client.getData().watched().inBackground().forPath("/test");
		//
		// // ���·���Ƿ����
		// client.checkExists().forPath(path);
		//
		// // �첽ɾ��
		// client.delete().inBackground().forPath("/head");

		// ע��۲��ߣ����ڵ�䶯ʱ����
//		client.getData().usingWatcher(new Watcher() {
//			@Override
//			public void process(WatchedEvent event) {
//				System.out.println("node is changed");
//			}
//		}).inBackground().forPath(path);

		//System.in.read();
		// ����ʹ��
		// client.close();
	}
}
