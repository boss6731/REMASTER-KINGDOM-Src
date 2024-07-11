 package l1j.server.server.serverpackets;

 public class S_MassTeleportSwitch
   extends ServerBasePacket
 {
   private static final String S_MASS_TELEPORT_SWITCH = "[S] S_MassTeleportSwitch";
   private byte[] _byte = null;

   public static final int SWITCH = 2594;

   public static final S_MassTeleportSwitch ON = new S_MassTeleportSwitch(1);
   public static final S_MassTeleportSwitch OFF = new S_MassTeleportSwitch(0);

   public S_MassTeleportSwitch(int value) {
     writeC(19);
     writeH(2594);

     writeC(8);
     writeC(value);

     writeH(0);
   }


   public byte[] getContent() {
     if (this._byte == null) this._byte = this._bao.toByteArray();
     return this._byte;
   }


   public String getType() {
     return "[S] S_MassTeleportSwitch";
   }
 }


