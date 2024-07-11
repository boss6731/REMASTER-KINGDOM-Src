 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Dungeon;
 import l1j.server.server.model.Instance.L1PcInstance;





 public class C_EnterPortal
   extends ClientBasePacket
 {
   private static final String C_ENTER_PORTAL = "[C] C_EnterPortal";

   public C_EnterPortal(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     int locx = readH();
     int locy = readH();
     L1PcInstance pc = client.getActiveChar();
     if (pc == null)
       return;  if (pc.get_teleport()) {
       return;
     }

     Dungeon.getInstance().dg(locx, locy, pc.getMap().getId(), pc);
   }


   public String getType() {
     return "[C] C_EnterPortal";
   }
 }


