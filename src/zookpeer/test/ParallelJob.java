package zookpeer.test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

/** 
 * �������� 
 *  
 * @author shencl 
 */  
public class ParallelJob implements Runnable {  
  
    private final String name;  
  
    private final InterProcessLock lock;  
  
    // ���ȴ�ʱ��  
    private final int wait_time = 5;  
  
    ParallelJob(String name, InterProcessLock lock) {  
        this.name = name;  
        this.lock = lock;  
    }  
  
    @Override  
    public void run() {  
        try {  
            doWork();  
        } catch (Exception e) {  
            // ingore;  
        }  
    }  
  
    public void doWork() throws Exception {  
        try {  
            if (!lock.acquire(wait_time, TimeUnit.SECONDS)) {  
                System.err.println(name + "�ȴ�" + wait_time + "�룬��δ�ܻ�ȡ��lock,׼��������");  
            }  
            // ģ��jobִ��ʱ��0-4000����  
            int exeTime = new Random().nextInt(4000);  
            System.out.println(name + "��ʼִ��,Ԥ��ִ��ʱ��= " + exeTime + "����----------");  
            Thread.sleep(exeTime);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            lock.release();  
        }  
    }  
}  