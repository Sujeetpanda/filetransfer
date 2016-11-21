package com.torch.utility.service;

import org.springframework.stereotype.Service;

@Service 
public interface FileTransferService {
	public String uploadFile(String userName, String password, String hostName, String fileName,String remoteDir);
}
