package com.cti.common.ftps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPSUtils {	
	private static FTPSClient ftps;
	protected int ftpConnectTimeout = 60 * 1000;
	public int ERROR_FTPDOWNLOAD = 5;
	public int ERROR_FTPUPLOAD = 6;
	public int ERROR_FTPDELETE = 7;
	public static boolean connect(String host, int port, String username, String password) {
		boolean isOK=false;
		
		String ftpServerName = host;
		int ftpServerPort = port;
		String ftpUserName = username; 
		String ftpPassword =password;  
		
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
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftps.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }else {
            	//System.out.println("準備登入!!");
            }
			boolean loginSatisfactorio = ftps.login(ftpUserName, ftpPassword);
			if(loginSatisfactorio) {
				System.out.println("Hello '"+ftpUserName+"' login success!!");
				isOK=true;
			}else {
				System.err.println("login fail!!");
			}
            ftps.execPBSZ(0);
            ftps.execPROT("P");
            /// ... // transfer files
            //ftp.setBufferSize(1000);
            //passive on
            ftps.enterLocalPassiveMode();
            ftps.setControlEncoding("MS950");
		} catch (IOException e) {
			System.err.println("FTPs server err.");
		}
		return isOK;
	}
	
    /**
     * @return void
     * @exception :(Error note)
     * @param filepath 要上傳的檔案路徑
     * @param uploadpath 上傳的文件的路徑 
     * @param filename 上傳的檔案名，留空表示不改變
     * @throws Exception
     */
	public static void upload(String filepath ,String uploadpath , String filename){ 
		System.out.println("uploadFile : "+filepath);
		File file = new File(filepath); 
		createFolder(uploadpath);
		InputStream input;
		String uploadFileName = "";
		try {
			input = new FileInputStream(file);
			if(filename.equals("")) { 
				uploadFileName = file.getName();
        	}else {
        		uploadFileName = filename;
        	}
			ftps.storeFile(uploadFileName, input);     
			input.close();  //關閉輸入流
			System.out.println("uploadFile success!! ,path:"+uploadpath +", filename:"+uploadFileName);
		}catch (Exception e) {
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
	public static void upload(File file ,String path){ 
		try { 
			System.out.println( "file.isDirectory():" + file.isDirectory()  );
	        if(file.isDirectory()){ 
	        	System.out.println("產生資料夾:"+path);
	        	createFolder(path);
	        	String[] files = file.list();             
	            for (int i = 0; i < files.length; i++) {      
	                File file1 = new File(path+"\\"+file.getPath()+"\\"+files[i] );      
	                if(file1.isDirectory()){      
	                	upload(file1 , path );      
	                	ftps.changeToParentDirectory();      
	                }else{                    
	                    File file2 = new File(file.getPath()+"\\"+files[i]);      
	                    FileInputStream input = new FileInputStream(file2);      
	                    ftps.storeFile(file2.getName(), input);      
	                    input.close();                            
	                }                 
	            }  
	        }else{      
	            File file2 = new File(file.getPath());    
	            //System.out.println( " file.getPath() : " + file.getPath()  + " | file2.getName() : " + file2.getName() );
	            InputStream input = new FileInputStream(file2);   
	            createFolder(path);   
	            ftps.storeFile(file2.getName(), input);      
	            input.close();  //關閉輸入流
	        }
		} catch (Exception e) { 
			e.printStackTrace(); 
		} finally {
			System.out.println("uploadFile done");
		}
	}
	
	/**
     * @param downloadpath 下載的文件的路徑 
     * @param fileName  下載的文件名 
     * @param localPath 下載的文件本地路徑
     * @param localfileName 下載到本地的檔案名，留空表示不改變
     */
	static void download(String downloadpath , String fileName , String localPath ,String localfileName){   
		try {
			System.out.println("download file to :"+localPath);
			String downloadFileName = "";
			ftps.changeWorkingDirectory(downloadpath);
			// 列出該目錄下所有文件
            FTPFile[] fs = ftps.listFiles();
            // 遍曆所有文件，找到指定的文件
            for (FTPFile ff : fs) {
            	System.out.println("==>"+ff.getName());
	             if (ff.getName().equals(fileName)) {
	              // 根據絕對路徑初始化文件
	            	File file =new File(localPath);    
	            	//如果資料夾不存在則建立    
	            	if  (!file.exists()  && !file.isDirectory())      {       
	            	    System.out.println("Directory create :"+localPath);  
	            	    file.mkdir();    
	            	}else{  
	            	    System.out.println("Directory already exists:"+localPath);  
	            	}
	            	if(localfileName.equals("")) { 
	            		downloadFileName = ff.getName();
	            	}else {
	            		downloadFileName = localfileName;
	            	}
	            	System.out.println("downloadFileName:"+downloadFileName);  
	            	File localFile = new File(localPath , downloadFileName);
		            // 輸出流
		            OutputStream is = new FileOutputStream(localFile);
		            // 下載文件
		            ftps.retrieveFile(ff.getName(), is);
		            System.out.println("download success!! ,path:"+localPath+" ,filename:"+downloadFileName);
		            is.close();  
	             }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}  
    } 
	
	public static void rename(String OriginalFilePath ,String ChangeFilePath){ 
		System.out.println("rename file ,from:+"+OriginalFilePath+" ,to:"+ChangeFilePath);
		try {
			//File file =new File(OriginalFilePath);
			//if(file.exists()) {
				ftps.rename(OriginalFilePath, ChangeFilePath);
			//	System.out.println("rename success!!");
			//}else {
			//	System.out.println("files not find:"+OriginalFilePath + " ,rename fail!!");
			//}
		}catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public static void delete(String FilePath){ 
		System.out.println("delete file:"+FilePath);
		try {
			//File file =new File(FilePath);
			//if(file.exists()) {
				ftps.deleteFile(FilePath);
			//	System.out.println("delete success!!");
			//}else {
			//	System.out.println("files not find:"+FilePath + " ,delete fail!!");
			//}
			
		}catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void createFolder(String path){ 
		String f = "";
		try { 
			for (int ii = 0; ii < path.split("/").length; ii++) {
	    		f = f+"/"+path.split("/")[ii];
	    		ftps.makeDirectory(path.split("/")[ii]); 
	    		ftps.changeWorkingDirectory(f); 
	    	}
		}catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public static void disconnect(){
		if (ftps.isConnected()) {
			try {
				ftps.logout();
				ftps.disconnect();
				//System.out.println("disconnect success");
			} catch (Exception e) { 
				System.out.println("disconnect fail : "+e);
			}
		}
	}
}
