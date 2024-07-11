 package l1j.server.server.clientpackets;

 import l1j.server.server.Controller.FishingTimeController;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_FishClick
   extends ClientBasePacket {
   private static final String C_FISHCLICK = "[C] C_FishClick";

   public C_FishClick(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);
     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null) {
       return;
     }
     FishingTimeController.getInstance().endFishing(pc);
   }


   public String getType() {
     return "[C] C_FishClick";
   }
 }


