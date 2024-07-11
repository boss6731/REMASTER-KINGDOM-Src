 package MJShiftObject.Battle;

 import l1j.server.server.model.Instance.L1PcInstance;


 public class MJShiftBattleCharacterInfo
 {
   public L1PcInstance owner;
   public String owner_name;

   protected MJShiftBattleCharacterInfo(L1PcInstance pc, int destination_id, String source_name, MJShiftBattleTeamInfo<? extends MJShiftBattleCharacterInfo> tInfo) {
     this.owner = pc;
     this.owner_id = destination_id;
     this.home_server_name = pc.get_server_description();
     this.owner_name = source_name;
     this.team_info = tInfo;
   }
   public String home_server_name; public int owner_id; public MJShiftBattleTeamInfo<? extends MJShiftBattleCharacterInfo> team_info;
   public String to_name_pair() {
     return String.format("(%s)%s", new Object[] { this.home_server_name, this.owner_name });
   }
 }


