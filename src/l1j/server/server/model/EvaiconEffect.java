 package l1j.server.server.model;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
 import l1j.server.server.serverpackets.ServerBasePacket;


 class EvaiconEffect
   implements L1ArmorSetEffect
 {
   public void giveEffect(L1PcInstance pc) {
     pc.sendPackets((ServerBasePacket)new S_SkillIconBlessOfEva(pc.getId(), -1));
   }


   public void cancelEffect(L1PcInstance pc) {
     pc.sendPackets((ServerBasePacket)new S_SkillIconBlessOfEva(pc.getId(), 0));
   }
 }


