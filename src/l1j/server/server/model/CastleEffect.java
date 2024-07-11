 package l1j.server.server.model;

 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class CastleEffect
   extends RepeatTask {
   private final L1PcInstance _pc;

   public CastleEffect(L1PcInstance pc, long inteval, boolean onOff) {
     super(inteval);
     this._pc = pc;
     this._onOff = onOff;
   }
   private boolean _onOff; private boolean _On = false;
   public void execute() {
     try {
       if (this._pc.getMapId() == 13005 || this._pc.getMapId() == 13006) {
         SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
         this._pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
         return;
       }
       if (this._onOff) {
         if (this._On) {
           this._On = false;
           SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
           this._pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
         }
         this._On = true;
         SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true);
         this._pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true));
       } else {

         SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
         this._pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)this._pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
       }

     } catch (Exception exception) {}
   }
 }


