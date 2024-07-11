 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1NpcInstance;

 public class S_NoSell
   extends ServerBasePacket {
   private static final String _S__25_NoSell = "[S] _S__25_NoSell";

   public S_NoSell(L1NpcInstance npc) {
     buildPacket(npc);
   }

   private void buildPacket(L1NpcInstance npc) {
     writeC(144);
     writeD(npc.getId());

     writeS("incence");
     writeC(1);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] _S__25_NoSell";
   }
 }


