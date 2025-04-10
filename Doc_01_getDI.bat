@echo off
java -version

java -cp ".;c:\java\FTPS_DIIOP_fcb_IOR\commons-net-3.8.0.jar;c:\java\FTPS_DIIOP_fcb_IOR\NCSO.jar" com.cti.common.ftps.mget "192.168.1.22" "2121" "192.168.1.21" "FCB/FTPSAccountSetup.nsf" "ap02" "/DocDI/FirstClass" "*.*" "D:\\TAKE\\FIRSTCLASS"
java -cp ".;c:\java\FTPS_DIIOP_fcb_IOR\commons-net-3.8.0.jar;c:\java\FTPS_DIIOP_fcb_IOR\NCSO.jar" com.cti.common.ftps.mget "192.168.1.22" "2121" "192.168.1.21" "FCB/FTPSAccountSetup.nsf" "ap02" "/DocDI/FirstClass/Attach" "*.*" "D:\\TAKE\\FIRSTCLASS\\Attach"
