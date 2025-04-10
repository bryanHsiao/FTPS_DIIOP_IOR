package com.cti.common.ftps;

public class upload {

	public static void main(String[] args) {
		if (args.length < 7) {
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
			String filepath = null;
			if (args.length >= 5)
				filepath = args[4];
			String uploadpath = null;
			if (args.length >= 6)
				uploadpath = args[5];
			String filename = null;
			if (args.length >= 7)
				filename = args[6];
			if(FTPSUtils.connect(host , port , username , password )) {
				FTPSUtils.upload(filepath, uploadpath, filename);
				FTPSUtils.disconnect();
			}
		}

	}

}
