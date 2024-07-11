 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_OwnCharStatus2 extends ServerBasePacket {
   private static final String _S__4F_S_OwnChraStatus2 = "[C] S_OwnCharStatus2";

   public S_OwnCharStatus2(L1PcInstance pc) {
     if (pc == null) {
       return;
     }
     this.cha = pc;
     writeC(223);
     writeH(this.cha.getAbility().getTotalStr());
     writeH(this.cha.getAbility().getTotalInt());
     writeH(this.cha.getAbility().getTotalWis());
     writeH(this.cha.getAbility().getTotalDex());
     writeH(this.cha.getAbility().getTotalCon());
     writeH(this.cha.getAbility().getTotalCha());
     writeC(this.cha.getInventory().getWeight100());
   }

   public S_OwnCharStatus2(L1PcInstance pc, boolean check) {
     if (pc == null) {
       return;
     }
     this.cha = pc;
     writeC(223);
     writeH(this.cha.getAbility().getTotalStr());
     writeH(this.cha.getAbility().getTotalInt());
     writeH(this.cha.getAbility().getTotalWis());
     writeH(this.cha.getAbility().getTotalDex());
     writeH(this.cha.getAbility().getTotalCon());
     writeH(this.cha.getAbility().getTotalCha());
     if (check) {
       writeC(0);
     }
   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[C] S_OwnCharStatus2";
   }


   private L1PcInstance cha = null;
 }


