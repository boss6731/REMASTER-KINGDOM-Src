 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_FixWeaponList;
 import l1j.server.server.serverpackets.ServerBasePacket;










 public class C_FixWeaponList
   extends ClientBasePacket
 {
   private static final String C_FIX_WEAPON_LIST = "[C] C_FixWeaponList";

   public C_FixWeaponList(byte[] abyte0, GameClient clientthread) {
     super(abyte0);
     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null)
       return;  pc.sendPackets((ServerBasePacket)new S_FixWeaponList(pc));
   }


   public String getType() {
     return "[C] C_FixWeaponList";
   }
 }


