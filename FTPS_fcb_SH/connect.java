package com.cti.common.ftps;

public class connect {

	public static void main(String[] args) {
		
		if (args.length < 4) {
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

			if(FTPSUtils.connect(host , port , username , password)) {
				FTPSUtils.disconnect();
			}
		}
	}
}
