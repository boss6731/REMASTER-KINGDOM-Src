 package l1j.server.server.clientpackets;

 import java.io.FileOutputStream;
 import l1j.server.server.GameClient;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Emblem
   extends ClientBasePacket
 {
   private static final String C_EMBLEM = "[C] C_Emblem";

   public C_Emblem(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     L1PcInstance player = clientthread.getActiveChar();
     if (player == null)
       return;
     if (player.getClanRank() != 4 && player.getClanRank() != 10) {
       return;
     }
     if (player.getClanid() != 0) {
       FileOutputStream fos = null;


       try {
         byte[] buff = readByte();




           try {
               // 生成新的徽章ID
               int newEmblemdId = IdFactory.getInstance().nextId();
               String emblem_file = String.valueOf(newEmblemdId);

               // 使用新的徽章ID創建文件輸出流
               fos = new FileOutputStream("emblem/" + emblem_file);

               // 將徽章數據寫入文件
               fos.write(buff, 0, buff.length);

               // 獲取玩家所屬的血盟
               L1Clan clan = ClanTable.getInstance().getTemplate(player.getClanid());

               // 更新血盟的徽章ID
               clan.setEmblemId(newEmblemdId);
               ClanTable.getInstance().updateClan(clan);

               // 向血盟在線成員發送更新後的徽章數據
               for (L1PcInstance pc : clan.getOnlineClanMember()) {
                   pc.sendPackets((ServerBasePacket) new S_ReturnedStat(pc.getId(), newEmblemdId));
                   pc.broadcastPacket((ServerBasePacket) new S_ReturnedStat(pc.getId(), newEmblemdId));
               }
           } catch (Exception e) {
               // 捕捉異常並打印堆棧跟蹤
               e.printStackTrace();
               // 打印錯誤信息
               System.out.println(String.format("%s 徽章註冊錯誤", new Object[] { player.getName() }));
           } finally {
               // 最終關閉文件輸出流
               if (fos != null) {
                   fos.close();
               }
               fos = null;
           }



   public String getType() {
     return "[C] C_Emblem";
   }
 }


