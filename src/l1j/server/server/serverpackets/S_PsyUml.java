     package l1j.server.server.serverpackets;

     import l1j.server.MJTemplate.MJString;
     import l1j.server.PowerBall.PowerBallController;
     import l1j.server.PowerBall.PowerBallInfo;
     import l1j.server.server.Controller.BugRaceController;

     public class S_PsyUml extends ServerBasePacket {
         public static S_PsyUml bugRaceUml(boolean is_viewed_gm) {
             PsyUmlArgs args = new PsyUmlArgs(); // 創建新的 PsyUmlArgs 對象
             args.pageTitle = "萊蒂"; // 設置頁面標題
             args.pageDescription = "好啦好啦，這次運動會即將參賽的魔法娃娃們的狀態是這樣的......"; // 設置頁面描述

             if (is_viewed_gm) {
                 int index = 0;
                 args.pageArguments[index++] = "名稱  "; // 設置參數標題：名稱
                 args.pageArguments[index++] = "狀態  "; // 設置參數標題：狀態
                 args.pageArguments[index++] = "勝率  "; // 設置參數標題：勝率
                 for (int i = 0; i < 5; i++) {
                     args.pageArguments[index++] = String.format("%s ", new Object[] { (BugRaceController.getInstance())._littleBugBear[i].getName() }); // 設置參數值：名稱
                     args.pageArguments[index++] = String.format("%s ", new Object[] { (BugRaceController.getInstance())._bugCondition[i] }); // 設置參數值：狀態
                     args.pageArguments[index++] = String.format("%s ", new Object[] { Double.valueOf((BugRaceController.getInstance())._winRate[i]) }); // 設置參數值：勝率
                 }
             } else {
                 int index = 0;
                 args.pageArguments[index++] = "名稱  "; // 設置參數標題：名稱
                 args.pageArguments[index++] = "狀態  "; // 設置參數標題：狀態
                 args.pageArguments[index++] = "勝率  "; // 設置參數標題：勝率
                 for (int i = 0; i < 5; i++) {
                     args.pageArguments[index++] = String.format("%s ", new Object[] { (BugRaceController.getInstance())._littleBugBear[i].getName() }); // 設置參數值：名稱
                     args.pageArguments[index++] = String.format("%s ", new Object[] { (BugRaceController.getInstance())._bugCondition[i] }); // 設置參數值：狀態
                     args.pageArguments[index++] = String.format("%s ", new Object[] { Double.valueOf((BugRaceController.getInstance())._winRate[i]) }); // 設置參數值：勝率
                 }
             }
             return new S_PsyUml(args); // 返回新的 S_PsyUml 對象
         }
     }

         return new S_PsyUml(args);
       }
         private static final int PageArgumentsLength = 18; // 頁面參數長度

         public static S_PsyUml powerBallUml() {
             PsyUmlArgs args = new PsyUmlArgs(); // 創建新的 PsyUmlArgs 對象
             args.pageTitle = "朱諾"; // 設置頁面標題

             PowerBallInfo pInfo = PowerBallController.getinfo(); // 獲取 PowerBallInfo 信息
             if (pInfo == null) {
                 // 如果 pInfo 為 null，設置頁面描述為遊戲已中止
                 args.pageDescription = "強力球遊戲目前已被遊戲管理員暫停.";
             } else {
                 // 如果 pInfo 不為 null，設置頁面描述和參數
                 args.pageDescription = String.format("[%s] 次數回 [一般球]", new Object[] { pInfo.getDate(), Integer.valueOf(pInfo.getNum()) });

                 args.pageArguments[3] = "組合號碼 "; // 設置參數索引 3 的值
                 args.pageArguments[4] = String.format("%d[%s]  ", new Object[] { Integer.valueOf(pInfo.getPlusNum()), pInfo.getoddEven() }); // 設置參數索引 4 的值
                 args.pageArguments[5] = String.format("[%s]  ", new Object[] { pInfo.getUnderOver() }); // 設置參數索引 5 的值
             }
             return new S_PsyUml(args); // 返回新的 S_PsyUml 對象
         }



       public S_PsyUml(PsyUmlArgs args) {
         writeC(144);
         writeD(args.objectId);
         writeS("psy");
         writeC(0);
         writeH(20);
         safeWriteS(args.pageTitle);
         safeWriteS(args.pageDescription);
         for (String s : args.pageArguments) {
           safeWriteS(s);
         }
         writeH(0);
       }

       private void safeWriteS(String s) {
         writeS(MJString.isNullOrEmpty(s) ? "" : s);
       }


       public byte[] getContent() {
         return getBytes();
       }



       public static class PsyUmlArgs
       {
         public int objectId;


         public String pageTitle;


         public String pageDescription;


         public String[] pageArguments = new String[18];
       }
     }


