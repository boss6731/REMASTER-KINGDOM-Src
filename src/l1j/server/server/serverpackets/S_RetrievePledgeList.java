 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.Warehouse.ClanWarehouse;
 import l1j.server.server.model.Warehouse.WarehouseManager;



 public class S_RetrievePledgeList
   extends ServerBasePacket
 {
   public boolean NonValue = false;
   public boolean 正在使用 = false;

   public S_RetrievePledgeList(int objid, L1PcInstance pc) {
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     if (clan == null) {
       return;
     }

       ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName()); // 獲取家族倉庫

       if (!clanWarehouse.setWarehouseUsingChar(pc.getId(), 0)) { // 嘗試設置當前用戶為使用者，失敗則進行下一步
           int id = clanWarehouse.getWarehouseUsingChar(); // 獲取當前使用者ID

           L1Object prevUser = L1World.getInstance().findObject(id); // 通過ID獲取用戶對象

           if (prevUser instanceof L1PcInstance) { // 檢查prevUser是否為L1PcInstance類型
               L1PcInstance usingPc = (L1PcInstance)prevUser; // 轉換為L1PcInstance

               if (usingPc.getClan() == clan) { // 檢查usingPc是否屬於同一個家族
                   pc.sendPackets("血盟成員 " + usingPc.getName() + "您正在使用血盟倉庫，請稍等片刻再使用."); // 通知當前用戶
                   this.正在使用 = true; // 設置標誌表示倉庫正在被使用
                   return; // 結束方法執行
               }
           }

           if (!clanWarehouse.setWarehouseUsingChar(pc.getId(), id)) { // 再次嘗試設置當前用戶為使用者
               pc.sendPackets("血盟成員 " + clanWarehouse.getName() + "您正在使用血盟倉庫，請稍等片刻再使用."); // 再次通知當前用戶
               this.正在使用 = true; // 設置標誌表示倉庫正在被使用
               return; // 結束方法執行
           }
       }

     if (pc.getInventory().getSize() < 200) {
       int size = clanWarehouse.getSize();
       if (size > 0) {
         writeC(127);
         writeD(objid);
         writeH(size);
         writeC(5);
         L1ItemInstance item = null;
         for (Object itemObject : clanWarehouse.getItems()) {
           item = (L1ItemInstance)itemObject;
           writeD(item.getId());
           writeC(item.getItem().getType2());
           writeH(item.get_gfxid());
           writeC(item.getItem().getBless());
           writeD(item.getCount());
           writeC(item.isIdentified() ? 1 : 0);
           writeS(item.getViewName());
           writeC(0);
         }
         writeD(500);
         writeD(0);
         writeH(0);
       } else {
         this.NonValue = true;
       }
     } else {
       clanWarehouse.setWarehouseUsingChar(0, 0);
       pc.sendPackets(new S_ServerMessage(263));
     }
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


