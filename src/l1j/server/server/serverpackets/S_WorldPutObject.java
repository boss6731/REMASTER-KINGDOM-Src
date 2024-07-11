 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;


 public class S_WorldPutObject
   extends ServerBasePacket
 {
   public static final boolean IS_PRESENTATION_MARK = true;
   private static final int SC_WORLD_PUT_OBJECT_NOTI = 119;
   public L1PcInstance _pc = null;


   public static S_WorldPutObject get(byte[] b) {
     S_WorldPutObject s = new S_WorldPutObject();
     s.writeByte(b);
     return s;
   }

   private S_WorldPutObject() {
     writeC(19);
     writeH(119);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "S_WorldPutObject";
   }
 }


