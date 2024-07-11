 package l1j.server.server.clientpackets;

 import java.util.ArrayList;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_CONTRIBUTION_ACK;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_EinhasadClanBuff;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Pledge;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Pledge
   extends ClientBasePacket {
   private static final String C_PLEDGE = "[C] C_Pledge";

   public C_Pledge(byte[] data, GameClient client) throws Exception {
     super(data);
     if (client == null) {
       return;
     }
     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }
     if (pc.getClanid() > 0) {
       showPledgeWindow(pc);
     }
   }

   public static void showPledgeWindow(L1PcInstance pc) {
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     pc.sendPackets((ServerBasePacket)new S_Pledge(clan.getClanId()));
     ArrayList<String> page1 = new ArrayList<>();
     ArrayList<String> page2 = new ArrayList<>();
     ArrayList<String> page3 = new ArrayList<>();
     String[] members = clan.getAllMembersName();
     int div = 0;
     try {
       div = members.length / 90;
     } catch (Exception exception) {}

     if (div > 0) {
       for (int i = 0; i < members.length; i++) {



         if (i < 90) {
           page1.add(members[i]);
         } else if (i < 91) {
           page2.add(members[i]);
         } else {
           page3.add(members[i]);
         }
       }  if (page3.size() > 0) {
         div = 3;
       } else if (page2.size() > 0) {
         div = 2;
       } else {
         div = 1;
       }  if (page1.size() > 0)
         pc.sendPackets((ServerBasePacket)new S_Pledge(div, 0, page1));
       if (page2.size() > 0)
         pc.sendPackets((ServerBasePacket)new S_Pledge(div, 1, page2));
       if (page3.size() > 0) {
         pc.sendPackets((ServerBasePacket)new S_Pledge(div, 2, page3));
       }
       page1.clear();
       page2.clear();
       page3.clear();
       page1 = null;
       page2 = null;
       page3 = null;
     } else {
       for (int i = 0; i < members.length; i++)
       {

         page1.add(members[i]);
       }
       pc.sendPackets((ServerBasePacket)new S_Pledge(1, 0, page1));
     }


     pc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), pc.getClanRank(), pc));
     pc.sendPackets((ServerBasePacket)new S_PacketBox(171, (Object[])clan.getOnlineClanMember()));
     pc.sendPackets((ServerBasePacket)new S_Pledge(clan, clan.getBless()));
     pc.sendPackets((ServerBasePacket)new S_EinhasadClanBuff(pc));
     SC_BLOOD_PLEDGE_CONTRIBUTION_ACK.clan_contribution_send(pc);
   }


   public String getType() {
     return "[C] C_Pledge";
   }
 }


