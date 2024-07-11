 package l1j.server.server.clientpackets;

 import l1j.server.MJCharacterActionSystem.Walk.WalkActionHandler;
 import l1j.server.MJCharacterActionSystem.WalkActionHandlerFactory;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_MoveChar extends ClientBasePacket {
   public C_MoveChar(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);

     L1PcInstance pc = client.getActiveChar();
     if (pc == null || pc.get_teleport() || pc.isDead()) {
       return;
     }
     if (pc.isstop()) {
       return;
     }


     WalkActionHandler walkActionHandler = WalkActionHandlerFactory.create();
     walkActionHandler.parse(pc, this);
     walkActionHandler.doWork();
   }
 }


