package zookpeer.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

/** 
 * leaderѡ�� 
 *  
 * @author shencl 
 */  
public class LeaderSelectorMain {  
  
    public static void main(String[] args) {  
  
        List<CuratorFramework> clients = new ArrayList<CuratorFramework>();  
        List<LeaderSelectorClient> examples =new ArrayList<LeaderSelectorClient>();
        try {  
            for (int i = 0; i < 10; i++) {  
                CuratorFramework client = CuratorFrameworkFactory
				.builder()
				.connectString("localhost:2181")
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
				.build();  
                LeaderSelectorClient example = new LeaderSelectorClient(client, "Client #" + i);  
                clients.add(client);  
                examples.add(example);  
  
                client.start();  
                example.start();  
            }  
  
            System.out.println("----------�ȹ۲�һ��ѡ�ٵĽ��-----------");  
            Thread.sleep(10000);  
  
            System.out.println("----------�ر�ǰ5���ͻ��ˣ��ٹ۲�ѡ�ٵĽ��-----------");  
            for (int i = 0; i < 5; i++) {  
                clients.get(i).close();  
            }  
  
            // �����и�С���ɣ���main����һֱ��������̨���룬�첽�Ĵ���Ϳ���һֱ��ִ�С���ͬ��while(ture)���ǣ����س���esc���˳�  
            new BufferedReader(new InputStreamReader(System.in)).readLine();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            for (LeaderSelectorClient exampleClient : examples) {  
                CloseableUtils.closeQuietly(exampleClient);  
            }  
            for (CuratorFramework client : clients) {  
                CloseableUtils.closeQuietly(client);  
            }  
        }  
    }  
}  