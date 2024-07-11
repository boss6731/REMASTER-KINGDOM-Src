         package l1j.server.server.command.executor;

         import java.util.Random;
         import java.util.StringTokenizer;
         import l1j.server.Config;
         import l1j.server.server.GeneralThreadPool;
         import l1j.server.server.RepeatTask;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1Userinc
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Userinc();
           }

           private static int _buffMaxCount = 0;
           private static int _remainBuffTime = 0;
           private static int _totalBuffTime = 0;

           private static Random _random = new Random(System.nanoTime());

           class UserCountBuffTimer extends RepeatTask {
             public UserCountBuffTimer() {
               super(2000L);
             }



             public void execute() {
               L1Userinc._remainBuffTime = L1Userinc._remainBuffTime - 2;

               if (L1Userinc._remainBuffTime < 1) {
                 L1Userinc._remainBuffTime = 0;

                 Config.ServerAdSetting.WHOISCONTER = L1Userinc._buffMaxCount;
                 cancel();
                 L1Userinc._UserCountBuffTimer = null;
               } else {
                 int incCount = L1Userinc._buffMaxCount * 2 / L1Userinc._totalBuffTime;

                 int additionalBuffRatio = L1Userinc._buffMaxCount * 1000 / L1Userinc._totalBuffTime % 1000;

                 if (L1Userinc._random.nextInt(1000) < additionalBuffRatio) {
                   incCount += 2;
                 }

                 Config.ServerAdSetting.WHOISCONTER += incCount;
               }
             }
           }

           private static UserCountBuffTimer _UserCountBuffTimer = null;


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               String inc = st.nextToken();

               if (inc.equalsIgnoreCase("重置")) {
                 if (_UserCountBuffTimer != null) {
                   _UserCountBuffTimer.cancel();
                   _UserCountBuffTimer = null;
                 }

                 Config.ServerAdSetting.WHOISCONTER = 0;
                 _buffMaxCount = 0;
                 _remainBuffTime = 0;
                 _totalBuffTime = 0; return;
               }
               if (inc.equalsIgnoreCase("0")) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("目前參與人數：" + Config.ServerAdSetting.WHOISCONTER));
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("參與總人數：" + _buffMaxCount));
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("剩餘數量:" + (_buffMaxCount - Config.ServerAdSetting.WHOISCONTER)));
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("總彈出時間：" + _totalBuffTime + "蠟燭"));
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("剩餘爆破時間：" + _remainBuffTime + "蠟燭"));

                 return;
               }
               int count = Integer.parseInt(st.nextToken());

               if (count < 0) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入一個大於 0 的數字。"));

                 return;
               }
               if (inc.equalsIgnoreCase("~")) {
                 int time = Integer.parseInt(st.nextToken());

                 if (time < 1) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入大於 0 的時間。"));

                   return;
                 }
                 if (time < _totalBuffTime) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("輸入的時間小於目前設定的彈出時間。"));

                   return;
                 }
                 if (count < _buffMaxCount) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("輸入的數量小於目前設定的詐騙者數量。"));

                   return;
                 }
                 if (_UserCountBuffTimer != null) {
                   _UserCountBuffTimer.cancel();
                   _UserCountBuffTimer = null;
                 }

                 _remainBuffTime += time - _totalBuffTime;
                 _totalBuffTime = time;
                 _buffMaxCount = count;

                 _UserCountBuffTimer = new UserCountBuffTimer();
                 GeneralThreadPool.getInstance().execute((Runnable)_UserCountBuffTimer);


                 return;
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("使用 +poof [0] 檢查目前狀態。"));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("+Poof [~] 自動彈出[數字][時間]（時間單位為秒）。"));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("+Pung 使用 [重置] 進行初始化。"));
             }
           }
         }


