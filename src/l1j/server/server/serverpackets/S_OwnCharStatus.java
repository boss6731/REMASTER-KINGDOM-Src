 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.gametime.GameTimeClock;

 public class S_OwnCharStatus extends ServerBasePacket {
   private static final String S_OWB_CHAR_STATUS = "[S] S_OwnCharStatus";

   public S_OwnCharStatus(L1PcInstance pc) {
     int time = GameTimeClock.getInstance().getGameTime().getSeconds();
     time -= time % 300;


     writeC(116);
     writeD(pc.getId());
     writeC(pc.getLevel());
     writeD(pc.get_exp());

     writeH(pc.getAbility().getTotalStr());
     writeH(pc.getAbility().getTotalInt());
     writeH(pc.getAbility().getTotalWis());
     writeH(pc.getAbility().getTotalDex());
     writeH(pc.getAbility().getTotalCon());
     writeH(pc.getAbility().getTotalCha());

     writeH(pc.getCurrentHp());

     writeH(pc.getMaxHp());
     writeH(pc.getCurrentMp());
     writeH(pc.getMaxMp());

     writeD(time);
     writeC(pc.get_food());
     writeC(pc.getInventory().getWeight100());
     writeH(pc.getLawful());
     if (pc.getResistance() != null) {
       writeH(pc.getResistance().getFire());
       writeH(pc.getResistance().getWater());
       writeH(pc.getResistance().getWind());
       writeH(pc.getResistance().getEarth());
     } else {
       writeD(0);
       writeD(0);
     }
     writeD(pc.getMonsterkill());
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_OwnCharStatus";
   }
 }


