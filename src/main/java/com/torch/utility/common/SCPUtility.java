package com.torch.utility.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.torch.utility.constants.FileTransferConstants;

public class SCPUtility {

	boolean ptimestamp = true;
	FileInputStream fis = null;
	
	public  Session getSession(String userName, String password,
			String hostName) throws Exception {
		//System.out.println("++++++SCP START");
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
	
	public  ChannelExec getChannel(Session session) throws Exception {
		Channel channel = null;
		try {
			channel = session.openChannel(FileTransferConstants.EXEC);
		} catch (JSchException e) {
			e.printStackTrace();
			throw new Exception(FileTransferConstants.FAILED_TO_CREATE_CHANNEL);

		}

		ChannelExec execChannel = (ChannelExec) channel;
		
		return execChannel;
	}
	
	public  void openConnection(ChannelExec channel) throws Exception {
		try {
			channel.connect();
		} catch (JSchException e) {
			e.printStackTrace();
			throw new Exception(FileTransferConstants.UNABLE_TO_OPEN_CHANNEL);
		}
	}
	
	public  String put(Session session,ChannelExec execChannel,String fileName, String remoteDir) {
		try {
				OutputStream out = execChannel.getOutputStream();
				InputStream in = execChannel.getInputStream();
				
				String command = getCommand(fileName,remoteDir);
				
				execChannel.setCommand(command);
				
				if (checkAck(in) != 0) {
					System.exit(0);
				}
			File f = new File(fileName);
			
			if (ptimestamp) {
				command = "T " + (f.lastModified() / 1000) + " 0";
				// The access time should be sent here,
				// but it is not accessible with JavaAPI ;-<
				command += (" " + (f.lastModified() / 1000) + " 0\n");
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					System.exit(0);
				}
			}
			
			// send "C0644 filesize filename", where filename should not include
			// '/'
			long filesize = f.length();
			command = "C0644 " + filesize + " ";
			if (fileName.lastIndexOf('/') > 0) {
				command += fileName.substring(fileName.lastIndexOf('/') + 1);
			} else {
				command += fileName;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
			
			
			// send a content of lfile
			fis = new FileInputStream(fileName);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
			out.close();

			execChannel.disconnect();
			session.disconnect();
			//System.out.println("++++++SCP END");
			System.exit(0);
			
			
		} catch (Exception e) {
			return FileTransferConstants.FILE_UPLOAD_FAILED;
		}

		return FileTransferConstants.FILE_UPLOAD_SUCCESS;
	}
	
	private String getCommand(String fileName,String remoteDir){
		String fullPath = remoteDir + "/" + fileName;
		String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + fullPath;
		return command;
	}
	
	private int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
}
