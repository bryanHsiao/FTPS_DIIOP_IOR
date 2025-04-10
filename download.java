package com.cti.common.ftps;

public class download {

	public static void main(String[] args) {
		if (args.length < 9) {
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

			String downloadpath = null;
			if (args.length >= 6)
				downloadpath = args[5];
			String fileName = null;
			if (args.length >= 7)
				fileName = args[6];
			String localPath = null;
			if (args.length >= 8)
				localPath = args[7];
			String localfileName = null;
			if (args.length >= 9)
				localfileName = args[8];
			String info[] = FTPSUtils.getFTPSLoginInfo(notesIP, notesDBpath, key);
			// System.out.println(info[0] + " | " + info[1]);
			if (info[0] != null) {
				if (FTPSUtils.connect(host, port, info[0], info[1])) {
					FTPSUtils.download(downloadpath, fileName, localPath, localfileName);
					FTPSUtils.disconnect();
				}
			}
		}

	}
}
