 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1ChatParty
 {
   private final List<L1PcInstance> _membersList = new ArrayList<>();

   private L1PcInstance _leader = null;

   public void addMember(L1PcInstance pc) {
     if (pc == null) {
       throw new NullPointerException();
     }
     if ((this._membersList.size() == Config.ServerAdSetting.PARTYCHATCOUNT && !this._leader.isGm()) || this._membersList.contains(pc)) {
       return;
     }

     if (this._membersList.isEmpty()) {
       setLeader(pc);
     }

     this._membersList.add(pc);
     pc.setChatParty(this);
     S_ServerMessage message = new S_ServerMessage(424, pc.getName());
     for (L1PcInstance member : this._membersList) {
       member.sendPackets((ServerBasePacket)message, false);
     }
   }

   private void removeMember(L1PcInstance pc) {
     if (!this._membersList.contains(pc)) {
       return;
     }

     this._membersList.remove(pc);
     pc.setChatParty(null);
   }

   public boolean isVacancy() {
     return (this._membersList.size() < Config.ServerAdSetting.PARTYCHATCOUNT);
   }

   public int getVacancy() {
     return Config.ServerAdSetting.PARTYCHATCOUNT - this._membersList.size();
   }

   public boolean isMember(L1PcInstance pc) {
     return this._membersList.contains(pc);
   }

   private void setLeader(L1PcInstance pc) {
     this._leader = pc;
   }

   public L1PcInstance getLeader() {
     return this._leader;
   }

   public boolean isLeader(L1PcInstance pc) {
     return (pc.getId() == this._leader.getId());
   }

   public String getMembersNameList() {
     String _result = new String("");
     for (L1PcInstance pc : this._membersList) {
       _result = _result + pc.getName() + " ";
     }
     return _result;
   }


   private void breakup() {
     for (L1PcInstance member : getMembers()) {
       removeMember(member);
       member.sendPackets((ServerBasePacket)new S_ServerMessage(418));
     }
   }

   public void leaveMember(L1PcInstance pc) {
     if (isLeader(pc)) {
       breakup();
     }
     else if (getNumOfMembers() == 2) {
       removeMember(pc);
       L1PcInstance leader = getLeader();
       removeMember(leader);

       sendLeftMessage(pc, pc);
       sendLeftMessage(leader, pc);
     } else {
       removeMember(pc);
       for (L1PcInstance member : getMembers()) {
         sendLeftMessage(member, pc);
       }
       sendLeftMessage(pc, pc);
     }
   }


   public void kickMember(L1PcInstance pc) {
     if (getNumOfMembers() == 2) {
       removeMember(pc);
       L1PcInstance leader = getLeader();
       removeMember(leader);
     } else {
       removeMember(pc);
     }
     pc.sendPackets((ServerBasePacket)new S_ServerMessage(419));
   }

   public L1PcInstance[] getMembers() {
     return this._membersList.<L1PcInstance>toArray(new L1PcInstance[this._membersList.size()]);
   }

   public int getNumOfMembers() {
     return this._membersList.size();
   }

   private void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
     sendTo.sendPackets((ServerBasePacket)new S_ServerMessage(420, left.getName()));
   }
 }


