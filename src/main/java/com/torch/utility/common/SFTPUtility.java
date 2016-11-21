package com.torch.utility.common;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.torch.utility.constants.FileTransferConstants;

@Component
public class SFTPUtility {

	public  Session getSession(String userName, String password,
			String hostName) throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(userName, hostName,
					FileTransferConstants.PPORT);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();

		} catch (JSchException e) {
			throw new Exception(FileTransferConstants.INVALID_DETAILS);
		}
		return session;
	}

	public  ChannelSftp getChannel(Session session) throws Exception {
		Channel channel = null;
		try {
			channel = session.openChannel(FileTransferConstants.SFTP);
		} catch (JSchException e) {
			e.printStackTrace();
			throw new Exception(FileTransferConstants.FAILED_TO_CREATE_CHANNEL);

		}

		ChannelSftp sftpChannel = (ChannelSftp) channel;
		
		return sftpChannel;
	}

	public  void openConnection(ChannelSftp channel) throws Exception {
		try {
			channel.connect();
		} catch (JSchException e) {
			e.printStackTrace();
			throw new Exception(FileTransferConstants.UNABLE_TO_OPEN_CHANNEL);
		}
	}

	public  String put(ChannelSftp sftpChannel,String fileName, String remoteDir) {
		try {
			File f = new File(fileName);
			sftpChannel.put(new FileInputStream(f), remoteDir + f.getName());
		} catch (Exception e) {
			return FileTransferConstants.FILE_UPLOAD_FAILED;
		}

		return FileTransferConstants.FILE_UPLOAD_SUCCESS;
	}
}
