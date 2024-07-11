 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import java.io.File;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1ClanJoinInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class C_LeaveClan
   extends ClientBasePacket
 {
   private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";

   public C_LeaveClan(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);
     L1PcInstance player = clientthread.getActiveChar();
     if (player == null) {
       return;
     }
     if (MJShiftBattlePlayManager.is_shift_battle(player)) {
       return;
     }
     String clanname = readS();
     L1Clan clan = L1World.getInstance().findClan(clanname);

     if (clan == null) {
       return;
     }
     int clan_id = clan.getClanId();

     if (clan_id == 0) {
       return;
     }


     if (player.isCrown() && player.getId() == clan.getLeaderId()) {
       if (clan.getAllianceList() != null) {
         String alliance_clanlist = clan.getAllianceList().toString();
         int idx = alliance_clanlist.indexOf("[") + 1;
         int idx2 = alliance_clanlist.lastIndexOf("]");
         if (idx > -1 && idx2 > -1) {
           String claninfo = alliance_clanlist.substring(idx, idx2);
           String[] alliance_clan_id = (String[])MJArrangeParser.parsing(claninfo, ", ", MJArrangeParseeFactory.createStringArrange()).result();
           for (int i = 0; i < alliance_clan_id.length; i++) {
             if (clan_id != Integer.parseInt(alliance_clan_id[i])) {
               L1Clan alliance_clan = L1World.getInstance().getClan(Integer.parseInt(alliance_clan_id[i]));
               if (alliance_clan != null) {
                 alliance_clan.removeAlliance(clan_id);
                 ClanTable.getInstance().updateClan(alliance_clan);
               }
             }
           }
         }
       }
       leaveClanBoss(clan, player);
     } else {
       leaveClanMember(clan, player);
     }
   }

   private void leaveClanBoss(L1Clan clan, L1PcInstance player) throws Exception {
     String player_name = player.getName();
     String clan_name = player.getClanname();

     if (clan.getCastleId() > 0 || clan.getHouseId() > 0) {
       player.sendPackets((ServerBasePacket)new S_ServerMessage(665));

       return;
     }
     if (clan.getCurrentWar() != null) {
       player.sendPackets((ServerBasePacket)new S_ServerMessage(302));

       return;
     }
     if (clan.AllianceSize() > 0) {
       player.sendPackets((ServerBasePacket)new S_ServerMessage(1235));

       return;
     }
     L1PcInstance pc = null;
     for (int i = 0; i < clan.getClanMemberList().size(); i++) {
       L1ClanJoinInstance.removeInstance(player.getClanid());

       pc = L1World.getInstance().getPlayer(((L1Clan.ClanMember)clan.getClanMemberList().get(i)).name);

       if (pc == null) {

         pc = CharacterTable.getInstance().restoreCharacter(((L1Clan.ClanMember)clan.getClanMemberList().get(i)).name);
       } else {

         pc.sendPackets((ServerBasePacket)new S_ServerMessage(269, player_name, clan_name));
       }
       pc.ClearPlayerClanData(clan);
     }

     String emblem_file = String.valueOf(player.getClanid());
     File file = new File("emblem/" + emblem_file);
     file.delete();
     ClanTable.getInstance().deleteClan(clan_name);
   }

   public static void leaveClanMember(L1Clan clan, L1PcInstance player) throws Exception {
     String player_name = player.getName();
     String clan_name = player.getClanname();
     L1PcInstance[] clanMember = clan.getOnlineClanMember();

     for (int i = 0; i < clanMember.length; i++) {
       clanMember[i].sendPackets((ServerBasePacket)new S_ServerMessage(178, player_name, clan_name));
       player.start_teleport(player.getX(), player.getY(), player.getMapId(), player.getHeading(), 18339, false, false);
     }
     if (player.isClanBuff()) {
       player.killSkillEffectTimer(7789);
       player.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, false));
       player.setClanBuff(false);
     }
     player.ClearPlayerClanData(clan);
     clan.removeClanMember(player_name);
   }



   public String getType() {
     return "[C] C_LeaveClan";
   }
 }


