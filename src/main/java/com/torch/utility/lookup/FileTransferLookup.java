package com.torch.utility.lookup;

import com.torch.utility.service.FileTransferService;

public interface FileTransferLookup {
	public FileTransferService getFileTransferService( String serviceType );
}
