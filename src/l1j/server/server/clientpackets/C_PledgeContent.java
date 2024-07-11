 package l1j.server.server.clientpackets;

 import l1j.server.MJTemplate.MJEncoding;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Pledge;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_PledgeContent
   extends ClientBasePacket
 {
   private static final String C_PledgeContent = "[C] C_PledgeContent";

   public C_PledgeContent(byte[] decrypt, GameClient clientthread) {
     super(decrypt);
     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null) {
       return;
     }

     if (pc.getClanid() == 0) {
       return;
     }

       int data = readC(); // 讀取整數數據
       if (data == 15) {
           String announce = readS(MJEncoding.EUCKR); // 以 EUCKR 編碼讀取字符串
           L1Clan clan = ClanTable.getInstance().getTemplate(pc.getClanid()); // 根據玩家的血盟 ID 獲取血盟模板
           if (clan != null) {
               clan.setAnnouncement(announce); // 設置血盟公告
               ClanTable.getInstance().updateClan(clan); // 更新血盟信息
               pc.sendPackets((ServerBasePacket)new S_PacketBox(168, announce)); // 發送公告封包
           } else {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("不存在的血盟。")); // 發送系統消息：不存在的血盟
           }
       } else if (data == 16) {
           String notes = readS(); // 讀取字符串
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid()); // 獲取玩家所屬的血盟
           if (clan != null) {
               clan.removeClanMember(pc.getName()); // 移除血盟成員
               clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), notes, pc.getId(), pc.getType(), pc.getOnlineStatus(), pc); // 添加血盟成員
               pc.setClanMemberNotes(notes); // 設置血盟成員備註
               pc.sendPackets((ServerBasePacket)new S_Pledge(pc.getName(), notes)); // 發送血盟封包
               try {
                   pc.save(); // 保存玩家數據
               } catch (Exception e) {
                   e.printStackTrace(); // 捕捉並打印異常
               }
           } else {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("不存在的血盟。")); // 發送系統消息：不存在的血盟
           }
       }
   }


   public String getType() {
     return "[C] C_PledgeContent";
   }
 }


