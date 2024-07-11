     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.Instance.L1SummonInstance;


     public class S_Test extends ServerBasePacket {
         private int _opcodeid; // 操作碼 ID
         private int _testlevel; // 測試級別
         private L1PcInstance _gm; // 遊戲管理員實例
         private final String _version = "S_HPMeter1.0"; // 版本號
         private final String _action = "只召喚一個召喚獸"; // 動作描述: "只召喚一個召喚獸"
         private final String _status = "召喚獸的 HP 計量器變動為 25%"; // 狀態描述: "召喚獸的 HP 計量器變動為 25%"
         int[][] _opcode; // 操作碼二維數組

         public S_Test(int OpCodeID, int TestLevel, L1PcInstance Player) {
             this._opcodeid = OpCodeID; // 初始化操作碼 ID
             this._testlevel = TestLevel; // 初始化測試級別
             this._gm = Player; // 初始化遊戲管理員實例
             this._opcode = new int[10][10]; // 假設初始化10x10的二維數組

             // 以下這些成員變量已在定義時初始化，不需要再覆蓋
             // this._version = "S_HPMeter1.0";
             // this._action = "사몬을 1마리만 내 둔다";
             // this._status = "사몬의 HP미터가25%에 변동한다";
         }

         // 其他方法和邏輯可以在這裡添加
     }



         this._opcode = new int[][] { { 2, 3, 4, 6, 8, 16, 17, 18, 19, 22, 24, 27, 31, 33, 34, 35, 37, 38, 40, 43, 47, 48, 49, 52, 54, 62, 65, 70, 72, 73, 74, 75, 76, 78, 80, 83, 84, 86, 87, 88, 89, 90, 91, 92, 93, 95, 98, 99, 101, 102, 104, 105, 107, 110, 112, 113, 114, 116, 117, 118, 119, 120, 121, 122, 124, 127, 128 }, { 0, 5, 9, 13, 42, 44, 50, 53, 55, 58, 60, 64, 77, 111, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139 }, { 1, 7, 10, 11, 12, 15, 20, 21, 23, 25, 26, 28, 29, 30, 32, 36, 39, 41, 45, 46, 51, 56, 57, 59, 61, 63, 66, 67, 68, 69, 71, 79, 81, 82, 85, 94, 96, 97, 100, 103, 106, 108, 109, 115, 123, 125, 126 } }; this._opcodeid = OpCodeID; this._testlevel = TestLevel; this._gm = Player; } public S_Test(byte[] data) { this._version = "S_HPMeter1.0"; this._action = "사몬을 1마리만 내 둔다"; this._status = "사몬의 HP미터가25%에 변동한다"; this._opcode = new int[][] { { 2, 3, 4, 6, 8, 16, 17, 18, 19, 22, 24, 27, 31, 33, 34, 35, 37, 38, 40, 43, 47, 48, 49, 52, 54, 62, 65, 70, 72, 73, 74, 75, 76, 78, 80, 83, 84, 86, 87, 88, 89, 90, 91, 92, 93, 95, 98, 99, 101, 102, 104, 105, 107, 110, 112, 113, 114, 116, 117, 118, 119, 120, 121, 122, 124, 127, 128 }, { 0, 5, 9, 13, 42, 44, 50, 53, 55, 58, 60, 64, 77, 111, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139 }, { 1, 7, 10, 11, 12, 15, 20, 21, 23, 25, 26, 28, 29, 30, 32, 36, 39, 41, 45, 46, 51, 56, 57, 59, 61, 63, 66, 67, 68, 69, 71, 79, 81, 82, 85, 94, 96, 97, 100, 103, 106, 108, 109, 115, 123, 125, 126 } };
         writeByte(data); }


       public byte[] getContent() {
         writeC(this._opcode[this._testlevel][this._opcodeid]);
         int objid = 0;
         Object[] petList = this._gm.getPetList().values().toArray();
         L1SummonInstance summon = null;
         for (Object pet : petList) {
           if (pet instanceof L1SummonInstance) {
             summon = (L1SummonInstance)pet;
             objid = summon.getId();
             break;
           }
         }
         writeD(objid);
         writeC(25);
         return getBytes();
       }

     public class S_Test extends ServerBasePacket {
         private int _opcodeid; // 操作碼 ID
         private int _testlevel; // 測試級別
         private L1PcInstance _gm; // 遊戲管理員實例
         private final String _version = "S_HPMeter1.0"; // 版本號
         private final String _action = "只召喚一個召喚獸"; // 動作描述
         private final String _status = "召喚獸的 HP 計量器變動為 25%"; // 狀態描述
         int[][] _opcode; // 操作碼二維數組

         public S_Test(int OpCodeID, int TestLevel, L1PcInstance Player) {
             this._opcodeid = OpCodeID; // 初始化操作碼 ID
             this._testlevel = TestLevel; // 初始化測試級別
             this._gm = Player; // 初始化遊戲管理員實例
             this._opcode = new int[10][10]; // 假設初始化10x10的二維數組
         }

         public String getInfo() {
            // 構建包含操作碼信息的字符串
             StringBuilder info = new StringBuilder();
             info.append(".opc 請輸入操作碼 ID。\n"); // 添加操作碼ID說明
                     info.append("[版本] ").append("S_HPMeter1.0"); // 添加版本信息
             info.append(" [等級] ").append(this._testlevel); // 添加測試級別
             info.append(" [ID範圍] 0 - ").append((this._opcode[this._testlevel]).length - 1).append("\n"); // 添加操作碼範圍
                     info.append("[上一個行動] ").append("只召喚一個召喚獸").append("\n"); // 添加前一個動作描述
                             info.append("[預期狀態] ").append("召喚獸的 HP 計量器變動為 25%").append("\n"); // 添加預期狀態描述
             return info.toString(); // 返回構建的字符串
         }

         public String getCode() {
            // 構建包含操作碼及其值的字符串
             StringBuilder info = new StringBuilder();
             info.append("[操作碼ID]").append(this._opcodeid).append(" [操作碼] ").append(this._opcode[this._testlevel][this._opcodeid]);
             return info.toString(); // 返回構建的字符串
         }

         public String getCodeList() {
            // 構建包含操作碼列表的字符串
             StringBuilder info = new StringBuilder();
             info.append(".opcid 請輸入操作碼 ID。\n"); // 添加操作碼ID說明
                     info.append("等級").append(this._testlevel).append("　0　　1　　2　　3　　4　　5　　6　　7　　8　　9\n"); // 添加標題行
             int t = 0;
             int tc = 10;
             for (int i = 0; i < (this._opcode[this._testlevel]).length; i++) {
                 if (tc == 10) {
                     if (t > 0)
                         info.append("\n"); // 添加換行符
                                 info.append(padt(t)); // 添加行首的序號
                     t++;
                     tc = 0;
                 }
                 info.append(pad(this._opcode[this._testlevel][i])); // 添加操作碼值
                 tc++;
             }
             return info.toString(); // 返回構建的字符串
         }

         // 假設這裡有兩個輔助方法 padt 和 pad 用於格式化輸出
         private String padt(int t) {
        // 格式化行首的序號

       private String pad(int i) {
         if (i < 10)
           return " 00" + i;
         if (i < 100)
           return " 0" + i;
         return " " + i;
       }

       private String padt(int i) {
         if (i < 10)
           return "0" + i + " ";
         return i + " ";
       }

       public String getType() {
         return "[S]  S_Test";
       }
     }


