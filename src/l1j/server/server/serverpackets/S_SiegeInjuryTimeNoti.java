 package l1j.server.server.serverpackets;

 public class S_SiegeInjuryTimeNoti
   extends ServerBasePacket
 {
   private static final int SC_SIEGE_INJURY_TIME_NOIT = 76;

   public static S_SiegeInjuryTimeNoti get() {
     S_SiegeInjuryTimeNoti s = new S_SiegeInjuryTimeNoti();
     s.writeC(8);
     s.writeC(1);
     s.writeC(16);
     s.writeC(0);
     s.writeH(0);
     return s;
   }

   public static S_SiegeInjuryTimeNoti get(boolean isDefence, long sec, String cname) {
     S_SiegeInjuryTimeNoti s = new S_SiegeInjuryTimeNoti();
     s.writeC(8);
     s.writeC(isDefence ? 1 : 2);
     s.writeC(16);
     if (sec > 0L) {
       s.writeBit(sec * 2L);
       s.writeC(26);
       s.writeS2(cname);
     } else {
       s.writeC(0);
     }  s.writeH(0);
     return s;
   }

   private S_SiegeInjuryTimeNoti() {
     writeC(19);
     writeH(76);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }


