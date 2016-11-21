package com.torch.utility.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.torch.utility.common.SCPUtility;

public class SCPFileTransferService implements FileTransferService {

	public String uploadFile(String userName, String password, String hostName,	String fileName, String remoteDir) {
		SCPUtility scpUtility = new SCPUtility();
		String retMessage = null;
		try {
			Session session = scpUtility.getSession(userName, password,hostName);
			ChannelExec sftpChannel = scpUtility.getChannel(session);
			scpUtility.openConnection(sftpChannel);
			retMessage = scpUtility.put(session, sftpChannel, fileName,remoteDir);
		} catch (Exception e) {
			return e.getMessage();
		}
		return retMessage;
	}
}
