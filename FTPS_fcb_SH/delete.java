package com.cti.common.ftps;

public class delete {

	public static void main(String[] args) {
		if (args.length < 5) {
			System.out.println("FTP args ERROR!! need 5");
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
			String FilePath = null;
			if (args.length >= 5)
				FilePath = args[4];

			if(FTPSUtils.connect(host , port , username , password )) {
				FTPSUtils.delete(FilePath);
				FTPSUtils.disconnect();
			}
		}
	}

}
