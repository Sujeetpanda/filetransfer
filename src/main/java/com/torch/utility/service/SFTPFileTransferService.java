package com.torch.utility.service;

import com.jcraft.jsch.ChannelSftp;
import com.torch.utility.common.SFTPUtility;

public class SFTPFileTransferService implements FileTransferService {

	public String uploadFile(String userName, String password, String hostName,String fileName,String remoteDir) {
		SFTPUtility sftpUtility = new SFTPUtility();
		String retMessage = null;
		try {
			sftpUtility.getSession(userName, password, hostName);
			ChannelSftp sftpChannel = sftpUtility.getChannel(sftpUtility.getSession(userName, password, hostName));
			sftpUtility.openConnection(sftpChannel);
			retMessage = sftpUtility.put(sftpChannel,fileName,remoteDir);
		} catch (Exception e) {
			return e.getMessage();			
		}
		return retMessage;
	}
}
