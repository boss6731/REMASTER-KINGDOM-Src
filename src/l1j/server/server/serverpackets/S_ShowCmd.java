 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.utils.BinaryOutputStream;

 public class S_ShowCmd
   extends ServerBasePacket
 {
   private static final int SC_DEVELSERVER_MESSAGE_NOTI = 71;
   private static final int SC_SIEGE_OBJECT_PUT_NOTI = 65;
   private static final int SC_SIEGE_ZONE_UPDATE_NOTI = 66;
   private static final int SC_PK_MESSAGE_AT_BATTLE_SERVER = 583;
   private static final int SC_PLAY_MOVIE_NOTI = 581;
   private static final int SC_DEATH_PENALTY_STATUS = 463;
   private static final int SC_FATIGUE_NOTI = 334;
   private static final int SC_DAMAGE_OF_TIME_NOTI = 403;
   private static final int SC_BASECAMP_CHART_NOTI_PACKET = 127;
   private static final int SC_DIALOGUE_MESSAGE_NOTI = 580;

   public static S_ShowCmd getQuestDesc(int surf, int msgid) {
     S_ShowCmd s = new S_ShowCmd(580);
     s.writeC(10);
     BinaryOutputStream bos = new BinaryOutputStream();
     bos.writeC(8);
     bos.writeBit(surf);
     bos.writeC(16);
     bos.writeBit(msgid);
     bos.writeC(26);
     bos.writeS2("music1011");
     bos.writeC(32);
     bos.writeBit(5000L);
     byte[] data = bos.getBytes();
     try {
       bos.close();
     } catch (Exception exception) {}
     s.writeBit(data.length);
     s.writeByte(data);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getChart() {
     S_ShowCmd s = new S_ShowCmd(127);
     s.writeC(10);
     BinaryOutputStream bos = new BinaryOutputStream();
     bos.writeC(10);
     bos.writeS2("test1");
     bos.writeC(16);
     bos.writeC(5);
     byte[] data = bos.getBytes();
     try {
       bos.close();
     } catch (Exception exception) {}
     s.writeBit(data.length);
     s.writeByte(data);

     s.writeC(16);
     s.writeBit(75L);
     s.writeC(24);
     s.writeBit(1L);
     s.writeH(0);

     return s;
   }

   public static S_ShowCmd getDamageOfTimeNoti(L1Character c) {
     S_ShowCmd s = new S_ShowCmd(403);
     s.writeC(8);
     s.writeBit(c.getId());
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getFatigueNoti() {
     S_ShowCmd s = new S_ShowCmd(334);
     s.writeC(8);
     s.writeC(1);
     s.writeC(16);
     s.writeC(1);
     s.writeC(24);
     s.writeC(0);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getPkMessageAtBattleServer(String s1, String s2) {
     S_ShowCmd s = new S_ShowCmd(583);
     s.writeC(8);
     s.writeC(0);
     s.writeC(18);
     s.writeS2(s1);
     s.writeC(24);
     s.writeC(0);
     s.writeC(34);
     s.writeS2(s2);
     s.writeC(40);
     s.writeC(1);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getDeathPenalty(boolean isPenalty) {
     S_ShowCmd s = new S_ShowCmd(463);
     s.writeC(8);
     s.writeC(0);
     s.writeC(16);
     s.writeB(!isPenalty);
     s.writeC(24);
     s.writeC(0);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getPlayMovieNoti(String url, int sec) {
     S_ShowCmd s = new S_ShowCmd(581);
     s.writeC(10);
     s.writeS2(url);
     s.writeC(16);
     s.writeBit(-1L);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getSiegeZoneUpdateNoti(boolean ison) {
     S_ShowCmd s = new S_ShowCmd(66);
     s.writeC(8);
     s.writeC(ison ? 1 : 2);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getSiegeObjectPutNoti(L1Character c, int ico) {
     S_ShowCmd s = new S_ShowCmd(65);
     s.writeC(8);
     s.writeBit(c.getId());
     s.writeBit(16L);
     s.writeC(ico);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd get(String st) {
     S_ShowCmd s = new S_ShowCmd(71);


     s.writeC(8);
     s.writeC(1);
     s.writeC(18);
     s.writeS2(st);
     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getProto8(int n) {
     S_ShowCmd s = new S_ShowCmd(n);






     s.writeH(0);
     return s;
   }

   public static S_ShowCmd getProtoA(int n) {
     S_ShowCmd s = new S_ShowCmd(n);
     s.writeC(10);
     s.writeC(0);
     s.writeH(0);
     return s;
   }

   private S_ShowCmd(int i) {
     writeC(19);
     writeH(i);
   }



   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


