     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Clan;
     import l1j.server.server.model.L1World;

     public class S_WarStartMessage
       extends ServerBasePacket
     {
         private static final String[] clanNames = new String[] {
                 "肯特城",
                 "獸人要塞",
                 "溫達伍德城",
                 "奇岩城",
                 "海音城",
                 "地下城",
                 "亞丁城"
         };
       public static S_WarStartMessage get() {
         S_WarStartMessage p = new S_WarStartMessage();
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() - 1 > 0 && clan.getCastleId() - 1 < clanNames.length) {
             clanNames[clan.getCastleId() - 1] = clan.getClanName();
           }
         }
         for (int i = 0; i < clanNames.length; i++) {
           p.writeS(clanNames[i]);
         }
         return p;
       }

       private S_WarStartMessage() {
         writeC(108);
         writeC(78);
         writeC(7);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "S_WarStartMessage";
       }
     }


