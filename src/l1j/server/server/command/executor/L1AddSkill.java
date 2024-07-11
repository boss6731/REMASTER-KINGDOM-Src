 package l1j.server.server.command.executor;

 import java.util.ArrayList;
 import java.util.StringTokenizer;
 import l1j.server.GameSystem.SkillBook.SkillBookInfo;
 import l1j.server.GameSystem.SkillBook.SkillBookLoader;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJPassiveSkill.MJPassiveInfo;
 import l1j.server.MJPassiveSkill.MJPassiveLoader;
 import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.server.SkillCheck;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;






 public class L1AddSkill
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1AddSkill();
   }


   public void execute(L1PcInstance gm, String cmdName, String arg) {
     try {
       int cnt = 0;
       String skill_name = "";
       int skill_id = 0;

       StringTokenizer st = new StringTokenizer(arg);
       String charname = st.nextToken();
       L1PcInstance pc = L1World.getInstance().getPlayer(charname);

       if (pc == null) {
         gm.sendPackets((ServerBasePacket)new S_SystemMessage("\\f3[" + charname + "] 這不是您要連結的角色。"));
       }
       int object_id = pc.getId();
       pc.sendPackets((ServerBasePacket)new S_SkillSound(object_id, 227));
       pc.broadcastPacket((ServerBasePacket)new S_SkillSound(object_id, 227));
       ArrayList<MJPassiveInfo> passives = null;
       ArrayList<SkillBookInfo> skills = null;
       ArrayList<SkillBookInfo> skills1 = null;
       SkillBookLoader.getInstance().setMaster(true);
       if (pc.isCrown()) {
         passives = MJPassiveLoader.getInstance().fromClassType(0);
         skills = SkillBookLoader.getInstance().fromClassType(0);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isKnight()) {
         passives = MJPassiveLoader.getInstance().fromClassType(1);
         skills = SkillBookLoader.getInstance().fromClassType(1);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isElf()) {
         passives = MJPassiveLoader.getInstance().fromClassType(2);
         skills = SkillBookLoader.getInstance().fromClassType(2);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isWizard()) {
         passives = MJPassiveLoader.getInstance().fromClassType(3);
         skills = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isDarkelf()) {
         passives = MJPassiveLoader.getInstance().fromClassType(4);
         skills = SkillBookLoader.getInstance().fromClassType(4);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isDragonknight()) {
         passives = MJPassiveLoader.getInstance().fromClassType(5);
         skills = SkillBookLoader.getInstance().fromClassType(5);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isBlackwizard()) {
         passives = MJPassiveLoader.getInstance().fromClassType(6);
         skills = SkillBookLoader.getInstance().fromClassType(6);
       } else if (pc.is전사()) {
         passives = MJPassiveLoader.getInstance().fromClassType(7);
         skills = SkillBookLoader.getInstance().fromClassType(7);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isFencer()) {
         passives = MJPassiveLoader.getInstance().fromClassType(8);
         skills = SkillBookLoader.getInstance().fromClassType(8);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       } else if (pc.isLancer()) {
         passives = MJPassiveLoader.getInstance().fromClassType(9);
         skills = SkillBookLoader.getInstance().fromClassType(9);
         skills1 = SkillBookLoader.getInstance().fromClassType(3);
       }
       if (skills != null) {
         for (SkillBookInfo sInfo : skills) {
           if (pc.isSkillMastery(sInfo.getSkillId())) {
             continue;
           }
           if (!sInfo.isMasterPossible()) {
             continue;
           }
           SkillBookLoader.getInstance().masterSkill(pc, sInfo, false);
         }
       }

       if (skills1 != null) {
         for (SkillBookInfo sInfo : skills1) {
           if (pc.isSkillMastery(sInfo.getSkillId())) {
             continue;
           }
           if (!sInfo.isMasterPossible()) {
             continue;
           }
           SkillBookLoader.getInstance().masterSkill(pc, sInfo, false);
         }
       }
       SkillBookLoader.getInstance().setMaster(false);
       if (passives != null) {
         for (MJPassiveInfo pInfo : passives) {
           if (pc.isPassive(pInfo.getPassiveId())) {
             continue;
           }
           int passiveId = pInfo.getPassiveId();
           if (passiveId == MJPassiveID.DOUBLE_BREAK_DESTINY.toInt() &&
             pc.hasSkillEffect(105)) {
             pc.removeSkillEffect(105);
           }

           if (passiveId == MJPassiveID.TACTICAL_ADVANCE.toInt()) {
             SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
             buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
             buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
             buff.set_on_icon_id(10154);
             buff.set_tooltip_str_id(8030);
             buff.set_is_good(true);
             pc.sendPackets((MJIProtoMessage)buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);

             pc.sendPackets((ServerBasePacket)new S_PacketBox(132, pc.getTotalER()));
             pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
             pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
           }
           pc.addPassive(pInfo);
           MJPassiveUserLoader.store(pc, pInfo, false);
         }
       }
       SkillCheck.getInstance().sendAllSkillList(pc);
       gm.sendPackets((ServerBasePacket)new S_SystemMessage("\\f3[" + charname + "] 角色技能大師完成"));
     } catch (Exception e) {
       gm.sendPackets((ServerBasePacket)new S_SystemMessage("\\f3 .技能大師[角色名稱]指令。"));
     }
   }
 }


