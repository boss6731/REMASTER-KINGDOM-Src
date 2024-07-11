 package BITOPERATION;

 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.InputStreamReader;

 public class SystemMotherBoardNumber
 {
   public static String getSystemMotherBoard_SerialNumber() {
     try {
       String OSName = System.getProperty("os.name");
       if (OSName.contains("Windows")) {
         return getWindowsMotherboard_SerialNumber();
       }

       return GetLinuxMotherBoard_serialNumber();

     }
     catch (Exception E) {
       System.err.println("系統主機板經驗： " + E.getMessage());
       return null;
     }
   }

   private static String getWindowsMotherboard_SerialNumber() {
     String result = "";
     try {
       File file = File.createTempFile("realhowto", ".vbs");
       file.deleteOnExit();
       FileWriter fw = new FileWriter(file);

       String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \n對於 colItems 中的每個 objItem \n    Wscript.Echo objItem.SerialNumber \n    exit for ' 只執行第一個 cpu！ \n下一個 \n";








       fw.write(vbs);
       fw.close();

       Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
       BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
       String line;
       while ((line = input.readLine()) != null) {
         result = result + line;
       }
       input.close();
     }
     catch (Exception E) {
       System.err.println("Windows 主機板實驗： " + E.getMessage());
     }
     return result.trim();
   }

   private static String GetLinuxMotherBoard_serialNumber() {
     String command = "dmidecode -s baseboard-serial-number";
     String sNum = null;
     try {
       Process SerNumProcess = Runtime.getRuntime().exec(command);
       BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));
       sNum = sNumReader.readLine().trim();
       SerNumProcess.waitFor();
       sNumReader.close();
     }
     catch (Exception ex) {
       System.err.println("Linux 主機板實驗： " + ex.getMessage());
       sNum = null;
     }
     return sNum;
   }

   public static void main(String[] args) {
     String motherBoard_SerialNumber = getSystemMotherBoard_SerialNumber();
     System.out.println("主機板序號： " + motherBoard_SerialNumber);
   }
 }


