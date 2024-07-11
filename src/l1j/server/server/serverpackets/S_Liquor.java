 package l1j.server.server.serverpackets;

 public class S_Liquor
   extends ServerBasePacket {
   private static final String _S__19_LIQUOR = "[S] S_Liquor";

   public S_Liquor(int objecId, int type) {
     writeC(229);
     writeD(objecId);
     writeC(type);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Liquor";
   }
 }


