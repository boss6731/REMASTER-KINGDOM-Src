     package l1j.server.server.serverpackets;


     public class S_Poison extends ServerBasePacket {
         private static final String S_POISON = "[S] S_Poison"; // 靜態常量，用於標識類別

         // 構造方法，接收對象ID和中毒類型
         public S_Poison(int objId, int type) {
             writeC(213); // 寫入常量 213
             writeD(objId); // 寫入對象ID

             // 根據中毒類型寫入對應的數據
             if (type == 0) {
                 writeC(0); // 寫入 0
                 writeC(0); // 寫入 0
             } else if (type == 1) {
                 writeC(1); // 寫入 1
                 writeC(0); // 寫入 0
             } else if (type == 2) {
                 writeC(0); // 寫入 0
                 writeC(1); // 寫入 1
             } else {
                 throw new IllegalArgumentException("拋出非法引數異常. type = " + type); // 拋出非法引數異常
             }
         }
     }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_Poison";
       }
     }


