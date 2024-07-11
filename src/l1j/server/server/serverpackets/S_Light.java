 package l1j.server.server.serverpackets;




 public class S_Light
   extends ServerBasePacket
 {
   private static final String S_LIGHT = "[S] S_Light";

   public S_Light(int objid, int type) {
     buildPacket(objid, type);
   }

   private void buildPacket(int objid, int type) {
     writeC(206);
     writeD(objid);
     writeC(type);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Light";
   }
 }


