 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;



 public class S_PetMenuPacket
   extends ServerBasePacket
 {
   public S_PetMenuPacket(L1NpcInstance npc, int exppercet) {
     buildpacket(npc, exppercet);
   }

   private void buildpacket(L1NpcInstance npc, int exppercet) {
     writeC(144);

     if (npc instanceof L1PetInstance) {
       L1PetInstance pet = (L1PetInstance)npc;
       writeD(pet.getId());
       writeS("anicom");
       writeC(0);
       writeH(10);
       switch (pet.getCurrentPetStatus()) {
         case 1:
           writeS("$469");
           break;
         case 2:
           writeS("$470");
           break;
         case 3:
           writeS("$471");
           break;
         case 5:
           writeS("$472");
           break;
         case 8:
           writeS("$613");
           break;
         default:
           writeS("$471");
           break;
       }
       writeS(Integer.toString(pet.getCurrentHp()));
       writeS(Integer.toString(pet.getMaxHp()));
       writeS(Integer.toString(pet.getCurrentMp()));
       writeS(Integer.toString(pet.getMaxMp()));
       writeS(Integer.toString(pet.getLevel()));


       writeS(pet.getName());
       writeS("$612");
       writeS(Integer.toString(exppercet));
       writeS(Integer.toString(pet.getLawful()));
     } else if (npc instanceof L1SummonInstance) {
       L1SummonInstance summon = (L1SummonInstance)npc;
       writeD(summon.getId());
       writeS("moncom");
       writeC(0);
       writeH(9);
       switch (summon.get_currentPetStatus()) {
         case 1:
           writeS("$469");
           break;
         case 2:
           writeS("$470");
           break;
         case 3:
           writeS("$471");
           break;
         case 5:
           writeS("$472");
           break;
         default:
           writeS("$471");
           break;
       }
       writeS(Integer.toString(summon.getCurrentHp()));
       writeS(Integer.toString(summon.getMaxHp()));
       writeS(Integer.toString(summon.getCurrentMp()));
       writeS(Integer.toString(summon.getMaxMp()));
       writeS(Integer.toString(summon.getLevel()));
     }
   }

   public byte[] getContent() {
     return getBytes();
   }
 }


