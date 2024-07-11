 package l1j.server.server.model;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1Question
         implements Runnable
 {
     private static L1Question _instance;
     public static String maintext;
     public static int good;
     public static int bad;
     public static boolean mainstart;
     public static final int EXECUTE_STATUS_NONE = 0;
     public static final int EXECUTE_STATUS_PREPARE = 1;
     public static final int EXECUTE_STATUS_PROGRESS = 3;
     public static final int EXECUTE_STATUS_FINALIZE = 4;
     private int _executeStatus = 0;

     public static L1Question getInstance(String text) {
         if (_instance == null) {
             _instance = new L1Question(text);
         }
         return _instance;
     }

     private L1Question(String text) {
         good = 0;
         bad = 0;
         maintext = text;

         GeneralThreadPool.getInstance().execute(this);
     }


     public void run() {
         try {
             switch (this._executeStatus) {

                 case 0:
                     mainstart = true; // 初始化變量，表示主過程已經開始
                     L1World.getInstance().broadcastServerMessage(" \fY調查即將開始。 （時間限制：30秒）"); // 廣播伺服器消息，通知玩家調查將開始
                     L1World.getInstance().broadcastServerMessage(" YES = 同意, NO = 反對, 如何回應調查 否則無效~!"); // 廣播伺服器消息，告知玩家如何回應調查

                     this._executeStatus = 1; // 將執行狀態設置為 1
                     GeneralThreadPool.getInstance().schedule(this, 3000L); // 調度此任務在 3 秒後執行
                     break;

                 case 1:
                     L1World.getInstance().broadcastPacketToAll((ServerBasePacket) new S_Message_YN(24, 6008, maintext)); // 向所有玩家廣播調查信息

                     this._executeStatus = 3; // 將執行狀態設置為 3
                     GeneralThreadPool.getInstance().schedule(this, 30000L); // 調度此任務在 30 秒後執行
                     break;

                 case 3:
                     L1World.getInstance().broadcastServerMessage("調查結果即將公佈."); // 廣播伺服器消息，通知玩家調查結果即將公佈 //잠시 후 설문조사 결과가 발표됩니다

                     this._executeStatus = 4; // 將執行狀態設置為 4
                     GeneralThreadPool.getInstance().schedule(this, 3000L); // 調度此任務在 3 秒後執行
                     break;

// 當狀態為 4 時的處理邏輯應該在後續代碼中實現
             }
         } catch (Exception e) {
             e.printStackTrace(); // 處理異常，打印堆棧跟蹤
         }
     }




                             case 4:
                                 L1World.getInstance().broadcastServerMessage(" \\fW【結果】贊成： " + good + "圖形, 反對 : " + bad + "圖形");
                                 _instance = null;
                                 mainstart = false;
                                 maintext = "";
                                 break;
             }

         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }


