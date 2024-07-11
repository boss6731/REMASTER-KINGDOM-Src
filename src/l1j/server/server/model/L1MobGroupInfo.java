 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.server.model.Instance.L1NpcInstance;



 public class L1MobGroupInfo
 {
   private final List<L1NpcInstance> _membersList = new ArrayList<>();

   private L1NpcInstance _leader;

   private L1Spawn _spawn;
   private boolean _isRemoveGroup;

   public void setLeader(L1NpcInstance npc) {
     this._leader = npc;
   } public L1NpcInstance getLeader() {
     return this._leader;
   } public boolean isLeader(L1NpcInstance npc) {
     return (npc.getId() == this._leader.getId());
   }

   public void setSpawn(L1Spawn spawn) {
     this._spawn = spawn;
   } public L1Spawn getSpawn() {
     return this._spawn;
   }
   public void addMember(L1NpcInstance npc) {
     if (npc == null) {
       throw new NullPointerException();
     }

     if (this._membersList.isEmpty()) {
       setLeader(npc);
       if (npc.isReSpawn()) {
         setSpawn(npc.getSpawn());
       }
     }

     if (!this._membersList.contains(npc)) {
       this._membersList.add(npc);
     }
     npc.setMobGroupInfo(this);
     npc.setMobGroupId(this._leader.getId());
   }

   public synchronized int removeMember(L1NpcInstance npc) {
     if (npc == null) {
       throw new NullPointerException();
     }

     if (this._membersList.contains(npc)) {
       this._membersList.remove(npc);
     }
     npc.setMobGroupInfo(null);

     if (isLeader(npc)) {
       if (isRemoveGroup() && this._membersList.size() != 0) {
         for (L1NpcInstance minion : this._membersList) {
           minion.setMobGroupInfo(null);
           minion.setSpawn(null);
           minion.setRespawn(false);
         }
         return 0;
       }
       if (this._membersList.size() != 0) {
         setLeader(this._membersList.get(0));
       }
     }

     return this._membersList.size();
   }
   public int getNumOfMembers() {
     return this._membersList.size();
   }

   public boolean isRemoveGroup() {
     return this._isRemoveGroup;
   } public void setRemoveGroup(boolean flag) {
     this._isRemoveGroup = flag;
   }
 }


