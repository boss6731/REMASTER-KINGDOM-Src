 package l1j.server.server.clientpackets;

 import java.util.Calendar;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.NoTradable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Trade;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class C_TradeAddItem
   extends ClientBasePacket
 {
   private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";
   Calendar rightNow = Calendar.getInstance();
   int day = this.rightNow.get(5);
   int hour = this.rightNow.get(10);
   int min = this.rightNow.get(12);
   int year = this.rightNow.get(1);
   int month = this.rightNow.get(2) + 1;
   String totime = "[" + this.year + ":" + this.month + ":" + this.day + ":" + this.hour + ":" + this.min + "]";

   public C_TradeAddItem(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);

     int itemid = readD();
     int itemcount = readD();
     L1PcInstance pc = client.getActiveChar();
     if (pc == null)
       return;
     L1Trade trade = new L1Trade();
     L1ItemInstance item = pc.getInventory().getItem(itemid);
     if (item == null) {
       return;
     }

     if (itemid != item.getId()) {
       return;
     }
     if (!item.isStackable() && itemcount != 1) {
       return;
     }
     if (itemcount <= 0 || item.getCount() <= 0) {
       return;
     }
     if (itemcount > item.getCount()) {
       itemcount = item.getCount();
     }
       if (itemcount > 2000000000) {
           System.out.println("防止複製漏洞");
           return;
       }

       if (item.getItemId() == 100002 || item.getItemId() == 100003) {
           if (pc.getLevel() >= 70 && item.getItemId() == 100003) {
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "70級以上需要使用高級角色交換憑證。"));
               return;
           }

           if (pc.getLevel() < 70 && item.getItemId() == 100002) {
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "70級以下需要使用低級角色交換憑證。"));
               return;
           }
       }

       int itemId = item.getItem().getItemId();

// 檢查是否為非GM且物品不可交易或物品有過期時間
       if (!pc.isGm() && (NoTradable.getInstance().isNoTradable(itemId) || item.getEndTime() != null)) {
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("該物品無法進行交易。"));
           return;
       }

// 檢查物品是否可交易
       if (!item.getItem().isTradable()) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
           return;
       }

// 檢查物品是否有刻印（即物品是否可交易）
       if (item.get_Carving() != 0) {
           pc.sendPackets("刻印物品無法交易。");
           return;
       }
     if (item.getBless() >= 128) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
       return;
     }
     if (item.isEquipped()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(906));

       return;
     }
     if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

       return;
     }
     Object[] petlist = pc.getPetList().values().toArray();
     L1PetInstance pet = null;
     for (Object petObject : petlist) {
       if (petObject instanceof L1PetInstance) {
         pet = (L1PetInstance)petObject;
         if (item.getId() == pet.getItemObjId()) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           return;
         }
       }
     }
       L1DollInstance doll = pc.getMagicDoll();
       if (doll != null && item.getId() == doll.getItemObjId()) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName())); // 210: 無法交易該物品
           return;
       }

       L1PcInstance tradingPartner = (L1PcInstance)L1World.getInstance().findObject(pc.getTradeID());
       if (tradingPartner == null) {
           return;
       }

       if (pc.getTradeOk() || tradingPartner.getTradeOk()) {
           pc.sendPackets("無法添加物品：一方已確認完成交易");
           tradingPartner.sendPackets("無法添加物品：一方已確認完成交易");
           return;
       }

       if (tradingPartner.getInventory().checkAddItem(item, itemcount) != 0) {
           tradingPartner.sendPackets((ServerBasePacket)new S_ServerMessage(270)); // 270: 交易對象的物品過多，無法添加物品
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(271)); // 271: 自己的物品過多，無法添加物品
           return;
       }

       trade.TradeAddItem(pc, itemid, itemcount); // 將物品添加到交易中


   public String getType() {
     return "[C] C_TradeAddItem";
   }
 }


