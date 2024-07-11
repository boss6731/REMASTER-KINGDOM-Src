 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
























 public class S_PetPack
   extends ServerBasePacket
 {
   private static final String S_PET_PACK = "[S] S_PetPack";
   private static final int STATUS_POISON = 1;

   public S_PetPack(L1PetInstance pet, L1PcInstance pc) {
     buildPacket(pet, pc);
   }


   private void buildPacket(L1PetInstance pet, L1PcInstance pc) {
     writeC(186);
     writeH(pet.getX());
     writeH(pet.getY());
     writeD(pet.getId());
     writeH(pet.getCurrentSpriteId());
     writeC(pet.getStatus());
     writeC(pet.getHeading());
     writeC(pet.getLight().getChaLightSize());
     writeC(pet.getMoveSpeed());

     writeD(pet.get_exp());
     writeH(pet.getTempLawful());
     writeS(pet.getName());
     writeS(pet.getTitle());
     int status = 0;
     if (pet.getPoison() != null &&
       pet.getPoison().getEffectId() == 1) {
       status |= 0x1;
     }

     writeC(status);
     writeD(0);
     writeS(null);
     writeS((pet.getMaster() != null) ? pet.getMaster().getName() : "");
     writeC(0);

     if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
       int maxhp = pet.getMaxHp();
       try {
         writeC(100 * pet.getCurrentHp() / maxhp);
       } catch (ArithmeticException e) {
         writeC(0);
       }
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

     if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
       int maxmp = pet.getMaxMp();
       try {
         writeC(100 * pet.getCurrentMp() / maxmp);
       } catch (ArithmeticException e) {
         writeC(0);
       }
     } else {
       writeC(255);
     }
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_PetPack";
   }
 }


