     package l1j.server.server.serverpackets;



     public class S_CastleMaster
       extends ServerBasePacket
     {
       private static final String _S__08_CASTLEMASTER = "[S] S_CastleMaster";

       public S_CastleMaster(int type, int objecId) {
         buildPacket(type, objecId);
       }

       private void buildPacket(int type, int objecId) {
         writeC(252);
         writeC(type);
         writeD(objecId);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_CastleMaster";
       }
     }


