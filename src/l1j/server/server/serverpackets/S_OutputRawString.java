 package l1j.server.server.serverpackets;



 public class S_OutputRawString
   extends ServerBasePacket
 {
   private static final String _S_OUTPUT_RAW_STRING = "[S] S_OutputRawString";
   private static final String _HTMLID = "withdraw";
   private byte[] _byte = null;

   public S_OutputRawString(int objId, String name, String text) {
     if (text.length() > 0) {
       buildPacket(objId, "withdraw", name, text);
     } else {
       close(objId);
     }
   }

   public void close(int objId) {
     buildPacket(objId, (String)null, (String)null, (String)null);
   }

   private void buildPacket(int objId, String html, String name, String text) {
     writeC(144);
     writeD(objId);
     writeS(html);
     writeH(2);
     writeH(2);
     writeS(name);
     writeS(text);
   }


   public byte[] getContent() {
     if (this._byte == null) {
       this._byte = getBytes();
     }
     return this._byte;
   }


   public String getType() {
     return "[S] S_OutputRawString";
   }
 }


