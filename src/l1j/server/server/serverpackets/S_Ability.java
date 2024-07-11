         package l1j.server.server.serverpackets;


         public class S_Ability
           extends ServerBasePacket
         {
           private static final String S_ABILITY = "[S] S_Ability";

           public S_Ability(int type, boolean equipped) {
             buildPacket(type, equipped);
           }

           private void buildPacket(int type, boolean equipped) {
             writeC(122);
             writeC(type);
             if (equipped) {
               writeC(1);
             } else {
               writeC(0);
             }
             writeC(2);
             writeH(0);
           }


           public byte[] getContent() {
             return getBytes();
           }


           public String getType() {
             return "[S] S_Ability";
           }
         }


