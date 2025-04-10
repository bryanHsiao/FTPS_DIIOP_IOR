package com.cti.common.ftps;

public class upload_binary {

	public static void main(String[] args) {
		if (args.length < 8) {
			System.out.println("FTP args ERROR!!");
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
			String filepath = null;
			if (args.length >= 6)
				filepath = args[5];
			String uploadpath = null;
			if (args.length >= 7)
				uploadpath = args[6];
			String filename = null;
			if (args.length >= 8)
				filename = args[7];

			String info[] = FTPSUtils.getFTPSLoginInfo(notesIP, notesDBpath, key);
			// System.out.println(info[0] + " | " + info[1]);
			if (info[0] != null) {
				if (FTPSUtils.connect(host, port, info[0], info[1])) {
					FTPSUtils.upload_binary(filepath, uploadpath, filename);
					FTPSUtils.disconnect();
				}
			}
		}

	}

}
