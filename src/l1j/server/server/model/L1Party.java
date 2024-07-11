 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.logging.Logger;
 import java.util.stream.Stream;
 import l1j.server.Config;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.PartyMember;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST_CHANGE;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_STATUS;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_SYNC_PERIODIC_INFO;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Party;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class L1Party
 {
     private static final Logger _log = Logger.getLogger(L1Party.class.getName());

     protected final List<L1PcInstance> _membersList = new ArrayList<>();
     protected final HashMap<Integer, Integer> _membersMark = new HashMap<>();
     private L1PcInstance _leader = null;

     public void onMoveMember(L1PcInstance pc) {
         refreshPartyMemberStatus(pc);
     }


     public void onDeadMember(L1PcInstance pc) {}


     public void addMember(L1PcInstance pc) {
         if (pc == null) {
             throw new NullPointerException();
         }
         if ((this._membersList.size() == Config.ServerAdSetting.PARTYUSERCOUNT && !this._leader.isGm()) || this._membersList.contains(pc)) {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(417));

             return;
         }
         if (this._membersList.isEmpty()) {
             setLeader(pc);
         }

         this._membersMark.put(Integer.valueOf(pc.getId()), Integer.valueOf(0));
         this._membersList.add(pc);
         pc.setParty(this);

         L1DollInstance doll = pc.getMagicDoll();

         SC_PARTY_MEMBER_LIST noti = SC_PARTY_MEMBER_LIST.newInstance();
         noti.set_leader_name(this._leader.getName());
         noti.add_member(PartyMember.newInstance(pc, 0));
         if (getNumOfMembers() > 1) {
             SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
             PartyMember pm = PartyMember.newInstance(pc, 0);
             change.set_new_user(pm);

             ProtoOutputStream stream = change.writeTo(MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE);
             for (L1PcInstance member : getMembers()) {
                 if (member.getId() != pc.getId()) {
                     member.sendPackets(stream, false);
                     noti.add_member(PartyMember.newInstance(member, ((Integer)this._membersMark.get(Integer.valueOf(member.getId()))).intValue()));
                     if (pc.isInvisble()) {
                         member.onPerceive(pc);
                         if (doll != null) {
                             doll.onPerceive(member);
                         }
                     }
                     if (member.isInvisble()) {
                         pc.onPerceive(member);
                         L1DollInstance memver_doll = member.getMagicDoll();
                         if (memver_doll != null) {
                             memver_doll.onPerceive(pc);
                         }
                     }
                 }
             }
             stream.dispose();
         }
         pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_PARTY_MEMBER_LIST, true);
     }

     protected void removeMember(L1PcInstance pc) {
         L1PcInstance l1PcInstance = pc;
         if (!this._membersList.contains(pc)) {
             return;
         }

         SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
         change.set_out_user(pc.getName());
         broadcast((MJIProtoMessage)change, MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE, true);
         this._membersList.remove(pc);
         pc.setParty(null);

         if (pc._CubeEffect) {
             if (l1PcInstance.hasSkillEffect(220))
                 l1PcInstance.removeSkillEffect(220);
             if (l1PcInstance.hasSkillEffect(215))
                 l1PcInstance.removeSkillEffect(215);
             if (l1PcInstance.hasSkillEffect(210))
                 l1PcInstance.removeSkillEffect(210);
             if (l1PcInstance.hasSkillEffect(205))
                 l1PcInstance.removeSkillEffect(205);
             pc._CubeEffect = false;
         }
         if (!pc.isPassive(MJPassiveID.AURA_PASSIVE.toInt())) {
             pc.auraBuff(false);
         }

         if (pc.isInvisble()) {
             L1DollInstance doll = pc.getMagicDoll();
             for (L1PcInstance member : getMembers()) {
                 member.onPerceive(pc);
                 if (doll != null) {
                     doll.onPerceive(member);
                 }
                 if (member.isInvisble()) {
                     pc.onPerceive(member);
                     L1DollInstance memver_doll = member.getMagicDoll();
                     if (memver_doll != null) {
                         memver_doll.onPerceive(pc);
                     }
                 }
             }
         }
     }

     public boolean isVacancy() {
         return (this._membersList.size() < Config.ServerAdSetting.PARTYUSERCOUNT);
     }

     public int getVacancy() {
         return Config.ServerAdSetting.PARTYUSERCOUNT - this._membersList.size();
     }

     public boolean isMember(L1PcInstance pc) {
         return this._membersList.contains(pc);
     }

     protected void setLeader(L1PcInstance pc) {
         this._leader = pc;
     }

     public L1PcInstance getLeader() {
         return this._leader;
     }

     public boolean isLeader(L1PcInstance pc) {
         return (pc.getId() == this._leader.getId());
     }

     public boolean isAutoDivision(L1PcInstance pc) {
         return (pc.getPartyType() == 1 || pc.getPartyType() == 4);
     }

     public String getMembersNameList() {
         String _result = new String("");
         for (L1PcInstance pc : this._membersList) {
             _result = _result + pc.getName() + " ";
         }
         return _result;
     }

     public void refresh(L1PcInstance pc) {
         L1PcInstance leader = getLeader();
         for (L1PcInstance member : getMembers()) {
             if (member != null && pc.getId() != member.getId())
             {

                 if (leader != null && leader.getId() == pc.getId()) {

                     member.sendPackets((ServerBasePacket)new S_Party(1730, pc));
                 } else {

                     member.sendPackets((ServerBasePacket)new S_Party(1729, pc));
                 }  }
         }
     }

     public void memberDie(L1PcInstance pc) {
         for (L1PcInstance member : getMembers()) {
             if (pc.getId() != member.getId())
             {


                 member.sendPackets((ServerBasePacket)new S_Party(1728, pc)); }
         }
     }

     public void broadcastPartyList() {
         SC_PARTY_MEMBER_LIST noti = SC_PARTY_MEMBER_LIST.newInstance();
         noti.set_leader_name(this._leader.getName());
         for (L1PcInstance member : getMembers()) {
             PartyMember pm = PartyMember.newInstance(member, ((Integer)this._membersMark.get(Integer.valueOf(member.getId()))).intValue());
             noti.add_member(pm);
         }
         broadcast((MJIProtoMessage)noti, MJEProtoMessages.SC_PARTY_MEMBER_LIST, true);
     }

     public void refreshPartyList() {
         SC_PARTY_SYNC_PERIODIC_INFO info = SC_PARTY_SYNC_PERIODIC_INFO.newInstance();
         for (L1PcInstance member : getMembers()) {
             SC_PARTY_MEMBER_STATUS status = SC_PARTY_MEMBER_STATUS.newInstance(member, ((Integer)this._membersMark.get(Integer.valueOf(member.getId()))).intValue());
             info.add_status(status);
         }
         broadcast((MJIProtoMessage)info, MJEProtoMessages.SC_PARTY_SYNC_PERIODIC_INFO, true);
     }

     public void handshakePartyMemberStatus(L1PcInstance pc1, L1PcInstance pc2) {
         SC_PARTY_MEMBER_STATUS status = SC_PARTY_MEMBER_STATUS.newInstance(pc1, ((Integer)this._membersMark.get(Integer.valueOf(pc1.getId()))).intValue());
         pc2.sendPackets((MJIProtoMessage)status, MJEProtoMessages.SC_PARTY_MEMBER_STATUS, true);
         status = SC_PARTY_MEMBER_STATUS.newInstance(pc2, ((Integer)this._membersMark.get(Integer.valueOf(pc2.getId()))).intValue());
         pc1.sendPackets((MJIProtoMessage)status, MJEProtoMessages.SC_PARTY_MEMBER_STATUS, true);
     }

     public void refreshPartyMemberStatus(L1PcInstance pc) {
         if (pc != null) {
             SC_PARTY_MEMBER_STATUS status = SC_PARTY_MEMBER_STATUS.newInstance(pc, ((Integer)this._membersMark.get(Integer.valueOf(pc.getId()))).intValue());
             broadcast((MJIProtoMessage)status, MJEProtoMessages.SC_PARTY_MEMBER_STATUS, true);
         }
     }

     protected void breakup() {
         for (L1PcInstance member : getMembers()) {
             removeMember(member);
             member.sendPackets((ServerBasePacket)new S_ServerMessage(418));
         }
     }

     public void passLeader(L1PcInstance pc) {
         setLeader(pc);
         broadcastPartyList();
     }




     public void leaveMember(L1PcInstance pc) {
         if (isLeader(pc) || getNumOfMembers() == 2) {

             breakup();
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
             breakup();
         } else {
             removeMember(pc);
             for (L1PcInstance member : getMembers()) {
                 sendLeftMessage(member, pc);
             }
             sendKickMessage(pc);
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(419));
     }

     public L1PcInstance[] getMembers() {
         return this._membersList.<L1PcInstance>toArray(new L1PcInstance[this._membersList.size()]);
     }

     public int getNumOfMembers() {
         return this._membersList.size();
     }

     private void sendKickMessage(L1PcInstance kickpc) {
         kickpc.sendPackets((ServerBasePacket)new S_ServerMessage(419));
     }

     protected void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
         sendTo.sendPackets((ServerBasePacket)new S_ServerMessage(420, left.getName()));
     }

     public List<L1PcInstance> getList() {
         return this._membersList;
     }

     public Stream<L1PcInstance> createMembersStream() {
         return this._membersList.stream();
     }

     public Stream<L1PcInstance> createVisibleMembersStream(L1PcInstance pc) {
         int mid = pc.getMapId();
         L1Location pt = pc.getLocation();
         return createMembersStream().filter(m ->

                 (m.getMapId() == mid && pt.getTileLineDistance(m.getLocation()) <= 50));
     }


     public void broadcast(ServerBasePacket packet) {
         createMembersStream().forEach(pc -> pc.sendPackets(packet, false));
         packet.clear();
     }

     public void broadcast(MJIProtoMessage message, int messageId) {
         broadcast(message, messageId, true);
     }

     public void broadcast(MJIProtoMessage message, MJEProtoMessages e, boolean isClear) {
         broadcast(message, e.toInt(), isClear);
     }

     public void broadcast(MJIProtoMessage message, MJEProtoMessages e) {
         broadcast(message, e, true);
     }

     public void broadcast(MJIProtoMessage message, int messageId, boolean isClear) {
         if (message.isInitialized()) {
             ProtoOutputStream stream = message.writeTo(MJEProtoMessages.fromInt(messageId));
             for (L1PcInstance member : getMembers()) {
                 member.sendPackets(stream, false);
             }
             if (isClear)
                 message.dispose();
         } else {
             MJEProtoMessages.printNotInitialized("party broadcast.", messageId, message.getInitializeBit());
         }
     }

     public void broadcastRootMent(ServerBasePacket packet) {
         createMembersStream().filter(pc -> pc.RootMent).forEach(pc -> pc.sendPackets(packet, false));
         packet.clear();
     }

     public void setPartyMark(L1PcInstance target, int markId) {
         this._membersMark.put(Integer.valueOf(target.getId()), Integer.valueOf(markId));
         SC_PARTY_MEMBER_LIST_CHANGE change = SC_PARTY_MEMBER_LIST_CHANGE.newInstance();
         change.set_new_user(PartyMember.newInstance(target, markId));
         broadcast((MJIProtoMessage)change, MJEProtoMessages.SC_PARTY_MEMBER_LIST_CHANGE, true);
     }
 }


