 package l1j.server.server.Controller;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Pledge;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class AuraController implements Runnable {
   private static AuraController _instance;

   public static AuraController getInstance() {
     if (_instance == null) {
       _instance = new AuraController();
     }
     return _instance;
   }


   public void run() {
     try {
       Clanbuff();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       GeneralThreadPool.getInstance().schedule(this, 2000L);
     }
   }

   private void Clanbuff() {
     try {
       for (L1Clan clan : L1World.getInstance().getAllClans()) {
         int bless = clan.getBless();
         int[] time = clan.getBuffTime();
         if (bless != 0) {
           clan.setBuffTime(bless - 1, time[bless - 1] - 1);
           if (clan.getBuffTime()[bless - 1] == 0) {
             for (L1PcInstance member : clan.getOnlineClanMember()) {
               member.sendPackets((ServerBasePacket)new S_Pledge(clan, bless));
               member.removeSkillEffect(bless + 504);
             }
             clan.setBless(0);
             ClanTable.getInstance().updateBless(clan.getClanId(), 0);
           }
         }
       }
     } catch (Exception exception) {}
   }
 }


