 package l1j.server.server;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class SkillCheck {
   private HashMap<Integer, List<Integer>> _SkillCheck = new HashMap<>();


   private static SkillCheck _instance;



   public static SkillCheck getInstance() {
     if (_instance == null) {
       _instance = new SkillCheck();
     }
     return _instance;
   }

   public void AddSkill(int objid, List<Integer> skillList) {
     this._SkillCheck.put(Integer.valueOf(objid), skillList);
   }

   public boolean AddSkill(int objid, int skillId) {
     List<Integer> skillList = this._SkillCheck.get(Integer.valueOf(objid));
     if (skillList == null) {
       this._SkillCheck.put(Integer.valueOf(objid), new ArrayList<>());
       skillList = this._SkillCheck.get(Integer.valueOf(objid));
     }

     for (Iterator<Integer> iterator = skillList.iterator(); iterator.hasNext(); ) { int Id = ((Integer)iterator.next()).intValue();
       if (Id == skillId) {
         return false;
       } }


     skillList.add(Integer.valueOf(skillId));

     return true;
   }

   public List<Integer> CheckSkill(L1PcInstance pc) {
     List<Integer> skillList = this._SkillCheck.get(Integer.valueOf(pc.getId()));
     if (skillList == null || skillList.size() <= 0) {
       return null;
     }
     return skillList;
   }

   public boolean CheckSkill(L1PcInstance pc, int skillId) {
     List<Integer> skillList = this._SkillCheck.get(Integer.valueOf(pc.getId()));

     if (skillList == null) {
       return false;
     }

     for (Iterator<Integer> iterator = skillList.iterator(); iterator.hasNext(); ) { int Id = ((Integer)iterator.next()).intValue();
       if (Id == skillId) {
         return true;
       } }


     return false;
   }

   public void DelSkill(int objid, int skillId) {
     List<Integer> skillList = this._SkillCheck.get(Integer.valueOf(objid));

     if (skillList != null) {
       skillList.remove(Integer.valueOf(skillId));
     }
   }

   public void QuitDelSkill(L1PcInstance pc) {
     this._SkillCheck.remove(Integer.valueOf(pc.getId()));
   }

   public void sendAllSkillList(L1PcInstance pc) {
     List<Integer> skillList = this._SkillCheck.get(Integer.valueOf(pc.getId()));
     if (skillList == null || skillList.size() <= 0) {
       return;
     }

     SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
     for (Integer spellId : skillList) {
       noti.appendNewSpell(spellId.intValue(), true);
     }
     pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
   }
 }


