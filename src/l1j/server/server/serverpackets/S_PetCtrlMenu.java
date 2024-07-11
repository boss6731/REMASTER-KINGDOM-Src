 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;



 public class S_PetCtrlMenu
   extends ServerBasePacket
 {
   private static final String S_PetCtrlMenu = "[S] S_PetCtrlMenu";

   public S_PetCtrlMenu(L1PcInstance pc, L1NpcInstance npc, boolean open) {
     buildPacket(pc, npc, open);
   }


   private void buildPacket(L1PcInstance pc, L1NpcInstance npc, boolean open) {
     writeC(43);
     writeC(12);

     if (open) {
       writeH(pc.getPetList().size() * 3);
       writeD(0);
       writeD(npc.getId());
       writeH(npc.getMapId());
       writeH(0);
       writeH(npc.getX());
       writeH(npc.getY());
       if (npc instanceof l1j.server.server.model.Instance.L1PetInstance) {
         writeC(1);
       } else {
         writeC(0);
       }
       writeS(npc.getNameId());
     } else {
       writeH(pc.getPetList().size() * 3 - 3);
       writeD(1);
       writeD(npc.getId());
     }
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_PetCtrlMenu";
   }
 }


