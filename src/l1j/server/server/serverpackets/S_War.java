 package l1j.server.server.serverpackets;




 public class S_War
   extends ServerBasePacket
 {
   private static final String S_WAR = "[S] S_War";

   public S_War(int type, String clan_name1, String clan_name2) {
     buildPacket(type, clan_name1, clan_name2);
   }


   private void buildPacket(int type, String clan_name1, String clan_name2) {
     writeC(63);
     writeC(type);
     writeS(clan_name1);
     writeS(clan_name2);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_War";
   }
 }


