 package l1j.server.server.command.executor;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1Clear
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1Clear();
   }

   public void execute(final L1PcInstance pc, String cmdName, String arg) {
     GeneralThreadPool.getInstance().execute(new Runnable()
         {
           public void run() {
             for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)pc, 20)) {
               if (obj instanceof L1MonsterInstance) {
                 L1MonsterInstance npc = (L1MonsterInstance)obj;
                 if (npc.getNpcId() == 50000220) {
                   continue;
                 }
                 npc.receiveDamage((L1Character)pc, 1000000);
                 if (npc.getCurrentHp() <= 0) {
                   pc.sendPackets((ServerBasePacket)new S_SkillSound(obj.getId(), 11748));
                   pc.broadcastPacket((ServerBasePacket)new S_SkillSound(obj.getId(), 11748)); continue;
                 }
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(obj.getId(), 11748));
                 pc.broadcastPacket((ServerBasePacket)new S_SkillSound(obj.getId(), 11748));
               }
             }
           }
         });
   }
 }


