package com.cti.common.ftps;

import lotus.domino.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.BufferedReader;
import java.io.FileReader;

public class FTPSUtils {
	private static FTPSClient ftps;
	protected int ftpConnectTimeout = 60 * 1000;
	public int ERROR_FTPDOWNLOAD = 5;
	public int ERROR_FTPUPLOAD = 6;
	public int ERROR_FTPDELETE = 7;

	static lotus.domino.Session session = null;
	static Database db = null;

	public static boolean connect(String host, int port, String username, String password) {
		boolean isOK = false;

		String ftpServerName = host;
		int ftpServerPort = port;
		String ftpUserName = username;
		String ftpPassword = password;

		boolean asciiType = true;
		boolean encryptPassword = true;

		try {
			ftps = new FTPSClient("TLSv1.2");
			ftps.setAuthValue("TLS");
			int reply;
			ftps.connect(ftpServerName, ftpServerPort);
			System.out.println("Connected to ftps server");
			System.out.print(ftps.getReplyString());
			reply = ftps.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftps.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			} else {
				// System.out.println("準備登入!!");
			}
			boolean loginSatisfactorio = ftps.login(ftpUserName, ftpPassword);
			if (loginSatisfactorio) {
				System.out.println("Hello '" + ftpUserName + "' login success!!");
				isOK = true;
			} else {
	
				System.err.println("login fail!!");
				//sendAlertMail();
			}
			//ftps.setFileType(FTP.BINARY_FILE_TYPE);
			ftps.execPBSZ(200);
			ftps.execPROT("P");
			/// ... // transfer files
			// ftp.setBufferSize(1000);
			// passive on
			ftps.enterLocalPassiveMode();
			ftps.setControlEncoding("MS950");
		} catch (IOException e) {
			System.err.println("FTPs server err.");
		}
		return isOK;
	}

	/**
	 * @return String[] 回傳2個值，第一個是帳號，第二個是密碼
	 * @param IP     Notes端IP
	 * @param dbPath Notes端DB路徑
	 * @param key    抓取FTP連線資訊鍵值
	 * @throws Exception
	 */
	public static String[] getFTPSLoginInfo(String IP, String dbPath, String key) {
		//lotus.domino.Session session = null;
		//Database db = null;
		View view = null;
		Document doc = null;
		String[] info = new String[2];
		String ior = "";
		try {
			// String dbPath = "FCB/FTPSAccountSetup.nsf";
			try {
				FileReader fr = new FileReader("diiop_ior.txt");
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					ior=ior+br.readLine();
				}
				fr.close();
				//session = NotesFactory.createSession(IP);
				session = NotesFactory.createSessionWithIOR(ior, "", "");
			} catch (NotesException e) {
				// TODO 自動產生的 catch 區塊
				System.out.println("=== Creare session Fail !!===");
			}
			db = session.getDatabase(session.getServerName(), dbPath);
			// System.out.println("getDB.title = " + db.getTitle());
		} catch (Exception e) {
			System.out.println("=== Connected to DB Fail !!===");
		}
		try {
			view = db.getView("vwAccountSet");
			// System.out.println(view.getName());
			doc = view.getDocumentByKey(key, true);

			if (doc != null) {
				info[0] = doc.getItemValueString("Account");
				info[1] = doc.getItemValueString("pwd");
			} else {
				System.out.println("key:" + key + " not find set doc !!");
			}
		} catch (NotesException e) {
			System.out.println("===view by key get document has Fail !!===");
		} finally {
			try {
				if (doc != null)
					doc.recycle();
				if (view != null)
					view.recycle();
				if (db != null)
					db.recycle();
				if (session != null)
					session.recycle();
			} catch (Exception e) {
				System.out.println("=== recycle get Error !! ===");
			}
		}
		return info;
	}

	/*public static void sendAlertMail() throws NotesException {
		if(db!=null) {
			Agent agent = db.getAgent("alertMail");
			agent.run();
		}
	}*/

	/**
	 * @return void
	 * @exception :(Error note)
	 * @param filepath   要上傳的檔案路徑
	 * @param uploadpath 上傳的文件的路徑
	 * @param filename   上傳的檔案名，留空表示不改變
	 * @throws Exception
	 */
	public static void upload(String filepath, String uploadpath, String filename) {
		System.out.println("uploadFile : " + filepath);
		File file = new File(filepath);
		createFolder(uploadpath);
		InputStream input;
		String uploadFileName = "";
		try {
			input = new FileInputStream(file);
			if (filename.equals("")) {
				uploadFileName = file.getName();
			} else {
				uploadFileName = filename;
			}
			ftps.storeFile(uploadFileName, input);
			input.close(); // 關閉輸入流
			System.out.println("uploadFile success!! ,path:" + uploadpath + ", filename:" + uploadFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void upload_binary(String filepath, String uploadpath, String filename) {
		System.out.println("uploadFile : " + filepath);
		File file = new File(filepath);
		createFolder(uploadpath);
		InputStream input;
		String uploadFileName = "";
		try {
			input = new FileInputStream(file);
			if (filename.equals("")) {
				uploadFileName = file.getName();
			} else {
				uploadFileName = filename;
			}
			ftps.setFileType(FTP.BINARY_FILE_TYPE);
			ftps.storeFile(uploadFileName, input);
			input.close(); // 關閉輸入流
			System.out.println("uploadFile success!! ,path:" + uploadpath + ", filename:" + uploadFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return void
	 * @exception :(Error note)
	 * @param file 上傳的文件或文件夾
	 * @param path 上傳的文件的路徑
	 * @throws Exception
	 */
	public static void upload(File file, String path) {
		try {
			System.out.println("file.isDirectory():" + file.isDirectory());
			if (file.isDirectory()) {
				System.out.println("產生資料夾:" + path);
				createFolder(path);
				String[] files = file.list();
				for (int i = 0; i < files.length; i++) {
					File file1 = new File(path + "\\" + file.getPath() + "\\" + files[i]);
					if (file1.isDirectory()) {
						upload(file1, path);
						ftps.changeToParentDirectory();
					} else {
						File file2 = new File(file.getPath() + "\\" + files[i]);
						FileInputStream input = new FileInputStream(file2);
						ftps.storeFile(file2.getName(), input);
						input.close();
					}
				}
			} else {
				File file2 = new File(file.getPath());
				// System.out.println( " file.getPath() : " + file.getPath() + " |
				// file2.getName() : " + file2.getName() );
				InputStream input = new FileInputStream(file2);
				createFolder(path);
				ftps.storeFile(file2.getName(), input);
				input.close(); // 關閉輸入流
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("uploadFile done");
		}
	}

	/**
	 * @param downloadpath  下載的文件的路徑
	 * @param fileName      下載的文件名
	 * @param localPath     下載的文件本地路徑
	 * @param localfileName 下載到本地的檔案名，留空表示不改變
	 */
	static void download(String downloadpath, String fileName, String localPath, String localfileName) {
		try {
			System.out.println("download file to :" + localPath);
			String downloadFileName = "";
			ftps.changeWorkingDirectory(downloadpath);
			// 列出該目錄下所有文件
			FTPFile[] fs = ftps.listFiles();
			// 遍曆所有文件，找到指定的文件
			for (FTPFile ff : fs) {
				//System.out.println("==>" + ff.getName());
				if (ff.getName().equals(fileName)) {
					// 根據絕對路徑初始化文件
					File file = new File(localPath);
					// 如果資料夾不存在則建立
					if (!file.exists() && !file.isDirectory()) {
						System.out.println("Directory create :" + localPath);
						file.mkdir();
					} else {
						//System.out.println("Directory already exists:" + localPath);
					}
					if (localfileName.equals("")) {
						downloadFileName = ff.getName();
					} else {
						downloadFileName = localfileName;
					}
					//System.out.println("downloadFileName:" + downloadFileName);
					File localFile = new File(localPath, downloadFileName);
					// 輸出流
					OutputStream is = new FileOutputStream(localFile);
					// 下載文件
					ftps.retrieveFile(ff.getName(), is);
					System.out.println("download success!! ,path:" + localPath + " ,filename:" + downloadFileName);
					is.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void download_binary(String downloadpath, String fileName, String localPath, String localfileName) {
		try {
			System.out.println("download file to :" + localPath);
			String downloadFileName = "";
			ftps.changeWorkingDirectory(downloadpath);
			// 列出該目錄下所有文件
			FTPFile[] fs = ftps.listFiles();
			// 遍曆所有文件，找到指定的文件
			for (FTPFile ff : fs) {
				//System.out.println("==>" + ff.getName());
				if (ff.getName().equals(fileName)) {
					// 根據絕對路徑初始化文件
					File file = new File(localPath);
					// 如果資料夾不存在則建立
					if (!file.exists() && !file.isDirectory()) {
						System.out.println("Directory create :" + localPath);
						file.mkdir();
					} else {
						//System.out.println("Directory already exists:" + localPath);
					}
					if (localfileName.equals("")) {
						downloadFileName = ff.getName();
					} else {
						downloadFileName = localfileName;
					}
					//System.out.println("downloadFileName:" + downloadFileName);
					File localFile = new File(localPath, downloadFileName);
					// 輸出流
					OutputStream is = new FileOutputStream(localFile);
					// 下載文件
					ftps.setFileType(FTP.BINARY_FILE_TYPE);
					ftps.retrieveFile(ff.getName(), is);
					System.out.println("download success!! ,path:" + localPath + " ,filename:" + downloadFileName);
					is.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void rename(String OriginalFilePath, String ChangeFilePath) {
		System.out.println("rename file ,from:+" + OriginalFilePath + " ,to:" + ChangeFilePath);
		try {
			// File file =new File(OriginalFilePath);
			// if(file.exists()) {
			ftps.rename(OriginalFilePath, ChangeFilePath);
			// System.out.println("rename success!!");
			// }else {
			// System.out.println("files not find:"+OriginalFilePath + " ,rename fail!!");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delete(String FilePath) {
		System.out.println("delete file:" + FilePath);
		try {
			// File file =new File(FilePath);
			// if(file.exists()) {
			ftps.deleteFile(FilePath);
			// System.out.println("delete success!!");
			// }else {
			// System.out.println("files not find:"+FilePath + " ,delete fail!!");
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createFolder(String path) {
		String f = "";
		try {
			for (int ii = 0; ii < path.split("/").length; ii++) {
				f = f + "/" + path.split("/")[ii];
				ftps.makeDirectory(path.split("/")[ii]);
				ftps.changeWorkingDirectory(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void disconnect() {
		if (ftps.isConnected()) {
			try {
				ftps.logout();
				ftps.disconnect();
				// System.out.println("disconnect success");
			} catch (Exception e) {
				System.out.println("disconnect fail : " + e);
			}
		}
	}

	//搜尋功能:下載檔案及刪除FTP檔案使用
	/** 
	     *@param FTPpath 	下載文件的FTP路徑
	     *@param fileName 	搜尋文件名稱的條件
	     *@param ftps		建立FTPClient物件 
	 */
	 

	    public static ArrayList<String> searchFiles(String FTPpath, String fileName, FTPSClient ftps) {

	        ArrayList<String> matchFiles = new ArrayList<String>();

	        try {

	            boolean ftpPath = ftps.changeWorkingDirectory(FTPpath);

	            if (ftpPath == true) {

	                FTPFile[] filesFTP = ftps.listFiles();
	                //for (FTPFile file : filesFTP) {
	                //    System.out.println(file.getName());
	                //}

	                fileName = fileName.replace(".", "\\W");
	                fileName = fileName.replaceAll("\\*", ".*");
	                fileName = fileName.replaceAll("\\?", ".");

	                boolean found = false;

	                for (FTPFile file : filesFTP) {

	                    if (file.getName().matches(fileName)) {
	                        found = true;
	                        matchFiles.add(file.getName());
	                    }
	                }
	                if (!found) {
	                    System.out.println("No file found");
	                }
	            } else {		
	                System.out.println("Path can not be found");
	            }

	            return matchFiles;

	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }

	        return matchFiles;
	    }
	    
	  //搜尋功能:上傳檔案使用
	    /** 	
	        *@param localPath 	 搜尋文件所在的本地路徑
	        *@param fileName 	 搜尋文件名稱的條件
	        *@param localFile	 建立目錄物件 
	    */
	     public static ArrayList<String> searchFiles(String localPath, String fileName, File localFile) {

	           ArrayList<String> matchFiles = new ArrayList<String>();

	           try {
	               //只找出路徑中的文件檔，而沒有資料夾
	               FileFilter filter = new FileFilter() {
	                   @Override
	                   public boolean accept(File dir) {
	                       return dir.isFile();
	                   }
	               };
	               localFile = new File(localPath);
	   			// 列出該目錄下所有文件
	               File[] files = localFile.listFiles(filter);
	   			// 若資料夾存在，執行以下動作
	               if (localFile.exists() && localFile.isDirectory()) {
	   				//列出資料夾中的所有文件
	                   //for (File file : files) {
	                   //    System.out.println(file.getName());
	                   //}
	   				/*萬用字元 V.S. 正則表示式: 
	   				  萬用字元 "?" 等於正則表示式 "."。 
	   				  萬用字元 "*" 等於正則表示式".*"。
	   				  而由於正則表示式"."代表符合除「\r」「\n」之外的任何單個字元，文件中連接附檔名的"."若用在搜尋時，需將之改為符合任何非單詞字元的"/W"。。
	   				*/
	                   fileName = fileName.replace(".", "\\W");
	                   fileName = fileName.replaceAll("\\*", ".*");
	                   fileName = fileName.replaceAll("\\?", ".");
	                   // 將是否找尋到檔案設定為false
	                   boolean found = false;

	                   for (File file : files) {

	                       if (file.getName().matches(fileName)) {
	                           //若有搜尋到檔案
	                           found = true;
	                           matchFiles.add(file.getName());
	                       }
	                   }
	   				//當沒有任何檔案被找到時
	                   if (!found) {
	                       System.out.println("No file found");
	                   }
	               } else {
	                   //若資料夾不存在時
	                   System.out.println("Path can not be found");
	               }

	               return matchFiles;

	           } catch (Exception e) {
	               System.out.println(e.getMessage());
	           }
	           return matchFiles;
	       }

	  // 整批上傳文件功能
	     /**
	      * @param localPath     上傳文件的本地路徑
	      * @param fileName      上傳文件的名稱
	      * @param uploadToPath  上傳文件至FTP的FTP路徑	
	     */
	       public static void mput(String localPath, String fileName, String uploadToPath) {

	     		//建立一個File目錄物件，存取本地路徑文件
	             File file = new File(localPath);
	     		//搜尋本地路徑文件，回傳所有符合搜尋條件的文件名稱
	             ArrayList<String> foundFiles = searchFiles(localPath, fileName, file);
	             System.out.println("");

	             for (int i = 0; i < foundFiles.size(); i++) {
	                 System.out.println("Found file:" + foundFiles.get(i));
	     			//上傳所有符合搜尋條件的文件至指定的FTP路徑
	                 upload_binary(localPath + "\\" + foundFiles.get(i), uploadToPath, "");
	             }
	         }
	     	
	     //整批下載文件功能
	      /**
	        *@param localPath      下載文件的本地路徑
	        *@param fileName       下載文件的名稱
	        *@param downloadToPath 下載文件的FTP路徑	
	      */
	         public static void mget(String localPath, String fileName, String downloadFromPath) {

	     		//搜尋FTP路徑文件，回傳所有符合搜尋條件的文件名稱
	             ArrayList<String> foundFiles = searchFiles(downloadFromPath, fileName, ftps);
	             System.out.println("");

	             for (int i = 0; i < foundFiles.size(); i++) {
	                 System.out.println("Found file:" + foundFiles.get(i));
	     			//下載所有符合搜尋條件的文件至指定的本地路徑
	                 download_binary(downloadFromPath, foundFiles.get(i), localPath, "");
	             }

	         }
	     	
	     //整批刪除文件功能
	      /**  
	        *@param  fileName       刪除文件的名稱
	        *@param  deletePath     刪除文件的FTP路徑	
	      */
	         public static void mdelete(String fileName, String deletePath) {

	     		//搜尋FTP路徑文件，回傳所有符合搜尋條件的文件名稱
	             ArrayList<String> foundFiles = searchFiles(deletePath, fileName, ftps);
	             System.out.println("");

	             for (int i = 0; i < foundFiles.size(); i++) {
	                 System.out.println("Found file:" + foundFiles.get(i));
	     			//刪除所有符合搜尋條件的文件
	                 delete(deletePath + "/" + foundFiles.get(i));
	             }
	         }

}
