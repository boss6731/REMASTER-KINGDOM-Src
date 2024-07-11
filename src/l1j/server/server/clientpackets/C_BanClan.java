 package l1j.server.server.clientpackets;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_BanClan
   extends ClientBasePacket {
   private static final String C_BAN_CLAN = "[C] C_BanClan";
   private static Logger _log = Logger.getLogger(C_BanClan.class.getName());

   public C_BanClan(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     String s = readS();
     if (s == null || s.equals("")) {
       return;
     }
     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null) {
       return;
     }
     L1Clan clan = L1World.getInstance().findClan(pc.getClanname());
     if (clan != null)
     {
       if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {

         for (int i = 0; i < clan.getClanMemberList().size(); i++) {
           if (pc.getName().toLowerCase().equals(s.toLowerCase())) {
             return;
           }
         }

         int castle_id = clan.getCastleId();
         if (castle_id != 0 && MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(439));
           return;
         }
         L1PcInstance tempPc = L1World.getInstance().getPlayer(s);
         if (tempPc != null) {
           if (tempPc.getClanid() == pc.getClanid()) {
             tempPc.ClearPlayerClanData(clan);
             clan.removeClanMember(tempPc.getName());
             pc.sendPackets((ServerBasePacket)new S_PacketBox(pc, 26));
             tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(238, pc.getClanname()));

             pc.sendPackets((ServerBasePacket)new S_ServerMessage(240, tempPc.getName()));
           } else {

             pc.sendPackets((ServerBasePacket)new S_ServerMessage(109, s));
           }
         } else {

           try {
             L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(s);
             if (restorePc != null && restorePc.getClanid() == pc.getClanid()) {

               restorePc.ClearPlayerClanData(clan);
               clan.removeClanMember(restorePc.getName());
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(240, restorePc.getName()));
             } else {

               pc.sendPackets((ServerBasePacket)new S_ServerMessage(109, s));
             }

           } catch (Exception e) {
             _log.log(Level.SEVERE, "C_BanClan[]Error", e);
           }
         }
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(518));
       }
     }
   }

   public String getType() {
     return "[C] C_BanClan";
   }
 }


