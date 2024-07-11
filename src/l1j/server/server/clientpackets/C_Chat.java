 package l1j.server.server.clientpackets;

 import l1j.server.Config;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.SpamTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1ExcludingList;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_NewChat;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Chat
   extends ClientBasePacket
 {
   private static final String C_CHAT = "[C] C_Chat";
   public static final int MACRO = 12;

   public C_Chat(byte[] abyte0, GameClient clientthread) {
     super(abyte0);
     if (clientthread == null) {
       return;
     }
     readC();
     L1PcInstance pc = clientthread.getActiveChar();

     if (pc == null) {
       return;
     }
     chatWorld(pc, readS());
   }

   private void chatWorld(L1PcInstance pc, String chatText) {
     if (pc.isGm() || pc.getLevel() >= Config.ServerAdSetting.GLOBALCHATLEVEL) {
       if (pc.isGm() || L1World.getInstance().isWorldChatElabled()) {
         if (pc.get_food() >= 12) {
           S_PacketBox pb = new S_PacketBox(11, pc.get_food());
           pc.sendPackets((ServerBasePacket)pb, true);
           S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
           pc.sendPackets((ServerBasePacket)pb2, true);
           if (pc.isGm()) {
             L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_NewChat(pc, 4, 3, chatText, "[******] "));
             return;
           }
           if (pc.hasSkillEffect(1005) && !pc.isGm()) {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(242));
             return;
           }
           for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
             L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
             if (!spamList15.contains(0, pc.getName())) {
               S_NewChat cp = new S_NewChat(pc, 4, 12, chatText, "");
               S_NewChat cp2 = new S_NewChat(pc, 4, 3, chatText, "");
               listner.sendPackets((ServerBasePacket)cp, true);
               listner.sendPackets((ServerBasePacket)cp2, true);
             }
           }
         } else {
           S_ServerMessage sm = new S_ServerMessage(462);
           pc.sendPackets((ServerBasePacket)sm, true);
         }
       } else {
         S_ServerMessage sm = new S_ServerMessage(510);
         pc.sendPackets((ServerBasePacket)sm, true);
       }
     } else {
       S_ServerMessage sm = new S_ServerMessage(195, String.valueOf(Config.ServerAdSetting.GLOBALCHATLEVEL));
       pc.sendPackets((ServerBasePacket)sm, true);
     }
   }


   public String getType() {
     return "[C] C_Chat";
   }
 }


