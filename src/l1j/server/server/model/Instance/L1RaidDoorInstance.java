 package l1j.server.server.model.Instance;

 import l1j.server.MJRaidSystem.MJRaidObject;
 import l1j.server.server.templates.L1Npc;


 public class L1RaidDoorInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private MJRaidObject _raidObj;

   public L1RaidDoorInstance(L1Npc template) {
     super(template);
   }

   public void setRaidObject(MJRaidObject obj) {
     this._raidObj = obj;
   }

   public MJRaidObject getRaidObject() {
     return this._raidObj;
   }


   public void onTalkAction(L1PcInstance pc) {
     if (this._raidObj == null) {
       return;
     }
     this._raidObj.doorMove(pc);
   }



   public void deleteMe() {
     super.deleteMe();
   }
 }


