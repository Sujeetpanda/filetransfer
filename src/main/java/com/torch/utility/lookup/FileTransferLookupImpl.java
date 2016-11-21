package com.torch.utility.lookup;

import com.torch.utility.constants.FileTransferConstants;
import com.torch.utility.service.FileTransferService;
import com.torch.utility.service.SCPFileTransferService;
import com.torch.utility.service.SFTPFileTransferService;


public class FileTransferLookupImpl implements FileTransferLookup{
	public FileTransferService getFileTransferService( String serviceType ){
		if(serviceType.equalsIgnoreCase(FileTransferConstants.SFTP)){
			return new SFTPFileTransferService();
		}
		if(serviceType.equalsIgnoreCase(FileTransferConstants.SCP)){
			return new SCPFileTransferService();
		}
		return null;
	}
	
}
