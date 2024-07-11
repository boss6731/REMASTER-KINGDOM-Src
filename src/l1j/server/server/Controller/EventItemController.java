 package l1j.server.server.Controller;

 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.TimeZone;
 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class EventItemController implements Runnable {
   private static EventItemController _instance;
   private static final SimpleDateFormat _sdf = new SimpleDateFormat("HHmm");
   public static final int SleepTime = 60000;

   public static EventItemController getInstance() {
     if (_instance == null) {
       _instance = new EventItemController();
     }
     return _instance;
   }


   public void run() {
     try {
       checkEventItem();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private Calendar getRealTime() {
     TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
     Calendar cal = Calendar.getInstance(_tz);
     return cal;
   }

     private void checkEventItem() {
         Calendar realTime = getRealTime(); // 註解: 獲取當前的實時日曆
         int nowTime = Integer.valueOf(_sdf.format(realTime.getTime())).intValue(); // 註解: 將實時轉換為整數時間格式
         int EventTime = Config.ServerRates.EventTime; // 註解: 獲取事件時間間隔
         int EventNumber = Config.ServerRates.EventNumber; // 註解: 獲取事件物品數量
         int EventItem = Config.ServerRates.EventItem; // 註解: 獲取事件物品 ID
         if (EventTime == 0) // 註解: 如果事件時間間隔為 0，則返回
             return;
         if (nowTime % EventTime == 0) { // 註解: 如果當前時間是事件時間的倍數
             for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) { // 註解: 遍歷所有玩家
                 if (!pc.isDead() && !pc.isPrivateShop() && !pc.noPlayerCK && pc != null) { // 註解: 判斷玩家是否符合條件
                     L1ItemInstance item = pc.getInventory().storeItem(EventItem, EventNumber); // 註解: 將事件物品存入玩家背包
                     if (item != null)
                         pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + " (" + EventNumber + ") 獲得")); // 註解: 發送系統消息通知玩家獲得物品
                 }
             }
         } else {
             return; // 註解: 如果當前時間不是事件時間的倍數，則返回
         }
     }


