 package l1j.server.server.model;

 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.SingleTask;
 import l1j.server.server.datatables.MonsterParalyzeDelay;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1CurseParalysis
   extends L1Paralysis {
   private final L1Character _target;
   private final int _delay;
   private final int _time;
   private SingleTask _timer;

   private class ParalysisDelayTimer extends SingleTask {
     public void execute() {
       if (isActive())
       {

         if (L1CurseParalysis.this._target instanceof L1PcInstance) {
           L1PcInstance player = (L1PcInstance)L1CurseParalysis.this._target;

           if (!player.isDead()) {
             SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
             noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
             noti.set_spell_id(33);
             noti.set_duration(L1CurseParalysis.this._time / 1000);
             noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
             noti.set_on_icon_id(5270);
             noti.set_off_icon_id(5270);
             noti.set_icon_priority(10);
             noti.set_tooltip_str_id(213);
             noti.set_end_str_id(0);
             noti.set_is_good(false);
             player.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
             player.sendPackets((ServerBasePacket)new S_Paralysis(4, true));
           }
           L1CurseParalysis.this._timer = new L1CurseParalysis.ParalysisTimer();
           L1CurseParalysis.this._target.setParalyzed(true);
           GeneralThreadPool.getInstance().schedule((Runnable)L1CurseParalysis.this._timer, L1CurseParalysis.this._time);
         }  }
     }

     private ParalysisDelayTimer() {} }

   private class ParalysisTimer extends SingleTask { private ParalysisTimer() {}

     public void execute() {
       L1CurseParalysis.this._timer = null;
       L1CurseParalysis.this.cure();
     } }







   private L1CurseParalysis(L1Character cha, int skill_id) {
     this._target = cha;
     MonsterParalyzeDelay.MonsterParalyze paralyze = MonsterParalyzeDelay.getInstance().get_paralyze(skill_id);
     this._delay = paralyze.paralyze_delay;
     this._time = paralyze.paralyze_millis;

     curse();
   }

   private void curse() {
     if (this._target instanceof L1PcInstance) {
       L1PcInstance player = (L1PcInstance)this._target;
       player.sendPackets((ServerBasePacket)new S_ServerMessage(212));
     }

     this._target.setPoisonEffect(2);
     this._timer = new ParalysisDelayTimer();
     this._target.setSkillEffect(1010, this._delay);
     if (this._target instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this._target;
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
       noti.set_spell_id(33);
       noti.set_duration(this._delay / 1000);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
       noti.set_on_icon_id(5270);
       noti.set_off_icon_id(5270);
       noti.set_icon_priority(10);
       noti.set_tooltip_str_id(212);
       noti.set_end_str_id(0);
       noti.set_is_good(false);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
     }
     GeneralThreadPool.getInstance().schedule((Runnable)this._timer, this._delay);
   }

   public static boolean curse(L1Character cha, int skill_id) {
     if (!(cha instanceof L1PcInstance) && !(cha instanceof l1j.server.server.model.Instance.L1MonsterInstance)) {
       return false;
     }

     if (cha.hasSkillEffect(1010) || cha
       .hasSkillEffect(1011)) {
       return false;
     }
     cha.setParalaysis(new L1CurseParalysis(cha, skill_id));
     return true;
   }


   public int getEffectId() {
     return 2;
   }



   public void cure() {
     if (this._timer != null) {
       this._timer.cancel();

       if (this._timer instanceof ParalysisTimer) {
         if (!this._timer.isExecuted()) {
           this._timer.execute();
         }
       } else {
         this._target.killSkillEffectTimer(1008);
       }

       this._timer = null;
     }

     this._target.setPoisonEffect(0);
     this._target.setParalaysis(null);

     if (this._target instanceof L1PcInstance) {

       L1PcInstance pc = (L1PcInstance)this._target;
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
       noti.set_spell_id(33);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
       noti.set_is_good(false);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
       if (!pc.isDead()) {
         pc.sendPackets((ServerBasePacket)new S_Paralysis(4, false));
       }
     }
     this._target.setParalyzed(false);
     this._target.removeSkillEffect(1010);
     this._target.removeSkillEffect(1011);
   }
 }


