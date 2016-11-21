package com.torch.utility.client;

import org.springframework.beans.factory.annotation.Autowired;

import com.torch.utility.delegate.FileTransferDelegate;



public class FileTransferClient {

	@Autowired
	FileTransferDelegate fileTransferDelegate;

	public String uploadFile(String userName, String password, String hostName,String fileName,String remoteDir,String serviceType){
		return fileTransferDelegate.uploadFile(userName, password, hostName, fileName,remoteDir,serviceType);
	}
}
