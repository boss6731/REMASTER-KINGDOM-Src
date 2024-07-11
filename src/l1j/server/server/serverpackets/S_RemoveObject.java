 package l1j.server.server.serverpackets;

 import l1j.server.server.model.L1Object;

 public class S_RemoveObject
   extends ServerBasePacket {
   private static final String S_RemoveObject = "[S] S_RemoveObject";

   public S_RemoveObject(L1Object obj) {
     writeC(51);
     writeD(obj.getId());
     writeH(0);
   }

   public S_RemoveObject(int objId) {
     writeC(51);
     writeD(objId);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_RemoveObject";
   }
 }


