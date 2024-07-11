 package l1j.server.server.model.poison;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public abstract class L1Poison {
   protected static boolean isValidTarget(L1Character cha) {
     if (cha == null) {
       return false;
     }

     if (cha.getPoison() != null) {
       return false;
     }

     if (!(cha instanceof L1PcInstance)) {
       return true;
     }

     if (cha.hasSkillEffect(70705) || cha.hasSkillEffect(30003) || cha.hasSkillEffect(30004) || cha.hasSkillEffect(157)) {
       return false;
     }

     L1PcInstance player = (L1PcInstance)cha;
     if (player.getInventory().checkEquipped(20298) || player
       .getInventory().checkEquipped(20117) || player
       .getInventory().checkEquipped(22196) || player
       .getInventory().checkEquipped(22197) || player
       .getInventory().checkEquipped(22198) || player
       .getInventory().checkEquipped(22199) || player

       .getInventory().checkEquipped(900263) || player
       .getInventory().checkEquipped(900264) || player
       .getInventory().checkEquipped(900265) || player
       .hasSkillEffect(104)) {
       return false;
     }
     return true;
   }

   protected static void sendMessageIfPlayer(L1Character cha, int msgId) {
     if (!(cha instanceof L1PcInstance)) {
       return;
     }

     L1PcInstance player = (L1PcInstance)cha;
     player.sendPackets((ServerBasePacket)new S_ServerMessage(msgId));
   }

   public abstract int getEffectId();

   public abstract void cure();
 }


