package com.cti.common.ftps;

public class rename {
	public static void main(String[] args) {
		if (args.length < 6) {
			System.out.println("FTP args ERROR!! need 6");
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
			String OriginalFilePath = null;
			if (args.length >= 5)
				OriginalFilePath = args[4];
			String ChangeFilePath = null;
			if (args.length >= 6)
				ChangeFilePath = args[5];

			if(FTPSUtils.connect(host , port , username , password )) {
				FTPSUtils.rename(OriginalFilePath, ChangeFilePath);
				FTPSUtils.disconnect();
			}
		}
	}
}
