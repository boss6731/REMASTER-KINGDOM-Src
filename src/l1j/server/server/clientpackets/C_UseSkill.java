 package l1j.server.server.clientpackets;

 import l1j.server.MJCharacterActionSystem.Spell.SpellActionHandler;
 import l1j.server.MJCharacterActionSystem.SpellActionHandlerFactory;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_UseSkill extends ClientBasePacket {
   public C_UseSkill(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     L1PcInstance pc = client.getActiveChar();

     if (pc == null || pc.get_teleport() || pc.isDead()) {
       return;
     }
     int skillId = readH() + 1;

     SpellActionHandler spellActionHandler = SpellActionHandlerFactory.create(skillId);
     if (spellActionHandler == null) {
       return;
     }

     spellActionHandler.parse(pc, this);
     spellActionHandler.doWork();
   }
 }


