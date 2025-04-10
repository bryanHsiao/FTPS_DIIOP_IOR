# FTPS_DIIOP_fcb_IOR

一個用於整合 Lotus Notes 資料庫與 FTPS 的 Java 公用程式，可用於上傳、下載、刪除檔案，並支援從 Notes DB 讀取 FTPS 帳密資訊。

---

## 📦 功能簡介

此工具提供以下功能：

- ✅ 使用 TLSv1.2 連線 FTPS
- ✅ 從 Notes DB 取得帳密資訊（透過 DIIOP 機制）
- ✅ 上傳單一檔案、整批檔案（支援 BINARY 模式）
- ✅ 下載單一或整批檔案（支援 BINARY 模式）
- ✅ 檔案重新命名與刪除
- ✅ 支援萬用字元搜尋（如 `*.txt`, `file_??.log`）
- ✅ 自動建立 FTP 路徑資料夾

---

## 📁 專案結構

```bash
FTPS_DIIOP_fcb_IOR/
├── diiop_ior.txt               # Notes DIIOP IOR 憑證 (必要)
├── compiler&execute.txt        # 編譯與執行說明
├── compiler__Connect.sh        # 測試 FTPS 連線的 shell script
├── compiler__Download.sh       # 測試下載的 shell script
├── FTPSUtils.java              # FTPS 公用程式核心程式碼
├── *.java                      # 個別功能主程式（如 connect.java、upload.java 等）
└── lib/
    ├── commons-net-3.8.0.jar   # Apache Commons Net 套件
    └── NCSO.jar                # IBM Domino Java API



⚙️ 使用方式
1️⃣ 準備 diiop_ior.txt
請放置一份合法的 DIIOP IOR 字串 至 diiop_ior.txt 檔案，用來建立 Notes 連線，例如：
IOR:000000000000...

2️⃣ 從 Notes DB 取得帳號與密碼
FTPSUtils 會使用以下方式連接 Notes DB：
String[] info = FTPSUtils.getFTPSLoginInfo(
    "192.168.1.21",                // Notes IP
    "FCB/FTPSAccountSetup.nsf",    // DB 路徑
    "ap02"                         // 查詢用的 Key
);
取得的帳號密碼會自動用來登入 FTPS 伺服器。

🧪 編譯與執行範例
以下指令可在 Windows 的 CMD 中使用（路徑請依實際環境調整）：

🔧 編譯範例
# 編譯主功能類別
javac -encoding utf-8 -d . -cp ".;C:\java\FTPS_DIIOP_fcb\*" FTPSUtils.java

# 編譯個別操作功能（如 connect）
javac -encoding utf-8 -d . -cp ".;C:\java\FTPS_DIIOP_fcb\*" connect.java

▶️ 執行範例
# 測試連線
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.connect 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02

# 上傳檔案
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.upload 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "D:\ftp_ShareFolder\download\20220711.xlsx" "testABCD/A/B" ""

# 上傳圖片（二進位模式）
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.upload_binary 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "D:\ftp_ShareFolder\upload\Autumn.jpg" "testABCD/A/B" ""

# 下載檔案（二進位模式）
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.download_binary 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "test3/X/" "20220711.xlsx" "D:\ftp_ShareFolder\download" ""

# 檔案重新命名
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.rename 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "test3/X/FmAPI.java" "test3/X/FmAPI_AAA.java"

# 刪除檔案
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.delete 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "test3/X/FmAPI_AAA.java"

# 整批上傳 (*.txt)
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.mput 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "D:\ftp_ShareFolder\M" "testABCD/A/B" "*.txt"

# 整批下載 (*.txt)
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.mget 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "testABCD/A/B" "*.txt" "D:\ftp_ShareFolder\download"

# 整批刪除 (*.txt)
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.mdelete 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02 "testABCD/A/B" "*.txt"

🧾 相依套件
commons-net-3.8.0.jar
NCSO.jar（IBM Domino Java API）

📌 注意事項
預設使用編碼為 MS950，若有檔名亂碼可考慮改為 UTF-8

若需轉移到 Linux 系統，建議將 .sh 檔案轉為 LF 格式
.gitattributes 可加入 *.sh text eol=lf 強制保留 Unix 換行符號

📜 授權
本專案僅供內部使用，未授權公開或商業用途。如需擴充或使用請聯繫專案負責人。
