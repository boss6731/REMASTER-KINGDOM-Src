     package l1j.server.server.serverpackets;

     public class S_CollectionNoti
       extends ServerBasePacket
     {
       private static final String S_COLLECTION_NOTI = "[S] S_CollectionNoti";
       private byte[] _byte = null;

       public static final int NOTI = 2653;

       public static final S_CollectionNoti LOGIN_START = new S_CollectionNoti();

       public S_CollectionNoti() {
         writeC(19);
         writeH(2653);
         writeH(0);
       }


       public byte[] getContent() {
         if (this._byte == null) {
           this._byte = this._bao.toByteArray();
         }
         return this._byte;
       }


       public String getType() {
         return "[S] S_CollectionNoti";
       }
     }


