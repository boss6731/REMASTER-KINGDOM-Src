         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Object;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_Message_YN;
         import l1j.server.server.serverpackets.S_SkillSound;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;

         public class L1Ress
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Ress();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               int objid = pc.getId();
               for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
                 if (tg.getCurrentHp() == 0 && tg.isDead()) {
                   tg.broadcastPacket((ServerBasePacket)new S_SkillSound(tg.getId(), 3944));
                   tg.sendPackets((ServerBasePacket)new S_SkillSound(tg.getId(), 3944));
                   tg.setTempID(objid);
                   tg.sendPackets((ServerBasePacket)new S_Message_YN(322, "")); continue;
                 }
                 tg.broadcastPacket((ServerBasePacket)new S_SkillSound(tg.getId(), 832));
                 tg.sendPackets((ServerBasePacket)new S_SkillSound(tg.getId(), 832));
                 tg.setCurrentHp(tg.getMaxHp());
                 tg.setCurrentMp(tg.getMaxMp());
               }

             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 命令錯誤"));
             }
           }
         }


