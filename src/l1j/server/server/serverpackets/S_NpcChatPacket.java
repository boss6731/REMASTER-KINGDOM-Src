 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.L1Character;




 public class S_NpcChatPacket
   extends ServerBasePacket
 {
   private static final String S_NPC_CHAT_PACKET = "[S] S_NpcChatPacket";
   public static final int NPC_CHAT_NOMAL_4 = 21;

   public S_NpcChatPacket(L1NpcInstance npc, String chat, int type) {
     buildPacket(npc, chat, type);
   }

   private void buildPacket(L1NpcInstance npc, String chat, int type) {
     switch (type) {
       case 0:
         writeC(61);

         writeC(type);
         writeD(npc.getId());



         if (npc.getNpcId() == 8500200 || npc.getNpcId() == 270043) {
           writeS(chat); break;
         }
         writeS(npc.getName() + ": " + chat);
         break;

       case 2:
         writeC(61);

         writeC(type);
         writeD(npc.getId());
         if (npc.getNpcTemplate().get_npcId() == 70518 || npc
           .getNpcTemplate().get_npcId() == 70506) {
           writeS(npc.getName() + ": " + chat); break;
         }
         writeS("" + npc.getName() + ": " + chat);
         break;


       case 3:
         writeC(61);
         writeC(type);
         writeD(npc.getId());
         writeS("[" + npc.getName() + "] " + chat);
         break;
       case 4:
         writeC(61);
         writeC(21);
         writeD(npc.getId());
         if (npc.getNpcId() == 8500200 || npc.getNpcId() == 270043) {
           writeS(chat); break;
         }
         writeS(npc.getName() + ": " + chat);
         break;
     }
   }



   public S_NpcChatPacket(L1Character cha, String chat) {
     writeC(61);
     writeC(0);
     writeD(cha.getId());
     writeS(chat);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_NpcChatPacket";
   }
 }


