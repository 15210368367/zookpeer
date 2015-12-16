package zookpeer.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class CrudExampleMain2 {

	public static void main(String[] args) throws Exception {
		String path = "/test_path";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
		// ���� �����namespace����Ϊһ������Ľڵ���ʹ��ʱ�Զ�����
		client.start();

		// // ����һ���ڵ�
		// client.create().forPath("/head", new byte[0]);
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

//		CuratorListener listener = new CuratorListener() {
//			@Override
//			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
//				System.out.println("node is changed");
//			}
//		};
//		client.getCuratorListenable().addListener(listener);
		// ע��۲��ߣ����ڵ�䶯ʱ����
		client.getChildren().usingWatcher(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				//event.get
				System.out.println("node is changed");
			}
		}).forPath(path);
		System.in.read();
		// ����ʹ��
		// client.close();
	}
}
