 package l1j.server.server.serverpackets;



 public class S_Restart
   extends ServerBasePacket
 {
   private byte[] _byte = null;

   public S_Restart(int objid, int type) {
     buildPacket(objid, type);
   }
   private static final String S_INVIS = "[S] S_Invis";
   private void buildPacket(int objid, int type) {
     writeC(89);
     writeD(objid);
     writeC(type);
   }


   public byte[] getContent() {
     if (this._byte == null) {
       this._byte = getBytes();
     }
     return getBytes();
   }


   public String getType() {
     return "[S] S_Invis";
   }
 }


