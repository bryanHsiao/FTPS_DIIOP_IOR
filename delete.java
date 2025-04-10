package com.cti.common.ftps;

public class mdelete {

	public static void main(String[] args) {
		if (args.length < 6) {
			System.out.println("FTP args ERROR!! need 5");
		} else {
			String host = null;
			if (args.length >= 1)
				host = args[0];
			int port = 0;
			if (args.length >= 2)
				port = Integer.parseInt(args[1]);

			String notesIP = null;
			if (args.length >= 3)
				notesIP = args[2];
			String notesDBpath = null;
			if (args.length >= 4)
				notesDBpath = args[3];
			String key = null;
			if (args.length >= 5)
				key = args[4];

			String FilePath = null;
			if (args.length >= 6)
				FilePath = args[5];
			String info[] = FTPSUtils.getFTPSLoginInfo(notesIP, notesDBpath, key);
			// System.out.println(info[0] + " | " + info[1]);
			if (info[0] != null) {
				if (FTPSUtils.connect(host, port, info[0], info[1])) {
					FTPSUtils.delete(FilePath);
					FTPSUtils.disconnect();
				}
			}
		}
	}

}
