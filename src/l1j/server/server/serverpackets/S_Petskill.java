 package l1j.server.server.serverpackets;

 import l1j.server.MJCompanion.Instance.MJCompanionInstance;


 public class S_Petskill
   extends ServerBasePacket
 {
   private static final String S_PetWindow = "[S] S_PetWindow";
   private byte[] _byte = null;

   public static final int DogBlood = 14;

   public S_Petskill(int Op, MJCompanionInstance Pet, boolean check) {
     buildPacket(Op, Pet, check);
   }

   private void buildPacket(int Op, MJCompanionInstance Pet, boolean check) {
     writeC(19);
     writeC(208);
     writeC(7);

     writeC(8);
     writeBit(Pet.getId());

     switch (Op) {
       case 14:
         writeC(144);
         writeC(2);
         if (check) {
           writeBit(100L); break;
         }  writeBit(12L);
         break;
     }
     writeH(0);
   }


   public String getType() {
     return "[S] S_PetWindow";
   }


   public byte[] getContent() {
     if (this._byte == null) {
       this._byte = getBytes();
     }
     return this._byte;
   }
 }


