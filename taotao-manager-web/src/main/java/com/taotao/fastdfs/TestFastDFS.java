package com.taotao.fastdfs;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.taotao.utils.FastDFSClient;
public class TestFastDFS {
	public static void main(String[] args) {
		TestFastDFS t =new TestFastDFS();
		try {
			System.out.println(".....");
			t.test3();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test3()
	{
		try {
			FastDFSClient client =new FastDFSClient("D:/itElse/javaWorkspa2/taotao-manager-web/src/main/resources/resource/client.conf");
			String file = client.uploadFile("D://itElse//pictureServer//1.jpg");
			System.out.println(file);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void test2() 
	{	
		System.out.println("进来了....");
		
		//填写配置文件(tracker服务器的地址)
		//加载配置文件(引入jar包,通过引入工程,引入到pom中引入,中央仓库并没有此jar包)
		try {
			ClientGlobal.init("D:/itElse/javaWorkspa2/taotao-manager-web/src/main/resources/resource/client.conf");
			//创建TrackerClient对象
			TrackerClient tc =new TrackerClient();
			//用TrackerClient对象创建trackerserver
			TrackerServer server = tc.getConnection();
			//创建一个StorageServer的引用
			StorageServer ss=null;
			//创建StorageClient对象(参数1:trackerserver, 参数2:StorageServer)
			StorageClient sc =new StorageClient(server,ss);
			//用StorageClient对象上传文件
			String [] strings =sc.upload_file("D://itElse//pictureServer//1.jpg", "jpg", null);
			for (String string : strings) {
				System.out.println(string);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
