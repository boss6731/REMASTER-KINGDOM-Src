 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;



 public class S_UseMap
   extends ServerBasePacket
 {
   private static final String S_USE_MAP = "[S] S_UseMap";

   public S_UseMap(L1PcInstance pc, int objid, int itemid) {
     writeC(132);
     writeD(objid);

     switch (itemid) { case 40373:
         writeD(16); break;
       case 40374: writeD(1); break;
       case 40375: writeD(2); break;
       case 40376: writeD(3); break;
       case 40377: writeD(4); break;
       case 40378: writeD(5); break;
       case 40379: writeD(6); break;
       case 40380: writeD(7); break;
       case 40381: writeD(8); break;
       case 40382: writeD(9); break;
       case 40383: writeD(10); break;
       case 40384: writeD(11); break;
       case 40385: writeD(12); break;
       case 40386: writeD(13); break;
       case 40387: writeD(14); break;
       case 40388: writeD(15); break;
       case 40389: writeD(17); break;
       case 40390: writeD(18);
         break; }

   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_UseMap";
   }
 }


