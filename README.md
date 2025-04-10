
# FTPS_DIIOP_IOR

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

## ☕ 環境需求（Environment Requirements）

- Java JDK **1.8（建議使用 Java 8）**
- 已安裝 Git、可透過 CMD 操作
- 使用者需擁有 IBM Notes 安裝環境與合法的 `NCSO.jar`
- 若需從 Notes 資料庫取帳密，請確保 DIIOP 已啟用，且 `diiop_ior.txt` 正確

---

## 📁 專案結構

```bash
FTPS_DIIOP_IOR/
├── diiop_ior.txt               # Notes DIIOP IOR 憑證 (必要)
├── compiler&execute.txt        # 編譯與執行說明
├── compiler__Connect.sh        # 測試 FTPS 連線的 shell script
├── compiler__Download.sh       # 測試下載的 shell script
├── FTPSUtils.java              # FTPS 公用程式核心程式碼
├── *.java                      # 個別功能主程式（如 connect.java、upload.java 等）
└── lib/
    ├── commons-net-3.8.0.jar   # Apache Commons Net 套件
    └── NCSO.jar                # IBM Domino Java API
```

---

## ⚙️ 使用方式

### 1️⃣ 準備 `diiop_ior.txt`

請放置一份合法的 **DIIOP IOR 字串** 至 `diiop_ior.txt` 檔案，用來建立 Notes 連線。

### 2️⃣ 從 Notes DB 取得帳號與密碼

```java
String[] info = FTPSUtils.getFTPSLoginInfo(
    "192.168.1.21",
    "FCB/FTPSAccountSetup.nsf",
    "ap02"
);
```

---

## 🧪 編譯與執行範例

在 CMD 中操作：

### 🔧 編譯範例

```bash
javac -encoding utf-8 -d . -cp ".;C:\java\FTPS_DIIOP_fcb\*" FTPSUtils.java
```

### ▶️ 執行範例

```bash
java -cp ".;C:\java\FTPS_DIIOP_fcb\commons-net-3.8.0.jar;C:\java\FTPS_DIIOP_fcb\NCSO.jar" com.cti.common.ftps.connect 192.168.1.22 2121 192.168.1.21 FCB/FTPSAccountSetup.nsf ap02
```

（其他 upload、download、mput、mget、mdelete 指令請參考 `compiler&execute.txt`）

---

## 🧾 相依套件

- [`commons-net-3.8.0.jar`](https://commons.apache.org/proper/commons-net/)
- `NCSO.jar`（IBM Domino Java API）

---

## 📌 注意事項

- 預設使用編碼為 `MS950`，若有檔名亂碼可考慮改為 `UTF-8`
- 建議 `.sh` 檔案保留 Unix 換行格式，可使用 `.gitattributes` 設定：
  ```gitattributes
  *.sh text eol=lf
  ```

---

## 📜 授權

本專案僅供內部使用，未授權公開或商業用途。如需擴充或使用請聯繫專案負責人。
