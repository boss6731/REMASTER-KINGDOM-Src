 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;



 public class S_SummonPack
   extends ServerBasePacket
 {
   private static final String _S__1F_SUMMONPACK = "[S] S_SummonPack";
   private static final int STATUS_POISON = 1;

   public S_SummonPack(L1SummonInstance pet, L1PcInstance pc) {
     buildPacket(pet, pc, true);
   }


   public S_SummonPack(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
     buildPacket(pet, pc, isCheckMaster);
   }


   private void buildPacket(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
     writeC(186);
     writeH(pet.getX());
     writeH(pet.getY());
     writeD(pet.getId());

     writeH(pet.getCurrentSpriteId());
     writeC(pet.getStatus());
     writeC(pet.getHeading());
     writeC(pet.getLight().getChaLightSize());
     writeC(pet.getMoveSpeed());
     writeD(0);
     writeH(0);
     writeS(pet.getNameId());
     writeS(pet.getTitle());
     int status = 0;
     if (pet.getPoison() != null &&
       pet.getPoison().getEffectId() == 1) {
       status |= 0x1;
     }

     writeC(status);
     writeD(0);
     writeS(null);
     if (isCheckMaster && pet.isExsistMaster()) {
       writeS(pet.getMaster().getName());
     } else {
       writeS("");
     }
     writeC(0);

     if (pet.getMaster() != null && pet
       .getMaster().getId() == pc.getId()) {

       int percent = (pet.getMaxHp() != 0) ? (100 * pet.getCurrentHp() / pet.getMaxHp()) : 100;
       writeC(percent);
     } else {
       writeC(255);
     }
     writeC(0);
     writeC(pet.getLevel());
     writeC(0);
     writeC(255);
     writeC(255);
     writeC(0);
     writeC(0);
     if (pet.getMaster() != null && pet
       .getMaster().getId() == pc.getId()) {

       int percent = (pet.getMaxMp() != 0) ? (100 * pet.getCurrentMp() / pet.getMaxMp()) : 100;
       writeC(percent);
     } else {
       writeC(255);
     }
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_SummonPack";
   }
 }


