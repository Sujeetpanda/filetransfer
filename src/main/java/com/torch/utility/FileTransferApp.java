package com.torch.utility;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.torch.utility.client.FileTransferClient;

public class FileTransferApp {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext( "FileTransferUtility-Spring.xml" );
		
		FileTransferClient client = (FileTransferClient) applicationContext.getBean("fileTransferClient");
		String retString = null;
		retString = client.uploadFile(args[0],args[1],args[2],args[3],args[4],args[5]);
		System.out.println(retString);
	}

}
