 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_SpeedChange
   extends ServerBasePacket {
   private static final String S_SPEED_CHANGE = "[S] S_SpeedChange";
   private byte[] _byte = null;

   public static final int SPEED = 1036;
   public static final byte[] BANGUARD_SHORT_FORM_ON = new byte[] { 10, 4, 8, 0, 16, 35, 10, 4, 8, 1, 16, 15 };
   public static final byte[] BANGUARD_SHORT_FORM_OFF = new byte[] { 10, 4, 8, 0, 16, 0, 10, 4, 8, 1, 16, 0 };
   public static final byte[] BANGUARD_LONG_FORM_ON = new byte[] { 10, 4, 8, 1, 16, 10 };
   public static final byte[] BANGUARD_LONG_FORM_OFF = new byte[] { 10, 4, 8, 1, 16, 0 };
   public static final byte[] SHOCK_ATTACK_ON = new byte[] { 10, 13, 8, 0, 16, -35, -1, -1, -1, -1, -1, -1, -1, -1, 1 };
   public static final byte[] SHOCK_ATTACK_OFF = new byte[] { 10, 4, 8, 0, 16, 0 };
   public static final byte[] RAIGING_WEAPONE_ATTACK_ON = new byte[] { 10, 4, 8, 1, 16, 10 };
   public static final byte[] RAIGING_WEAPONE_ATTACK_OFF = new byte[] { 10, 4, 8, 1, 16, 0 };
   public static final byte[] BLIND_HIDING_ON = new byte[] { 10, 4, 8, 0, 16, 35 };
   public static final byte[] BLIND_HIDING_OFF = new byte[] { 10, 4, 8, 0, 16, 0 };
   public static final byte[] MADO_ON = new byte[] { 10, 4, 8, 0, 16, 5 };
   public static final byte[] MADO_OFF = new byte[] { 10, 4, 8, 0, 16, 0 };
   public static final byte[] ENSNARE_ON = new byte[] { 10, 4, 8, 0, 16, -50 };
   public static final byte[] ENSNARE_OFF = new byte[] { 10, 4, 8, 0, 16, 0 };

   public static final int SHOCK_ATTACK = 1;
   public static final int RAIGING_WEAPONE = 2;
   public static final int BLIND_HIDING = 3;
   public static final int MADO = 4;
   public static final int ENSNARE = 5;

   public S_SpeedChange(L1PcInstance pc, boolean longForm, boolean onoff) {
     writeC(19);
     writeH(1036);

     writeC(8);
     writeBit(pc.getId());

     writeC(18);
     byte[] banguardBytes = longForm ? (onoff ? BANGUARD_LONG_FORM_ON : BANGUARD_LONG_FORM_OFF) : (onoff ? BANGUARD_SHORT_FORM_ON : BANGUARD_SHORT_FORM_OFF);
     writeBytesWithLength(banguardBytes);

     writeH(0);
   }

   public S_SpeedChange(L1PcInstance pc, int type, boolean onoff) {
     byte[] speedBytes = null;
     switch (type) { case 1:
         speedBytes = onoff ? SHOCK_ATTACK_ON : SHOCK_ATTACK_OFF; break;
       case 2: speedBytes = onoff ? RAIGING_WEAPONE_ATTACK_ON : RAIGING_WEAPONE_ATTACK_OFF; break;
       case 3: speedBytes = onoff ? BLIND_HIDING_ON : BLIND_HIDING_OFF; break;
       case 4: speedBytes = onoff ? MADO_ON : MADO_OFF; break;
       case 5: speedBytes = onoff ? ENSNARE_ON : ENSNARE_OFF;
         break; }

     writeC(19);
     writeH(1036);

     writeC(8);
     writeBit(pc.getId());

     writeC(18);
     writeBytesWithLength(speedBytes);

     writeH(0);
   }


   public byte[] getContent() {
     if (this._byte == null) this._byte = this._bao.toByteArray();
     return this._byte;
   }


   public String getType() {
     return "[S] S_SpeedChange";
   }
 }


