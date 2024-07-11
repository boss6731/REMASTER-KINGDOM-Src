 package l1j.server.server.serverpackets;


 public class S_ShowPolyList
   extends ServerBasePacket
 {
   public S_ShowPolyList(int objid) {
     writeC(144);
     writeD(objid);
     writeS("monlist");
   }

   public S_ShowPolyList(int objid, String str) {
     writeC(144);
     writeD(objid);
     writeS(str);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }


