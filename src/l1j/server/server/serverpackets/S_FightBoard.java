     package l1j.server.server.serverpackets;

     import l1j.server.PowerBall.PowerBallController;
     import l1j.server.PowerBall.PowerBallInfo;


     public class S_FightBoard
       extends ServerBasePacket
     {
       private static final String S_RaceBoard = "[C] S_RaceBoard";

       public S_FightBoard(int number) {
         buildPacket(number);
       }

         private void buildPacket(int number) {
             // 寫入操作碼 144
             writeC(144);
             // 寫入編號
             writeD(number);
             // 寫入字符串 "psy"
             writeS("psy");
             // 寫入常量 0
             writeC(0);
             // 寫入短整型 11
             writeH(11);
             // 寫入字符串 "주노"
             writeS("朱諾");

             // 獲取 PowerBallInfo 對象
             PowerBallInfo pInfo = PowerBallController.getinfo();
             if (pInfo == null) {
                 // 當 PowerBallInfo 為空時，寫入以下提示訊息
                 writeS("當前 PowerBall 遊戲已被遊戲管理員停止.");
                 // 寫入 11 個空字符串
                 for (int i = 0; i < 11; i++) {
                     writeS("");
                 }
             }
             // 假設這裡還會有其他代碼處理 pInfo 不為 null 的情況
         }
         } else {

           writeS(String.format("[%s] 第 %d 期 [一般球]", new Object[] { pInfo.getDate(), Integer.valueOf(pInfo.getNum()) }));
           int i;
           for (i = 0; i < 3; i++) {
             writeS("");
           }

           writeS("組合號碼  ");
           writeS(String.format("%d[%s]  ", new Object[] { Integer.valueOf(pInfo.getPlusNum()), pInfo.getoddEven() }));
           writeS(String.format("[%s]  ", new Object[] { pInfo.getUnderOver() }));


           for (i = 0; i < 3; i++) {
             writeS("");
           }
         }

         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[C] S_RaceBoard";
       }
     }


