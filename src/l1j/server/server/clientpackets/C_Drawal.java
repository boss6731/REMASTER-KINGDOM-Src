 package l1j.server.server.clientpackets;

 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 // 處理從城堡提取稅金的邏輯
 public class C_Drawal extends ClientBasePacket {
     private static final String C_DRAWAL = "[C] C_Drawal";

     public C_Drawal(byte[] abyte0, GameClient clientthread) throws Exception {
         super(abyte0);

         try {
             int i = readD(); // 讀取數據
             int j = readD(); // 讀取數據

                // 獲取當前活動角色
             L1PcInstance pc = clientthread.getActiveChar();
                // 獲取角色所屬的血盟
             L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

             if (clan != null) { // 如果角色有血盟
                 int castle_id = clan.getCastleId(); // 獲取血盟的城堡ID
                 if (castle_id != 0) { // 如果血盟擁有城堡
                     if (MJCastleWarBusiness.getInstance().isNowWar(clan.getCastleId())) { // 檢查城堡是否處於戰爭中
                         S_SystemMessage sm = new S_SystemMessage("在攻城戰期間無法提取稅金。");
                         pc.sendPackets((ServerBasePacket)sm, true); // 發送系統消息通知玩家
                         return; // 返回，不進行後續操作
                     }
           if (pc.getClanRank() != 10 || !pc.isCrown() || pc.getId() != pc.getClan().getLeaderId()) {
             return;
           }
           MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
           int money = war.getPublicMoney();
           long _money = money;
           if (_money <= 0L || money < j) {
             return;
           }
           money -= j;
           L1ItemInstance item = ItemTable.getInstance().createItem(40308);

           if (item != null) {
             war.setPublicMoney(money);
             MJCastleWarBusiness.getInstance().updateCastle(castle_id);
             if (pc.getInventory().checkAddItem(item, j) == 0) {
               pc.getInventory().storeItem(40308, j);
             } else {
               L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(40308, j);
             }
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(143, "$457", String.format("$4(%d)", new Object[] { Integer.valueOf(j) })));
           }
         }
       }
     } catch (Exception exception) {

     } finally {
       clear();
     }
   }


   public String getType() {
     return "[C] C_Drawal";
   }
 }


