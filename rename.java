package com.cti.common.ftps;

public class rename {
	public static void main(String[] args) {
		if (args.length < 7) {
			System.out.println("FTP args ERROR!! need 6");
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

			String OriginalFilePath = null;
			if (args.length >= 6)
				OriginalFilePath = args[5];
			String ChangeFilePath = null;
			if (args.length >= 7)
				ChangeFilePath = args[6];

			String info[] = FTPSUtils.getFTPSLoginInfo(notesIP, notesDBpath, key);
			// System.out.println(info[0] + " | " + info[1]);
			if (info[0] != null) {
				if (FTPSUtils.connect(host, port, info[0], info[1])) {
					FTPSUtils.rename(OriginalFilePath, ChangeFilePath);
					FTPSUtils.disconnect();
				}
			}

		}
	}
}
