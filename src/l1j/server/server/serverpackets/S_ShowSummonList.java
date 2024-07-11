 package l1j.server.server.serverpackets;

 public class S_ShowSummonList
   extends ServerBasePacket
 {
   private static final String S_ShowSummonList = "[S] S_ShowSummonList";

   public S_ShowSummonList(int objid) {
     writeC(144);
     writeD(objid);
     writeS("summonlist");
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_ShowSummonList";
   }
 }


