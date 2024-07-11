 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;


 public class S_Party
   extends ServerBasePacket
 {
   private static final String _S_Party = "[S] S_Party";

   public S_Party(int type, L1PcInstance pc) {
     switch (type) {
       case 104:
         newMember(pc);
         break;
       case 105:
         oldMember(pc);
         break;
       case 106:
         changeLeader(pc);
       case 110:
         refreshParty(pc);
         break;
       case 1728:
         NameColor(pc, 0);
         break;
       case 1729:
         NameColor(pc, 1);
         break;
       case 1730:
         NameColor(pc, 2);
         break;
     }
   }

   public S_Party(String htmlid, int objid) {
     buildPacket(htmlid, objid, "", "", 0);
   }

   public S_Party(String htmlid, int objid, String partyname, String partymembers) {
     buildPacket(htmlid, objid, partyname, partymembers, 1);
   }

   private void buildPacket(String htmlid, int objid, String partyname, String partymembers, int type) {
     writeC(144);
     writeD(objid);
     writeS(htmlid);
     writeH(type);
     writeH(2);
     writeS(partyname);
     writeS(partymembers);
   }

   public void newMember(L1PcInstance pc) {
     if (pc.getParty() == null) {
       writeC(108);
       writeC(104);
       writeD(0);
     } else {
       L1PcInstance leader = pc.getParty().getLeader();
       L1PcInstance[] member = pc.getParty().getMembers();
       double nowhp = 0.0D;
       double maxhp = 0.0D;
       double nowmp = 0.0D;
       double maxmp = 0.0D;
       writeC(108);
       writeC(104);
       nowhp = leader.getCurrentHp();
       maxhp = leader.getMaxHp();
       nowmp = leader.getCurrentMp();
       maxmp = leader.getMaxMp();
       writeC(member.length - 1);
       for (int i = 0, a = member.length; i < a; i++) {
         if (member[i] != null) {

           nowhp = member[i].getCurrentHp();
           maxhp = member[i].getMaxHp();
           nowmp = member[i].getCurrentMp();
           maxmp = member[i].getMaxMp();
           writeD(member[i].getId());
           writeS(member[i].getName());
           writeC(member[i].getClassNumber());
           writeC(0);
           writeC(0);
           writeC((int)(nowhp / maxhp) * 100);
           writeC((int)(nowmp / maxmp) * 100);
           writeD(member[i].getMapId());
           writeH(member[i].getX());
           writeH(member[i].getY());
           writeD(0);
           writeC((member[i].getId() == leader.getId()) ? 1 : 0);
         }
       }
     }
   }

   public void oldMember(L1PcInstance pc) {
     writeC(108);
     writeC(105);
     writeD(pc.getId());
     writeS(pc.getName());
     writeC(pc.getClassNumber());
     writeC(0);
     writeC(0);
     writeD(pc.getMapId());
     writeH(pc.getX());
     writeH(pc.getY());
   }

   public void memberdie(L1PcInstance pc) {
     writeC(108);
     writeC(108);
     writeD(pc.getId());
     writeH(0);
   }

   public void NameColor(L1PcInstance pc, int type) {
     writeC(108);
     writeC(108);
     writeD(pc.getId());
     writeH(type);
   }

   public void changeLeader(L1PcInstance pc) {
     writeC(108);
     writeC(106);
     writeD(pc.getId());
     writeH(0);
   }

   public void refreshParty(L1PcInstance pc) {
     if (pc == null || pc.getParty() == null)
       return;  L1PcInstance[] member = pc.getParty().getMembers();
     writeC(108);
     writeC(110);
     writeC(member.length);
     for (int i = 0, a = member.length; i < a; i++) {
       writeD(member[i].getId());
       writeD(member[i].getMapId());
       writeH(member[i].getX());
       writeH(member[i].getY());
     }
     writeC(0);
   }



   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_Party";
   }
 }


