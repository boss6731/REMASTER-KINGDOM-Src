 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;























 public class C_SelectTarget
   extends ClientBasePacket
 {
   private static final String C_SELECT_TARGET = "[C] C_SelectTarget";

   public C_SelectTarget(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     int petId = readD();
     int type = readC();
     int targetId = readD();

     L1Object petObject = L1World.getInstance().findObject(petId);
     L1Object targetObject = L1World.getInstance().findObject(targetId);
     if (petObject == null || !(petObject instanceof L1PetInstance))
       return;
     if (targetObject == null || !(targetObject instanceof L1Character)) {
       return;
     }
     L1PetInstance pet = (L1PetInstance)petObject;
     L1Character target = (L1Character)targetObject;
     if (target instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)target;
       if (pc.checkNonPvP(pc, (L1Character)pet)) {
         return;
       }
     }
     pet.setMasterTarget(target);
   }


   public String getType() {
     return "[C] C_SelectTarget";
   }
 }


