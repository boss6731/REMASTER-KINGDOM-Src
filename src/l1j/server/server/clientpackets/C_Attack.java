 package l1j.server.server.clientpackets;

 import l1j.server.MJCharacterActionSystem.Attack.AttackActionHandler;
 import l1j.server.MJCharacterActionSystem.AttackActionHandlerFactory;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_Attack
   extends ClientBasePacket {
   public C_Attack(byte[] decrypt, GameClient client) {
     super(decrypt);

     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }
     if (pc.isstop()) {
       return;
     }
     AttackActionHandler attackActionHandler = AttackActionHandlerFactory.create();
     if (attackActionHandler == null) {
       return;
     }


     attackActionHandler.parse(pc, this);
     attackActionHandler.doWork();
   }
 }


