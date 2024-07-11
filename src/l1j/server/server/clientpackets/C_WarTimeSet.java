 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;




 public class C_WarTimeSet
   extends ClientBasePacket
 {
   private static final String C_WAR_TIME_SET = "[C] C_WarTimeSet";

   public C_WarTimeSet(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     clear();
   }


   public String getType() {
     return "[C] C_WarTimeSet";
   }
 }


