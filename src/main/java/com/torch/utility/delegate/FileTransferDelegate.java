package com.torch.utility.delegate;

import org.springframework.beans.factory.annotation.Autowired;

import com.torch.utility.lookup.FileTransferLookup;
import com.torch.utility.service.FileTransferService;




public class FileTransferDelegate {
	@Autowired
	FileTransferLookup fileTransferLookup;
	
	public String uploadFile(String userName, String password, String hostName, String fileName,String remoteDir,String serviceType){
		FileTransferService fileTransferService = fileTransferLookup.getFileTransferService(serviceType);
		return fileTransferService.uploadFile(userName, password, hostName, fileName,remoteDir);
	}

}
