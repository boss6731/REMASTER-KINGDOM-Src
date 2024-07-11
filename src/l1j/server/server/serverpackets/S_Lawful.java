 package l1j.server.server.serverpackets;


 public class S_Lawful
   extends ServerBasePacket
 {
   private static final String S_LAWFUL = "[S] S_Lawful";

   public S_Lawful(int objid, int lawful) {
     buildPacket(objid, lawful);
   }

   private void buildPacket(int objid, int lawful) {
     writeC(84);
     writeD(objid);
     writeH(lawful);
     writeD(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Lawful";
   }
 }


