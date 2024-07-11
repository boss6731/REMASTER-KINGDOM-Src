     package l1j.server.server.serverpackets;


     public class S_CloseList
       extends ServerBasePacket
     {
       public S_CloseList(int objid) {
         writeC(144);
         writeD(objid);
         writeS("");
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


