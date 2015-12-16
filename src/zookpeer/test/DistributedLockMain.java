package zookpeer.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.utils.CloseableUtils;

/**
 * �ֲ�ʽ��ʵ��
 * 
 * @author shencl
 */
public class DistributedLockMain {
	private static CuratorFramework client = null;// ClientFactory.newClient();
	private static final String PATH = "/locks";

	// �����ڲ��������룩��д��
	private static final InterProcessReadWriteLock lock;
	// ����
	private static final InterProcessLock readLock;
	// д��
	private static final InterProcessLock writeLock;

	static {
		client.start();
		lock = new InterProcessReadWriteLock(client, PATH);
		readLock = lock.readLock();
		writeLock = lock.writeLock();
	}

	public static void main(String[] args) {
		try {
			List<Thread> jobs = new ArrayList<Thread>();
			for (int i = 0; i < 10; i++) {
				Thread t = new Thread(new ParallelJob("Parallel����" + i, readLock));
				jobs.add(t);
			}

			for (int i = 0; i < 10; i++) {
				Thread t = new Thread(new MutexJob("Mutex����" + i, writeLock));
				jobs.add(t);
			}

			for (Thread t : jobs) {
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseableUtils.closeQuietly(client);
		}
	}
}