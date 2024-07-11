 package l1j.server.server.model;

 import java.util.List;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1DeleteItemOnGround
 {
   private DeleteTimer _deleteTimer;
   public static final int EXECUTE_STATUS_NONE = 0;
   public static final int EXECUTE_STATUS_READY = 2;
   private int _executeStatus = 0;

   private static final int INTERVAL = Config.ServerAdSetting.ITEMDELETIONTIME * 60 * 1000 - 10000;

   private static final Logger _log = Logger.getLogger(L1DeleteItemOnGround.class.getName());






   private class DeleteTimer
     implements Runnable
   {
     public void run() {
       switch (L1DeleteItemOnGround.this._executeStatus) {
         case 0:
           L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(Config.Message.WorldDeleteCleaning));
           L1DeleteItemOnGround.this._executeStatus = 2;
           GeneralThreadPool.getInstance().schedule(this, 10000L);
           break;


         case 2:
           L1DeleteItemOnGround.this.deleteItem();

           L1DeleteItemOnGround.this._executeStatus = 0;
           GeneralThreadPool.getInstance().schedule(this, L1DeleteItemOnGround.INTERVAL);
           break;
       }
     }
   }


   public void initialize() {
     if (!Config.ServerAdSetting.ITEMDELETIONTYPE.equalsIgnoreCase("auto")) {
       return;
     }

     this._deleteTimer = new DeleteTimer();
     GeneralThreadPool.getInstance().schedule(this._deleteTimer, INTERVAL);
   }

    private void deleteItem() {
       // 初始刪除計數器
         int numOfDeleted = 0;
         // 用於存儲當前處理的物品
         L1ItemInstance item = null;
         // 用於存儲可見玩家的列表
         List<L1PcInstance> players = null;
         // 用於存儲地面上的物品庫存
         L1Inventory groundInventory = null;

         // 遍歷所有L1World中的對象
         for (L1Object obj : L1World.getInstance().getObject()) {
             // 如果不是L1ItemInstance則跳過
             if (!(obj instanceof L1ItemInstance)) {
                 continue;
             }

             // 將obj轉換為L1ItemInstance
             item = (L1ItemInstance) obj;

             // 如果物品的坐標為(0, 0)則跳過
             if (item.getX() == 0 && item.getY() == 0) {
                 continue;
             }

             // 如果物品ID為40515則跳過
             if (item.getItem().getItemId() == 40515) {
                 continue;
             }

             // 如果物品在房屋內則跳過
             if (L1HouseLocation.isInHouse(item.getX(), item.getY(), item.getMapId())) {
                 continue;
             }

             // 獲取物品周圍一定範圍內的可見玩家
             players = L1World.getInstance().getVisiblePlayer((L1Object) item, Config.ServerAdSetting.ITEMDELETIONRANGE);

             // 如果周圍沒有可見玩家，則刪除物品
             if (players.isEmpty()) {
                 // 獲取地面上的物品庫存

                 groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
                 // 從地面庫存中移除物品
                 groundInventory.removeItem(item);
                 // 刪除計數器增1
                 numOfDeleted++;
             }
         }

         // 日誌記錄刪除的物品數量
         _log.fine("自動刪除世界地圖上的物品。刪除數量: " + numOfDeleted);
    }


