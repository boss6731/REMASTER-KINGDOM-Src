 package l1j.server.server.serverpackets;


 public class S_SelectTarget
   extends ServerBasePacket
 {
   private static final String S_SELECT_TARGET = "[S] S_SelectTarget";
   private byte[] _byte = null;

   public S_SelectTarget(int ObjectId) {
     writeC(22);
     writeD(ObjectId);
     writeC(0);
     writeC(0);
     writeC(2);
   }


   public byte[] getContent() {
     if (this._byte == null) {
       this._byte = getBytes();
     }
     return this._byte;
   }

   public String getType() {
     return "[S] S_SelectTarget";
   }
 }


