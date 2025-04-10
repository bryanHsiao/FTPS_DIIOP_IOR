package com.cti.common.ftps;

public class download {

	public static void main(String[] args) {
		if (args.length < 8) {
			System.out.println("FTP args ERROR!!");
		}else {
			String host = null;
			if (args.length >= 1)
				host = args[0];
		    int port = 0;
			if (args.length >= 2)
				port = Integer.parseInt(args[1]);
		    String username = null;
			if (args.length >= 3)
				username = args[2];
		    String password = null;
			if (args.length >= 4)
				password = args[3];
			String downloadpath = null;
			if (args.length >= 5)
				downloadpath = args[4];
			String fileName = null;
			if (args.length >= 6)
				fileName = args[5];
			String localPath = null;
			if (args.length >= 7)
				localPath = args[6];
			String localfileName = null;
			if (args.length >= 8)
				localfileName = args[7];
			if(FTPSUtils.connect(host , port , username , password )) {
				FTPSUtils.download(downloadpath, fileName, localPath, localfileName);
				FTPSUtils.disconnect();
			}
		}

	}
}
